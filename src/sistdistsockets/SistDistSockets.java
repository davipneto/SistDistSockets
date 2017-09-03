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
        Peer p = new Peer(s.nextInt(), 1235, group);
        System.out.println("----------PEER " + p.getID() + "----------");
        s.nextLine();
        InitialReceiveThread t = new InitialReceiveThread(p);
        t.start();
//        while(true) {
//            System.out.println("Digite a mensagem a ser enviada: ");
//            message = s.nextInt();
//            s.next();
//            if (message == -1) {
//                t.interrupt();
//                break;
//            }
//            switch (message) {
//                case 0: mess = "OI"; break;
//                case 1: mess = "VAMO"; break;
//                case 2: mess = "CABO"; break;
//                default: mess = "NAO"; break;
//            }
//            p.send(mess);
//        }
        p.send("OI");
        p.send("VAMO");
        p.send("CHEGA");
    }
    
}
