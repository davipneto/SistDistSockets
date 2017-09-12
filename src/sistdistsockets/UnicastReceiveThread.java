package sistdistsockets;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.*;
import java.security.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

/**
 * A classe UnicastReceiveThread possui um ServerSocket responsável por ficar
 * ouvindo em uma porta, e processa as mensagens recebidas para um determinado
 * peer.
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public class UnicastReceiveThread extends Thread {

    private Peer peer;
    private ServerSocket listenSocket;

    /**
     * Cria uma UnicastReceiveThread para o peer especificado, alocando uma
     * porta disponível para inicializar um objeto ServerSocket.
     *
     * @param peer o peer a ser associado com esse objeto
     */
    public UnicastReceiveThread(Peer peer) {
        this.peer = peer;
        try {
            listenSocket = new ServerSocket(0);
            peer.setPort(listenSocket.getLocalPort());
        } catch (IOException ex) {
            Logger.getLogger(UnicastReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método herdado da classe Thread. Quando chamado o método <i>start</i>,
     * ele é invocado em uma nova thread, rodando em paralelo com os outros
     * processos.<p>
     * Ele invoca o método <i>accept</i> do objeto da classe ServerSocket, que
     * bloqueia a Thread até que uma conexão seja feita, e então processa a
     * mensagem recebida.</p>
     */
    @Override
    public void run() {
        try {
            ObjectOutputStream out;
            ObjectInputStream in;
            Socket clientSocket;
            Message data;
            while (true) {
                //inicializado o socket que ouve
                clientSocket = listenSocket.accept();
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());
                //armazena e converte em um tipo Messagem a mensagem recebida pelo socket
                data = (Message) in.readObject();
                //se o peer deseja comprar algum produto
                if (data.getMessage().startsWith("wannabuy")) {
                    //repara da mensagem a descrição do produto desejado
                    String descr = data.getMessage().substring(8);
                    //encontra todos os produtos vendidos pelos peers
                    Map<Integer, Set<Product>> prodEverybody = peer.getPeersProducts();
                    Set<Integer> keys = prodEverybody.keySet();
                    //set de produtos auxiliar
                    Set<Product> products;
                    //map com os produtos que batem com a descrição desejada
                    Map<Integer, Product> prodBuy = new HashMap();
                    for (int iD : keys) {
                        products = prodEverybody.get(iD);
                        for (Product prod : products) {
                            //se a descrição bate com a desejada armazena o produto e o id que possui no map
                            if (prod.getDescricao().equals(descr)) {
                                prodBuy.put(iD, prod);
                                break;
                            }
                        }
                    }
                    //envia como resposta o map de produtos e ids resultantes
                    out.writeObject(prodBuy);
                } //se o peer deseja a chave pública do peer com que deseja realizar compra 
                else if (data.getMessage().startsWith("wannaKey")) {
                    //pega o id do peer que deseja a chave
                    int id = Integer.parseInt(data.getMessage().substring(8));
                    //armazena o objeto PeerAnswer com os dados requisitados
                    PeerAnswer key = peer.getPeers().get(id);
                    //envia como resposta o objeto com os dados requisitados
                    out.writeObject(key);
                } //se o peer esta enviado a sua chave pública para o indexador
                else if (data.getMessage().equals("myKey")) {
                    //converte a mensagem para o tipo PublicKeyMessage
                    PublicKeyMessage mess = (PublicKeyMessage) data;
                    //adiciona os dados à lista de associa um id com a sua chave pública
                    peer.setKeyForAPeer(data.getSenderID(), mess.getPublicKey());
                    //informa que recebeu a chave pública do peer
                    System.out.println("Recebi a chave publica do peer " + data.getSenderID());

                } //se o peer deseja enviar os seus produtos
                else if (data.getMessage().equals("myProduct")) {
                    ProductMessage mess = (ProductMessage) data;
                    peer.setProductForAPeer(data.getSenderID(), mess.getProduct());
                    System.out.println("Recebi um produto do peer " + data.getSenderID() + ": " + mess.getProduct());
                } //se o peer deseja sair da aplicação
                else if (data.getMessage().equals("byebye")) {
                    peer.getPeers().remove(data.getSenderID());
                    peer.getPeersProducts().remove(data.getSenderID());
                } //como última opção o peer envia uma mensagem de compra criptografada
                else {
                    //exibi o dado criptografado
                    System.out.println("dado da compra recebido com criptografia: " + data.getMessage());
                    //inicia uma instância de criptografia com o algoritmo RSA
                    Cipher cipher = Cipher.getInstance("RSA");
                    //inicia modo de descriptografia com a chave privada
                    cipher.init(Cipher.DECRYPT_MODE, peer.getPrivateKey());
                    //convente a mensagem para um vetor de byte
                    byte[] mess = DatatypeConverter.parseHexBinary(data.getMessage());
                    //descriptografa e converte dado para String
                    String message = new String(cipher.doFinal(mess), StandardCharsets.UTF_8);
                    //exibe a mensagem descriptografada
                    System.out.println("descriptografado " + message);
                    //faz a busca para garantir que possui o produto requisitado
                    boolean encontrou = false;
                    for (Product p : peer.getProdutos()) {
                        if (p.getDescricao().equals(message)) {
                            encontrou = true;
                            break;
                        }
                    }
                    //envia como resposta 1 ou -1 se encontrou ou não o produto
                    if (encontrou) {
                        out.writeObject(1);
                    } else {
                        out.writeObject(-1);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(UnicastReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UnicastReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UnicastReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(UnicastReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(UnicastReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(UnicastReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(UnicastReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
