package sistdistsockets;


/**
 * A classe MultipeerMessageManager é responsável por processar uma mensagem
 * recebida para um determinado peer.
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 * @see Peer
 */
public class MultipeerMessageManager extends Thread {

    private Peer peer; //o peer correspondente
    private String message; //a mensagem recebida
    private int senderID; // o id do peer que enviou a mensagem

    /**
     * Cria uma MultipeerMessageManager para o peer e uma message especificada
     *
     * @param peer o par associado com esse objeto
     * @param message uma <i>Message</i> contendo os dados da mensagem recebida
     */
    public MultipeerMessageManager(Peer peer, Message message) {
        this.peer = peer;
        this.message = message.getMessage();
        this.senderID = message.getSenderID();
    }

    /**
     * Método herdado da classe Thread. Quando chamado o método <i>start</i>,
     * ele é invocado em uma nova thread, rodando em paralelo com os outros
     * processos.<p>
     * Esse método analisa a mensagem recebida e coordenada as ações a serem
     * executadas pelo peer associado.</p>
     */
    @Override
    public void run() {
        //protecao para evitar nullPointerException
        if (message == null) {
            message = "";
        }
        //mensagem de envio do indexador para indicar que está vivo + a sua porta unicast
        if (message.startsWith("indexerHi")) {
            //seta a variavel do peer que indica que o indexador esta vivo
            peer.setIndexerOn(true);
            //caso o peer tenha acabado de entrar no grupo, salva a porta e ip unicast do indexador, esvazia a lista
            //utilizada para eleicao e envia os dados necessarios em comunicao unicast para ele
            if (peer.getIndexerPort() == -1) {
                int portsDigit = Integer.valueOf(message.substring(9,10));
                int port = Integer.parseInt(message.substring(10,10+portsDigit));
                peer.setIndexerPort(port);
                String ip = message.substring(10+portsDigit);
                peer.setIndexerIP(ip);
                System.out.println("ip do indexador: " + ip);
                peer.getPeersOnGroup().clear();
                //enviar chave publica
                PeerAnswer pa = new PeerAnswer(peer.getPublicKey(), peer.getIp(), peer.getPort());
                PublicKeyMessage mess = new PublicKeyMessage(peer.getID(), "myKey", pa);
                peer.send(mess, port);
                //enviar informacoes de venda
                for (Product product : peer.getProdutos()) {
                    ProductMessage pmess = new ProductMessage(peer.getID(), "myProduct", product);
                    peer.send(pmess, port);
                }
            }
            //mensagem indicando o novo indexador, sua porta e o ip unicast
        } else if (message.startsWith("newIndexer")) {
            //como ha um novo indexador, o peer salva a porta e ip unicast dele, esvazia a lista
            //utilizada para eleicao e envia os dados necessarios em comunicao unicast para ele
            int portsDigit = Integer.valueOf(message.substring(10,11));
            int port = Integer.parseInt(message.substring(11, 11 + portsDigit));
            peer.setIndexerPort(port);
            String ip = message.substring(11 + portsDigit);
            peer.setIndexerIP(ip);
            System.out.println("ip do indexador: " + ip);
            peer.getPeersOnGroup().clear();
            //enviar chave publica
            PeerAnswer pa = new PeerAnswer(peer.getPublicKey(), peer.getIp(), peer.getPort());
            PublicKeyMessage mess = new PublicKeyMessage(peer.getID(), "myKey", pa);
            peer.send(mess, port);
            //enviar informacoes de venda
            for (Product product : peer.getProdutos()) {
                ProductMessage pmess = new ProductMessage(peer.getID(), "myProduct", product);
                peer.send(pmess, port);
            }
            //mensagem para comunicacao entre os peers quando nao ha indexador, para realizar a eleicao do mesmo
        } else if (message.equals("hi")) {
            //adiciona o id do peer que enviou a mensagem na lista dos ids do grupo, para eleger o novo indexador
            if (!peer.getPeersOnGroup().contains(senderID)) {
                peer.putPeersOnGroup(senderID);
            }
        }
    }
}
