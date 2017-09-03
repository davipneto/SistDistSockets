/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author geova
 */
public class InitialReceiveThread extends Thread {
    
    private Peer peer;
    
    public InitialReceiveThread (Peer peer) {
        this.peer = peer;
    }

    @Override
    public void run() {
        try {
            MulticastSocket s = new MulticastSocket(6789);
            InetAddress group = InetAddress.getByName(peer.getGroup());
            s.joinGroup(group);
            String message;
            int number;
            DatagramPacket messageIn = null;
            do{		// get messages from others in group
                byte[] buffer = new byte[100];
                messageIn = new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                message = new String(messageIn.getData());
                Message m = new Message(message);
                System.out.println("Sender: " + m.getSenderID() + " Message: " + m.getMessage() + " received by peer" + peer.getID());
                TimeUnit.SECONDS.sleep(1);
            }while(!message.trim().equals("sair"));
            s.close();
            this.interrupt();
        } catch (IOException ex) {
            Logger.getLogger(InitialReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(InitialReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
