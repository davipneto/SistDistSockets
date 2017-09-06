/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 * A classe UnicastReceiveThread possui um ServerSocket responsável por ficar ouvindo em
 * uma porta, e processa as mensagens recebidas para um determinado peer invocando
 * uma instância da classe UnicastMessageManager.
 * @author davi
 */
public class UnicastReceiveThread extends Thread {
    
    private Peer peer;
    private ServerSocket listenSocket;
    
    /**
     * Cria uma UnicastReceiveThread para o peer especificado, alocando uma porta disponível
     * para inicializar um objeto ServerSocket.
     * @param peer o peer a ser associado com esse objeto
     */
    public UnicastReceiveThread(Peer peer) {
        this.peer = peer;
        try {
            listenSocket = new ServerSocket(0);
            peer.setPort(listenSocket.getLocalPort());
        } catch (IOException ex) {
            Logger.getLogger(UnicastReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Método herdado da classe Thread. Quando chamado o método <i>start</i>, ele é invocado
     * em uma nova thread, rodando em paralelo com os outros processos.<p>
     * Ele invoca o método <i>accept</i> do objeto da classe ServerSocket, que bloqueia a
     * Thread até que uma conexão seja feita, e então inicializa um objeto da classe
     * UnicastMessageManager para processar a mensagem recebida.</p>
     */
    @Override
    public void run() {
        try {
            DataInputStream in;
            DataOutputStream out;
            Socket clientSocket;
            String data;
            while (true) {
                clientSocket = listenSocket.accept();
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());
                data = in.readUTF();
            }
        } catch (IOException ex) {
            Logger.getLogger(UnicastReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
