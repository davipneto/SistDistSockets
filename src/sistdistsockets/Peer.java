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
    private ArrayList<Integer> peersOnGroup;
    
    public Peer(int id, int port, String group) {
        this.iD = id;
        this.port = port;
        this.group = group;
        peersOnGroup = new ArrayList();
        indexerPort = -1;
        indexerOn = false;
        VerifyAliveIndexerTask vai = new VerifyAliveIndexerTask(this);
        Timer verifyAliveIndexerTimer = new Timer();
        verifyAliveIndexerTimer.scheduleAtFixedRate(vai, 5000, 5000);
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
        System.out.println("Eu, peer" + iD + "vou ser o indexador!!");
        send("newIndexer" + port);
        TimerTask informAliveIndexerTimerTask = new TimerTask() {
            @Override
            public void run() {
                send("indexerHi" + port);
            }
        };
        Timer informAliveIndexerTimer = new Timer();
        informAliveIndexerTimer.schedule(informAliveIndexerTimerTask, 0, 5000);
    }
    
    public void putPeersOnGroup(int iD) {
        peersOnGroup.add(iD);
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

    public int getIndexerPort() {
        return indexerPort;
    }

    public ArrayList<Integer> getPeersOnGroup() {
        return peersOnGroup;
    }

    public boolean isIndexerOn() {
        return indexerOn;
    }

    public void setIndexerOn(boolean indexerOn) {
        this.indexerOn = indexerOn;
    }

    public void setIndexerPort(int indexerPort) {
        this.indexerPort = indexerPort;
    }
    
    
    
    
}
