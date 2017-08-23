/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author geova
 */
public class InitialReceiveThread extends Thread {
    
    private String address;
    
    public InitialReceiveThread (String address) {
        this.address = address;
    }

    @Override
    public void run() {
        try {
            MulticastSocket s = new MulticastSocket(6789);
            InetAddress group = InetAddress.getByName(address);
            s.joinGroup(group);
            String message;
            DatagramPacket messageIn = null;
            for(int i=0;i<4;i++) {		// get messages from others in group
                byte[] buffer = new byte[1000];
                messageIn = new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                message = new String(messageIn.getData());
                System.out.println("Received:\t" + message);
            }
            s.close();
            this.interrupt();
        } catch (IOException ex) {
            Logger.getLogger(InitialReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
