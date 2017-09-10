/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author davi
 */
public class UnicastMessageManager extends Thread {
    
    private Peer peer;
    private Message message;

    public UnicastMessageManager(Peer peer, Message message) {
        this.peer = peer;
        this.message = message;
    }
    
    @Override
    public void run() {
        
        if (message.getMessage().equals("myKey")) {
            PublicKeyMessage mess = (PublicKeyMessage) message;
            peer.setKeyForAPeer(message.getSenderID(), mess.getPublicKey());
            System.out.println("Recebi a chave publica do peer " + message.getSenderID());
        } else
        if (message.getMessage().equals("myProduct")) {
            ProductMessage mess = (ProductMessage) message;
            peer.setProductForAPeer(message.getSenderID(), mess.getProduct());
            System.out.println("Recebi um produto do peer " + message.getSenderID() + ": " + mess.getProduct());
        }
        
    }
    
}
