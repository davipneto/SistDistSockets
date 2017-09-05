/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

/**
 *
 * @author davi
 */
public class MultipeerMessageManager extends Thread {
    
    private Peer peer; //o peer correspondente
    private String message; //a mensagem recebida
    private int senderID; // o id do peer que enviou a mensagem

    public MultipeerMessageManager(Peer peer, Message message) {
        this.peer = peer;
        this.message = message.getMessage();
        this.senderID = message.getSenderID();
    }
    
    //processa a mensagem recebida
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
