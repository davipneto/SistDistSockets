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
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static sistdistsockets.SistDistSockets.group;

/**
 *
 * @author davi
 */

public class Peer extends Thread {
    
    private int iD;
    private int port;
    private String group;
    //private ArrayList<Peer> list;
    
    public Peer(int id, int port, String group) {
        this.iD = id;
        this.port = port;
        this.group = group;
    }
    
    @Override
    public void run() {
        
    }
    
    public void send(String message) {
        MulticastSocket s = null;
        try {
            InetAddress inetGroup = InetAddress.getByName(group);
            s = new MulticastSocket(6789);
            s.joinGroup(inetGroup);
            Message m = new Message(iD, message.length(), message);
            message = m.toString();
            System.out.println("mensagem codificada: " + message);
            DatagramPacket messageOut = new DatagramPacket(message.getBytes(), message.getBytes().length, inetGroup, 6789);
            s.send(messageOut);
            s.leaveGroup(inetGroup);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    public int getID() {
        return iD;
    }

    public int getPort() {
        return port;
    }

    public String getGroup() {
        return group;
    }
    
    
}
