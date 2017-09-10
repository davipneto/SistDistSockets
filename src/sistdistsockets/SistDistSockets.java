/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import frames.CompraEVenda;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.security.PublicKey;
import javax.swing.JOptionPane;

/**
 * Classe Principal do Projeto, onde se encontra o método main.
 * @author davi
 */
public class SistDistSockets {
    
    static final String group = "226.2.2.5";

    /**
     * Método Principal, onde é instanciado um par e suas Threads para receber as mensagens
     * via socket. O usuário pode solicitar uma compra e cadastrar produtos para venda.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        try {
            System.out.println("IP da maquina: " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
            Logger.getLogger(SistDistSockets.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*Scanner s = new Scanner(System.in);
        System.out.println("Digite o ID do peer: ");
        int id = s.nextInt();
        s.nextLine();
        System.out.println("id" + id);
        */
        int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o ID do peer: "));
        
        //Inicializa o peer com o id e porta fornecidos, e o grupo Multicast definido
        Peer p = new Peer(id, group);
        System.out.println("----------PEER " + p.getID() + "----------");
        //Inicializa a Thread que vai ficar recebendo as mensagens do grupo Multicast com o peer correspondente
        MultipeerReceiveThread t = new MultipeerReceiveThread(p);
        t.start();
        UnicastReceiveThread u = new UnicastReceiveThread(p);
        u.start();
        System.out.println("Porta: " + p.getPort()); 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CompraEVenda(p).setVisible(true);
            }
        });
    }
    
}
