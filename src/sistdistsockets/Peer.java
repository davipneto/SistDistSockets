/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.*;
import javax.crypto.AEADBadTagException.*;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

/**
 * A classe Peer encapsula as informações de um nó da arquitetura Peer-to-Peer de redes
 * de computadores, que funciona tanto como cliente quanto servidor, permitindo
 * compartilhamento de serviços e dados sem a necessidade de um servidor central.<p>
 * Nessa implementação, um dos peers funciona como um indexador, que é responsável por
 * fornecer as chaves públicas para comunicação unicast entre os pares, e fornecer informações
 * sobre os produtos disponíveis para venda em cada peer.</p>
 * @author davi
 * @see java.net.MulticastSocket
 * @see MultipeerReceiveThread
 */

public class Peer {
    
    private int iD;
    private int port;
    private String group;
    //variável setada em true quando o peer recebe a mensagem indicando que o indexador está vivo
    private boolean indexerOn;
    private int indexerPort;
    /*
    lista dos peers ativos no grupo, utilizada quando o indexador cai, para que o peer compare seu iD com o iD
    dos outros peers, e se o menor iD for o dele, invocara o metodo para ser elegido como indexador
    */
    private ArrayList<Integer> peersOnGroup;
    private Set<Product> produtos;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Map<Integer,PublicKey> peers;
    private Map<Integer,Set<Product>> peersProducts;
    
    /**
     * Cria um peer com id e grupo multicast especificados, e gera o par de chaves pública
     * e privada.
     * @param id o identificador do par
     * @param group o grupo Multicast ao que o par vai pertencer
     */
    
