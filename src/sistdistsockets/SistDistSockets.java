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

        Peer p1 = new Peer(1, 1235, group);
        InitialReceiveThread t1 = new InitialReceiveThread(p1);
        t1.start();
        Peer p2 = new Peer(2, 1236, group);
        InitialReceiveThread t2 = new InitialReceiveThread(p2);
        t2.start();
        p1.send("OLAPESSOAL");
        p2.send("OIEEE");
    }
    
}
