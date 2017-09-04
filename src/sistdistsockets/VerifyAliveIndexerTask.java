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

    private Peer peer; //o peer que acionou essa TimerTask

    public VerifyAliveIndexerTask(Peer peer) {
        this.peer = peer;
    }
    
    @Override
    public void run() {
        //Entra nessa condiçao caso o indexador esteja vivo
        if (peer.isIndexerOn()) {
            System.out.println("Indexador ta vivo");
            //seta a variavel "indexerOn" do peer como falsa, pois na próxima verificacao essa variável somente sera 
            //verdadeira caso o peer tenha recebido novamente a mensagem do indexador indicando que está vivo
            peer.setIndexerOn(false);
        //caso o indexador não esteja vivo, entra nesse else
        } else {
            System.out.println("Indexador esta morto");
            //envia uma mensagem para o grupo Multicast indicando sua presenca
            peer.send("hi");
            //caso o número de peers no grupo seja maior ou igual a 4, sera elegido um novo indexador
            if (peer.getPeersOnGroup().size()>=4) {
                System.out.println("Vamos eleger");
                boolean willBeIndexer = true;
                //caso o iD do peer for o menor de todos, a variavel "willBeIndexer" permanece verdadeira, e o peer
                //é elegido como o novo indexador
                for (int peerID: peer.getPeersOnGroup()) {
                    if(peer.getID() > peerID) {
                        willBeIndexer = false;
                        break;
                    }
                }
                if (willBeIndexer)
                    peer.beTheIndexer();
            }
        }
    }
    
}