    public Peer(int id, String group) {
        this.iD = id;
        this.port = -1;
        this.group = group;
        peersOnGroup = new ArrayList();
        //inicialmente, o peer nao conhece seu indexador
        indexerPort = -1;
        indexerOn = false;
        produtos = new HashSet();
        peers = new HashMap();
        peersProducts = new HashMap();
        try {
            //Inicializa o par de chaves do peer
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(512);
            KeyPair keypair = kpg.generateKeyPair();
            privateKey = keypair.getPrivate();
            publicKey = keypair.getPublic();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Falha na geração das chaves do peer " + iD);
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Instancia a TimerTask que vai ficar verificando de 5 em 5 segundos se o indexador esta vivo
        VerifyAliveIndexerTask vai = new VerifyAliveIndexerTask(this);
        Timer verifyAliveIndexerTimer = new Timer();
        verifyAliveIndexerTimer.scheduleAtFixedRate(vai, 5000, 5000);
    }
    
    /**
     * Envia uma mensagem para o grupo Multicast.
     * @param message a mensagem a ser enviada
     */
    public void send(String message) {
        MulticastSocket s = null;
        try {
            //inicializa uma instância da classe MulticastSocket na porta do grupo
            s = new MulticastSocket(6789);
            //insere a instância no grupo
            InetAddress inetGroup = InetAddress.getByName(group);
            s.joinGroup(inetGroup);
            //envia a mensagem e deixa o grupo
            Message m = new Message(iD, message);
            message = m.toString();
            DatagramPacket messageOut = new DatagramPacket(message.getBytes(), message.getBytes().length, inetGroup, 6789);
            s.send(messageOut);
            s.leaveGroup(inetGroup);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            //fecha o socket caso não esteja nulo
            if (s != null) {
                s.close();
            }
        }
    }
    
    /**
     * Envia uma mensagem Unicast para a porta especifica
     * @param message a mensagem a ser enviada
     * @param port o número da porta 
     */
    public void send(Message message, int port) {
        Socket sock = null;
        try {
            sock = new Socket("localhost", port);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            out.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(SistDistSockets.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Map<Integer,Product> sendBuyRequest(Message message, int port){
        Socket sock = null;
        try {
            sock = new Socket("localhost", port);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            out.writeObject(message);
            //esperar a resposta
            Map answer = (Map) in.readObject();
            return answer;
        } catch (IOException ex) {
            Logger.getLogger(SistDistSockets.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public PublicKey sendBuy(int id){
        Socket sock = null;
        Message m = new Message(this.iD, "wannaKey"+id);
        try {
            sock = new Socket("localhost", port);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            out.writeObject(m);
            //esperar a resposta
            PublicKey panswer = (PublicKey) in.readObject();
            return panswer;
        } catch (IOException ex) {
            Logger.getLogger(SistDistSockets.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Define este par como o indexador.
     */
    public void beTheIndexer() {
        System.out.println("Eu, peer " + iD + " vou ser o indexador!!");
        //envia a mensagem de novo indexador para o grupo Multicast, para os outros peers informarem seus dados
        send("newIndexer" + port);
        //ativa a TimerTask q vai enviar de 5 em 5s uma mensagem para os processos saberem que o indexador está vivo
        TimerTask informAliveIndexerTimerTask = new TimerTask() {
            @Override
            public void run() {
                send("indexerHi" + port);
            }
        };
        Timer informAliveIndexerTimer = new Timer();
        informAliveIndexerTimer.schedule(informAliveIndexerTimerTask, 0, 5000);
    }
    
    /**
     * Adiciona o iD especificado a uma lista de iDs utilizada para eleger um novo indexador
     * quando o atual cair.
     * @param iD o iD a ser adicionado na lista.
     */
    public void putPeersOnGroup(int iD) {
        peersOnGroup.add(iD);
    }

    /**
     * Retorna o iD do par.
     * @return um valor <i>int</i> que representa o identificador do par.
     */
    public int getID() {
        return iD;
    }
    
    /**
     * Retorna a porta em que o par está escutando na comunicação Unicast.
     * @return um valor <i>int</i> que representa a porta do par para comunicação Unicast.
     */
    public int getPort() {
        return port;
    }

    /**
     * Retorna o grupo Multicast em que o par está inserido.
     * @return uma <i>String</i> que representa o IP do grupo Multicast
     */
    public String getGroup() {
        return group;
    }

    /**
     * Retorna a porta em que o indexador está escutando na comunicação Unicast.
     * @return um valor <i>int</i> que representa a porta do par indexador para comunicação Unicast
     */
    public int getIndexerPort() {
        return indexerPort;
    }

    /**
     * Retorna a lista de iDs dos peers do grupo Multicast, que é preenchida quando o indexador cai.
     * @return um <i>ArrayList</i> de inteiros contendo os iDs dos peers do grupo Multicast
     */
    public ArrayList<Integer> getPeersOnGroup() {
        return peersOnGroup;
    }

    public Map<Integer, Set<Product>> getPeersProducts() {
        return peersProducts;
    }

    
    
    /**
     * Teste se o indexador está vivo.
     * @return um <i>boolean</i> indicando se o indexador está vivo ou não
     */
    public boolean isIndexerOn() {
        return indexerOn;
    }

    /**
     * Define se o indexador está vivo.
     * @param indexerOn um <i>boolean</i> indicando se o indexador está vivo ou não
     */
    public void setIndexerOn(boolean indexerOn) {
        this.indexerOn = indexerOn;
    }

    /**
     * Seta o valor da porta do indexador.
     * @param indexerPort um <i>int</i> indicando o valor da porta do indexador
     */
    public void setIndexerPort(int indexerPort) {
        this.indexerPort = indexerPort;
    }

    /**
     * Seta o valor da porta para comunicação Unicast do peer.
     * @param port um <i>int</i> indicando o valor da porta do peer
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Retorna a chave pública do peer
     * @return uma <i>PublicKey</i> representando a chave pública do peer
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    public Set<Product> getProdutos() {
        return produtos;
    }
    
    public void setProduct(Product product){
        produtos.add(product);
    }
    
    public void setKeyForAPeer (int iD, PublicKey key) {
        peers.put(iD, key);
    }
    
    public void setProductForAPeer (int iD, Product product) {
       if (!peersProducts.containsKey(iD)) {
           peersProducts.put(iD, new HashSet());
       }
       Set products = peersProducts.get(iD);
       products.add(product);
    }

    public Map<Integer, PublicKey> getPeers() {
        return peers;
    }
    
    

}
