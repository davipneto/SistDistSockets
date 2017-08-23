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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davi
 */

public class Peer extends Thread {
    
    private int id;
    private MulticastSocket ms;
    private int port;
    private ArrayList<Peer> list;
    
    public Peer(int id, int port, String group) {
        this.id = id;
        this.port = port;
        try {
            ms = new MulticastSocket(port);
            ms.joinGroup(InetAddress.getByName(group));
        } catch (IOException ex) {
            System.out.println("Erro na criação do peer");
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ms!= null)
                ms.close();
        }
    }
    
    @Override
    public void run() {
        try {
            DatagramPacket hello = new DatagramPacket("Ola".getBytes(), 3, ms.getInetAddress(), port);
            ms.send(hello);
            byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                ms.receive(messageIn);
                String message = new String(messageIn.getData());
                System.out.println("Received:\t" + message);
        } catch (IOException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
