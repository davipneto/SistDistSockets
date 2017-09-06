/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davi
 */
public class UnicastReceiveThread extends Thread {
    
    private Peer peer;
    
    public UnicastReceiveThread(Peer peer) {
        this.peer = peer;
    }
    
    @Override
    public void run() {
        try {
            ServerSocket listenSocket = new ServerSocket(peer.getPort());
            DataInputStream in;
            DataOutputStream out;
            Socket clientSocket;
            String data;
            while (true) {
                clientSocket = listenSocket.accept();
                in = new DataInputStream(clientSocket.getInputStream());
                out =new DataOutputStream(clientSocket.getOutputStream());
                data = in.readUTF();
            }
        } catch (IOException ex) {
            Logger.getLogger(UnicastReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
