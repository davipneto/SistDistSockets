/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author geova
 */
public class MultipeerReceiveThread extends Thread {
    
    private Peer peer; //o peer que essa thread representa
    
    public MultipeerReceiveThread (Peer peer) {
        this.peer = peer;
    }

    @Override
    public void run() {
        try {
            //inicializa o socket Multicast na porta correta
            MulticastSocket s = new MulticastSocket(6789);
            //o socket entra no grupo correspondente
            InetAddress group = InetAddress.getByName(peer.getGroup());
            s.joinGroup(group);
            String message;
            DatagramPacket messageIn = null;
            while (true) {
                //prepara o buffer e o datagrama para receber mensagens do grupo
                byte[] buffer = new byte[100];
                messageIn = new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                message = new String(messageIn.getData());
                //inicializa um objeto da classe Message, para separar as informacoes recebidas (o iD de quem enviou,
                //o tamanho da mensagem e a mensagem em si)
                Message m = new Message(message);
                //inicializa e executa a thread responsável por processar a mensagem recebida, e fica em um loop
                //para receber mais mensagens do grupo multicast
                PeerMessageManager pmm = new PeerMessageManager(peer, m);
                pmm.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(MultipeerReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
