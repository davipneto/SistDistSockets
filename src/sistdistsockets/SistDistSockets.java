/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.net.DatagramPacket;

/**
 *
 * @author davi
 */
public class SistDistSockets {
    
    static final String group = "226.2.2.3";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Peer p1 = new Peer(1, 3957, group);
        Peer p2 = new Peer(2, 3958, group);
        Peer p3 = new Peer(3, 3959, group);
        Peer p4 = new Peer(4, 3956, group);
        InitialReceiveThread irt = new InitialReceiveThread(group);
        p1.start();
        p2.start();
        p3.start();
        p4.start();
    }
    
}
