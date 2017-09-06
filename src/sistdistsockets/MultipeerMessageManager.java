/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

/**
 * A classe MultipeerMessageManager é responsável por processar uma mensagem recebida para
 * um determinado peer.
 * @author davi
 */
public class MultipeerMessageManager extends Thread {
    
    private Peer peer; //o peer correspondente
    private String message; //a mensagem recebida
    private int senderID; // o id do peer que enviou a mensagem

    /**
     * Cria uma MultipeerMessageManager para o peer e uma message especificada
     * @param peer o par associado com esse objeto
     * @param message uma <i>Message</i> contendo os dados da mensagem recebida
     */
    public MultipeerMessageManager(Peer peer, Message message) {
        this.peer = peer;
        this.message = message.getMessage();
        this.senderID = message.getSenderID();
    }
    
    /**
     * Método herdado da classe Thread. Quando chamado o método <i>start</i>, ele é invocado
     * em uma nova thread, rodando em paralelo com os outros processos.<p>
     * Esse método analisa a mensagem recebida e coordenada as ações a serem executadas
     * pelo peer associado.</p>
     */
    @Override
    public void run() {
        //protecao para evitar nullPointerException
        if (message == null)
            message = "";
        //mensagem de envio do indexador para indicar que está vivo + a sua porta unicast
        if (message.startsWith("indexerHi")) {
            //seta a variavel do peer que indica que o indexador esta vivo
            peer.setIndexerOn(true);
            //caso o peer tenha acabado de entrar no grupo, salva a porta unicast do indexador, esvazia a lista
            //utilizada para eleicao e envia os dados necessarios em comunicao unicast para ele
            if (peer.getIndexerPort() == -1) {
                int port = Integer.parseInt(message.substring(9));
                peer.setIndexerPort(port);
                System.out.println("Porta do indexador: " + port);
                peer.getPeersOnGroup().clear();
                //enviar dados necessários
            }
        //mensagem indicando o novo indexador e suaa porta unicast
        } else
        if (message.startsWith("newIndexer")) {
            //como ha um novo indexador, o peer salva a porta unicast dele, esvazia a lista
            //utilizada para eleicao e envia os dados necessarios em comunicao unicast para ele
            int port = Integer.parseInt(message.substring(10));
            peer.setIndexerPort(port);
            System.out.println("Porta do indexador: " + port);
            peer.getPeersOnGroup().clear();
            //enviar dados necessários
        //mensagem para comunicacao entre os peers quando nao ha indexador, para realizar a eleicao do mesmo
        } else
        if (message.equals("hi")) {
            //adiciona o id do peer que enviou a mensagem na lista dos ids do grupo, para eleger o novo indexador
            if (!peer.getPeersOnGroup().contains(senderID)) {
                peer.putPeersOnGroup(senderID);
            }
        }
    }
}
