/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.util.TimerTask;

/**
 *
 * @author davi
 */
public class VerifyAliveIndexerTask extends TimerTask {

    private Peer peer;
    private boolean isTimeToVerify;

    public VerifyAliveIndexerTask(Peer peer) {
        this.peer = peer;
    }
    
    @Override
    public void run() {
        if (peer.isIndexerOn()) {
            System.out.println("Indexador ta vivo");
            peer.setIndexerOn(false);
            isTimeToVerify = false;
        } else if (isTimeToVerify) {
            System.out.println("Vamos tentar eleger");
            peer.send("hi");
            if (peer.getPeersOnGroup().size()>=4) {
                System.out.println("Vamos eleger");
                boolean willBeIndexer = true;
                for (int peerID: peer.getPeersOnGroup()) {
                    if(peer.getID() > peerID) {
                        willBeIndexer = false;
                        break;
                    }
                }
                if (willBeIndexer)
                    peer.beTheIndexer();
            }
        } else {
                System.out.println("Indexador caiu");
                isTimeToVerify = true;
                peer.send("hi");
        }
    }
    
}
