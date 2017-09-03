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
        System.out.println("Sender: " + senderID + " Message: " + message + " received by peer" + peer.getID());
        switch (message) {
            case "indexerHi":
                peer.setIndexerOn(true);
                break;
            default:
                break;
        }
    }
}
