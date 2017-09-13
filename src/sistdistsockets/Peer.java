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
 * A classe Peer encapsula as informações de um nó da arquitetura Peer-to-Peer
 * de redes de computadores, que funciona tanto como cliente quanto servidor,
 * permitindo compartilhamento de serviços e dados sem a necessidade de um
 * servidor central.<p>
 * Nessa implementação, um dos peers funciona como um indexador, que é
 * responsável por fornecer as chaves públicas para comunicação unicast entre os
 * pares, e fornecer informações sobre os produtos disponíveis para venda em
 * cada peer.</p>
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 * @see java.net.MulticastSocket
 * @see MultipeerReceiveThread
 */
public class Peer {

    private int iD;
    private String ip;
    private int port;
    private String group;
    //variável setada em true quando o peer recebe a mensagem indicando que o indexador está vivo
    private boolean indexerOn;
    //variáveis que armazenam, respectivamente, IP e porta do indexador para comunicação unicast
    private String indexerIP;
    private int indexerPort;
    /*
    lista dos peers ativos no grupo, utilizada quando o indexador cai, para que o peer compare seu iD com o iD
    dos outros peers, e se o menor iD for o dele, invocara o metodo para ser elegido como indexador
     */
    private ArrayList<Integer> peersOnGroup;
    private Set<Product> produtos;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Map<Integer, PeerAnswer> peers;
    private Map<Integer, Set<Product>> peersProducts;
    //map que armazena reputação local dos outros peers
    private Map<Integer, Integer> reputations;

