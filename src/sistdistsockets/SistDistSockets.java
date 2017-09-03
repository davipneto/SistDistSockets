/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davi
 */
public class SistDistSockets {
    
    static final String group = "226.2.2.5";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        Scanner s = new Scanner(System.in);
        String mess;
        int message;
        System.out.println("Digite o ID do peer: ");
        int id = s.nextInt();
        //s.nextLine();
        System.out.println("Digite a porta: ");
        int port = s.nextInt();
        s.nextLine();
        Peer p = new Peer(id, port, group);
        System.out.println("----------PEER " + p.getID() + "----------");
        PeerReceiveThread t = new PeerReceiveThread(p);
        t.start();
    }
    
}
