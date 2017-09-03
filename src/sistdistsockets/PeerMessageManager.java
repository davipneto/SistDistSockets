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
public class PeerMessageManager extends Thread {
    
    private Peer peer;
    private String message;
    private int senderID;

    public PeerMessageManager(Peer peer, Message message) {
        this.peer = peer;
        this.message = message.getMessage();
        this.senderID = message.getSenderID();
    }
    
    @Override
    public void run() {
        if (message == null)
            message = "";
        if (message.startsWith("indexerHi")) {
            peer.setIndexerOn(true);
            if (peer.getIndexerPort() == -1) {
                int port = Integer.parseInt(message.substring(9));
                peer.setIndexerPort(port);
                System.out.println("Porta do indexador: " + port);
                peer.getPeersOnGroup().clear();
                //enviar dados necessários
            }
        } else
        if (message.startsWith("newIndexer")) {
            int port = Integer.parseInt(message.substring(10));
            peer.setIndexerPort(port);
            System.out.println("Porta do indexador: " + port);
            peer.getPeersOnGroup().clear();
            //enviar dados necessários
        } else
        if (message.equals("hi")) {
            if (!peer.getPeersOnGroup().contains(senderID)) {
                peer.putPeersOnGroup(senderID);
            }
        }
    }
}