    /**
     * Cria um peer com id e grupo multicast especificados, e gera o par de
     * chaves pública e privada.
     *
     * @param id o identificador do par
     * @param group o grupo Multicast ao que o par vai pertencer
     */
    public Peer(int id, String group) {
        this.iD = id;
        this.port = -1;
        this.indexerIP = null;
        this.group = group;
        peersOnGroup = new ArrayList();
        //inicialmente, o peer nao conhece seu indexador
        indexerPort = -1;
        indexerOn = false;
        produtos = new HashSet();
        peers = new HashMap();
        peersProducts = new HashMap();
        reputations = new HashMap();
        try {
            this.ip = InetAddress.getLocalHost().getHostAddress();
            //Inicializa o par de chaves do peer
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair keypair = kpg.generateKeyPair();
            privateKey = keypair.getPrivate();
            publicKey = keypair.getPublic();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Falha na geração das chaves do peer " + iD);
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Instancia a TimerTask que vai ficar verificando de 5 em 5 segundos se o indexador esta vivo
        VerifyAliveIndexerTask vai = new VerifyAliveIndexerTask(this);
        Timer verifyAliveIndexerTimer = new Timer();
        verifyAliveIndexerTimer.scheduleAtFixedRate(vai, 5000, 5000);
    }

    /**
     * Envia uma mensagem para o grupo Multicast.
     *
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
     *
     * @param message a mensagem a ser enviada
     * @param port o número da porta
     */
    public void send(Message message, int port) {
        Socket sock = null;
        try {
            //trocar localhost pelo ip do indexador
            sock = new Socket(indexerIP, port);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            out.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(SistDistSockets.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Envia uma mensagem Unicast para a porta do indexador requisitando quais
     * peers possuiem o produto procurado
     *
     * @param message a mensagem a ser enviada
     * @param port o número da porta
     */
    public Map<Integer, Product> sendBuyRequest(Message message, int port) {
        Socket sock = null;
        try {
            //trocar localhost pelo ip do indexador
            sock = new Socket(indexerIP, port);
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

    /**
     * Envia uma mensagem Unicast para a porta do indexador requisitando chave
     * pública do peer com que o peer atual deseja fazer uma transação
     *
     * @param id do peer que deseja realizar comunicação
     * @return um objeto <i>PeerAnswer</i> que representa o peer com o objeto a
     * ser comprado.
     */
    public PeerAnswer sendBuy(int id) {
        Socket sock = null;
        Message m = new Message(this.iD, "wannaKey" + id);
        try {
            //trocar localhost pelo ip do indexador
            sock = new Socket(indexerIP, indexerPort);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            out.writeObject(m);
            //esperar a resposta
            PeerAnswer panswer = (PeerAnswer) in.readObject();
            return panswer;
        } catch (IOException ex) {
            Logger.getLogger(SistDistSockets.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Envia uma mensagem Unicast para ip e porta especifica. Utilizado para
     * realizar a compra.
     *
     * @param message a mensagem a ser enviada
     * @param ip o número de ip
     * @param port o número da porta
     * @return um valor <i>int</i> que representa se a transação foi realizada
     * com sucesso(1) ou não(-1)
     */
    public int sendBuyFS(Message message, String ip, int port) {
        Socket sock = null;
        try {
            sock = new Socket(ip, port);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            out.writeObject(message);
            //esperar a resposta
            int answer = (Integer) in.readObject();
            return answer;
        } catch (IOException ex) {
            Logger.getLogger(SistDistSockets.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     * Envia uma mensagem Unicast para o indexador anunciando saída do grupo
     */
    public void sendBye() {
        Socket sock = null;
        Message m = new Message(this.iD, "byebye");
        try {
            //trocar localhost pelo ip do indexador
            sock = new Socket(indexerIP, indexerPort);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            out.writeObject(m);
        } catch (IOException ex) {
            Logger.getLogger(SistDistSockets.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Define este par como o indexador.
     */
    public void beTheIndexer() {
        System.out.println("Eu, peer " + iD + " vou ser o indexador!!");
        //envia a mensagem de novo indexador para o grupo Multicast, para os outros peers informarem seus dados
        send("newIndexer" + String.valueOf(port).length() + port + ip);
        //ativa a TimerTask q vai enviar de 5 em 5s uma mensagem para os processos saberem que o indexador está vivo
        TimerTask informAliveIndexerTimerTask = new TimerTask() {
            @Override
            public void run() {
                send("indexerHi" + String.valueOf(port).length() + port + ip);
            }
        };
        Timer informAliveIndexerTimer = new Timer();
        informAliveIndexerTimer.schedule(informAliveIndexerTimerTask, 0, 5000);
    }

    /**
     * Adiciona o iD especificado a uma lista de iDs utilizada para eleger um
     * novo indexador quando o atual cair.
     *
     * @param iD o iD a ser adicionado na lista.
     */
    public void putPeersOnGroup(int iD) {
        peersOnGroup.add(iD);
    }

    /**
     * Retorna o iD do par.
     *
     * @return um valor <i>int</i> que representa o identificador do par.
     */
    public int getID() {
        return iD;
    }

    /**
     * Retorna a porta em que o par está escutando na comunicação Unicast.
     *
     * @return um valor <i>int</i> que representa a porta do par para
     * comunicação Unicast.
     */
    public int getPort() {
        return port;
    }

    /**
     * Retorna o grupo Multicast em que o par está inserido.
     *
     * @return uma <i>String</i> que representa o IP do grupo Multicast
     */
    public String getGroup() {
        return group;
    }

    /**
     * Retorna a porta em que o indexador está escutando na comunicação Unicast.
     *
     * @return um valor <i>int</i> que representa a porta do par indexador para
     * comunicação Unicast
     */
    public int getIndexerPort() {
        return indexerPort;
    }

    /**
     * Retorna a lista de iDs dos peers do grupo Multicast, que é preenchida
     * quando o indexador cai.
     *
     * @return um <i>ArrayList</i> de inteiros contendo os iDs dos peers do
     * grupo Multicast
     */
    public ArrayList<Integer> getPeersOnGroup() {
        return peersOnGroup;
    }

    /**
     * Retorna a lista de produtos por iDs dos peers do grupo Multicast.
     *
     * @return um <i>ArrayList</i> de inteiros contendo os iDs dos peers do
     * grupo Multicast
     */
    public Map<Integer, Set<Product>> getPeersProducts() {
        return peersProducts;
    }

    /**
     * Teste se o indexador está vivo.
     *
     * @return um <i>boolean</i> indicando se o indexador está vivo ou não
     */
    public boolean isIndexerOn() {
        return indexerOn;
    }

    /**
     * Define se o indexador está vivo.
     *
     * @param indexerOn um <i>boolean</i> indicando se o indexador está vivo ou
     * não
     */
    public void setIndexerOn(boolean indexerOn) {
        this.indexerOn = indexerOn;
    }

    /**
     * Seta o valor da porta do indexador.
     *
     * @param indexerPort um <i>int</i> indicando o valor da porta do indexador
     */
    public void setIndexerPort(int indexerPort) {
        this.indexerPort = indexerPort;
    }

    /**
     * Seta o valor da porta para comunicação Unicast do peer.
     *
     * @param port um <i>int</i> indicando o valor da porta do peer
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Retorna a chave pública do peer
     *
     * @return uma <i>PublicKey</i> representando a chave pública do peer
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Retorna o set de produtos do peer
     *
     * @return um <i>Set<Products></i> representando o conjunto de produtos
     */
    public Set<Product> getProdutos() {
        return produtos;
    }

    /**
     * Retorna o ip do peer
     *
     * @return uma <i>String</i> representando o ip do peer
     */
    public String getIp() {
        return ip;
    }

    /**
     * Seta um novo produto à lista de produtos do peer
     */
    public void setProduct(Product product) {
        produtos.add(product);
    }

    /**
     * Seta a lista de peers com o id, porta, ip e PublicKey
     *
     * @param iD do peer
     * @param key que representa a porta, ip e PublicKey do peer
     */
    public void setKeyForAPeer(int iD, PeerAnswer key) {
        peers.put(iD, key);
    }

    /**
     * Adiciona à lista do indexador o produto que o peer está vendendo
     *
     * @param id é o id do peer
     * @param product é o produto que o peer está vendendo
     */
    public void setProductForAPeer(int iD, Product product) {
        if (!peersProducts.containsKey(iD)) {
            peersProducts.put(iD, new HashSet());
        }
        Set products = peersProducts.get(iD);
        products.add(product);
    }

    /**
     * Retorna a lista de peers com suas respectivas portas, ips e PublicKeys
     *
     * @return peers do tipo Map que associa um id com um objeto do tipo
     * PeerAnswer
     */
    public Map<Integer, PeerAnswer> getPeers() {
        return peers;
    }

    /**
     * Retorna a chave privada do peer
     *
     * @return tipo <i>PrivateKey</i> que armazena a chave privada de um peer
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * Atualiza as reputações dos peers depois de realizada a compra
     *
     * @param id do peer que vendeu
     * @param rep possui 1 ou -1 para adicionar à reputação do peer
     */
    public void setReputations(int id, int rep) {
        if (!reputations.containsKey(id)) {
            reputations.put(id, rep);
        } else {
            reputations.put(id, reputations.get(id) + rep);
        }
    }

    /**
     * Retorna a lista de reputações dos peers que já realizaram alguma
     * transação com o peer
     *
     * @return do tipo <i>Map<Integer, Integer></i> que associa o id do peer com a sua reputação
     */
    public Map<Integer, Integer> getReputations() {
        return reputations;
    }

    /**
     * Seta o ip do indexador
     *
     * @param indexerIP que representa o ip do indexador
     */
    public void setIndexerIP(String indexerIP) {
        this.indexerIP = indexerIP;
    }

}
