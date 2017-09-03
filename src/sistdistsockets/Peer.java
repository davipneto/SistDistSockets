/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author davi
 */

public class Peer {
    
    private int iD;
    private int port;
    private String group;
    private boolean indexerOn;
    private int indexerPort;
    //private ArrayList<Peer> list;
    
    public Peer(int id, int port, String group) {
        this.iD = id;
        this.port = port;
        this.group = group;
        indexerOn = false;
        TimerTask verifyAliveIndexerTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (indexerOn) {
                    System.out.println("Indexador ta vivo, quem fala eh o peer " + iD);
                    indexerOn = false;
                } else {
                    System.out.println("indexador caiu, quem fala eh o peer " + iD);
                }
            }
        };
        Timer verifyAliveIndexerTimer = new Timer();
        verifyAliveIndexerTimer.scheduleAtFixedRate(verifyAliveIndexerTimerTask, 5000, 5000);
    }
    
    public void send(String message) {
        MulticastSocket s = null;
        try {
            InetAddress inetGroup = InetAddress.getByName(group);
            s = new MulticastSocket(6789);
            s.joinGroup(inetGroup);
            Message m = new Message(iD, message.length(), message);
            message = m.toString();
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
    
    public void beTheIndexer() {
        TimerTask informAliveIndexerTimerTask = new TimerTask() {
            @Override
            public void run() {
                send("indexerHi");
            }
        };
        Timer informAliveIndexerTimer = new Timer();
        informAliveIndexerTimer.schedule(informAliveIndexerTimerTask, 0, 5000);
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

    public boolean isIndexerOn() {
        return indexerOn;
    }

    public void setIndexerOn(boolean indexerOn) {
        this.indexerOn = indexerOn;
    }
    
    
    
}
