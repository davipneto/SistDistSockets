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
import java.io.*;

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
        Scanner s = new Scanner(System.in);
        String mess;
        int message;
        System.out.println("Digite o ID do peer: ");
        int id = s.nextInt();
        s.nextLine();
        //Inicializa o peer com o id e porta fornecidos, e o grupo Multicast definido
        Peer p = new Peer(id, group);
        System.out.println("----------PEER " + p.getID() + "----------");
        //Inicializa a Thread que vai ficar recebendo as mensagens do grupo Multicast com o peer correspondente
        MultipeerReceiveThread t = new MultipeerReceiveThread(p);
        t.start();
        UnicastReceiveThread u = new UnicastReceiveThread(p);
        u.start();
        try {
            Socket sock = new Socket("localhost", p.getPort());
            DataInputStream in = new DataInputStream(sock.getInputStream());
            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            out.writeUTF("SERA QUE DEU CERTO ESSA MERDA??");
        } catch (IOException ex) {
            Logger.getLogger(SistDistSockets.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
