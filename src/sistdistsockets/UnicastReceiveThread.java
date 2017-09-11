/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * peer invocando uma instância da classe UnicastMessageManager.
 *
 * @author davi
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
     * bloqueia a Thread até que uma conexão seja feita, e então inicializa um
     * objeto da classe UnicastMessageManager para processar a mensagem
     * recebida.</p>
     */
    @Override
    public void run() {
        try {
            ObjectOutputStream out;
            ObjectInputStream in;
            Socket clientSocket;
            Message data;
            while (true) {
                clientSocket = listenSocket.accept();
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());
                data = (Message) in.readObject();
                if (data.getMessage().startsWith("wannabuy")) {
                    String descr = data.getMessage().substring(8);
                    Map<Integer, Set<Product>> prodEverybody = peer.getPeersProducts();
                    Set<Integer> keys = prodEverybody.keySet();
                    Set<Product> products;
                    Map<Integer, Product> prodBuy = new HashMap();
                    for (int iD : keys) {
                        products = prodEverybody.get(iD);
                        for (Product prod : products) {
                            if (prod.getDescricao().equals(descr)) {
                                prodBuy.put(iD, prod);
                                break;
                            }
                        }
                    }
                    out.writeObject(prodBuy);
                } else if (data.getMessage().startsWith("wannaKey")) {
                    int id = Integer.parseInt(data.getMessage().substring(8));
                    PeerAnswer key = peer.getPeers().get(id);
                    out.writeObject(key);
                } else if (data.getMessage().equals("myKey")) {
                    PublicKeyMessage mess = (PublicKeyMessage) data;
                    peer.setKeyForAPeer(data.getSenderID(), mess.getPublicKey());
                    System.out.println("Recebi a chave publica do peer " + data.getSenderID());
                } else if (data.getMessage().equals("myProduct")) {
                    ProductMessage mess = (ProductMessage) data;
                    peer.setProductForAPeer(data.getSenderID(), mess.getProduct());
                    System.out.println("Recebi um produto do peer " + data.getSenderID() + ": " + mess.getProduct());
                } else if (data.getMessage().equals("byebye")) {
                    peer.getPeers().remove(data.getSenderID());
                    peer.getPeersProducts().remove(data.getSenderID());
                } else {
                    System.out.println("dado da compra recebido com criptografia: " + data.getMessage());
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.DECRYPT_MODE, peer.getPrivateKey());
                    byte[] mess = DatatypeConverter.parseHexBinary(data.getMessage());
                    String message = new String(cipher.doFinal(mess), StandardCharsets.UTF_8);
                    System.out.println("descriptografado " + message);
                    boolean encontrou = false;
                    for (Product p: peer.getProdutos()) {
                        if (p.getDescricao().equals(message)) {
                            encontrou = true;
                            break;
                        }
                    }
                    if (encontrou)
                        out.writeObject(1);
                    else
                        out.writeObject(-1);
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
