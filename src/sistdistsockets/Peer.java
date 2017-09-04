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
    //variável setada em true quando o peer recebe a mensagem indicando que o indexador está vivo
    private boolean indexerOn;
    private int indexerPort;
    /*
    lista dos peers ativos no grupo, utilizada quando o indexador cai, para que o peer compare seu iD com o iD
    dos outros peers, e se o menor iD for o dele, invocara o metodo para ser elegido como indexador
    */
    private ArrayList<Integer> peersOnGroup;
    
    public Peer(int id, int port, String group) {
        this.iD = id;
        this.port = port;
        this.group = group;
        peersOnGroup = new ArrayList();
        //inicialmente, o peer nao conhece seu indexador
        indexerPort = -1;
        indexerOn = false;
        //Instancia a TimerTask que vai ficar verificando de 5 em 5 segundos se o indexador esta vivo
        VerifyAliveIndexerTask vai = new VerifyAliveIndexerTask(this);
        Timer verifyAliveIndexerTimer = new Timer();
        verifyAliveIndexerTimer.scheduleAtFixedRate(vai, 5000, 5000);
    }
    
    //metodo para enviar uma mensagem para o grupo Multicast
    public void send(String message) {
        MulticastSocket s = null;
        try {
            //inicializa uma instância da classe MulticastSocket na porta do grupo
            s = new MulticastSocket(6789);
            //insere a instância no grupo
            InetAddress inetGroup = InetAddress.getByName(group);
            s.joinGroup(inetGroup);
            //envia a mensagem e deixa o grupo
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
            //fecha o socket caso não esteja nulo
            if (s != null) {
                s.close();
            }
        }
    }
    
    //define o peer como indexador
    public void beTheIndexer() {
        System.out.println("Eu, peer " + iD + " vou ser o indexador!!");
        //envia a mensagem de novo indexador para o grupo Multicast, para os outros peers informarem seus dados
        send("newIndexer" + port);
        //ativa a TimerTask q vai enviar de 5 em 5s uma mensagem para os processos saberem que o indexador está vivo
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
