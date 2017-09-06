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
 * A classe MultipeerReceiveThread é responsável por ficar constantemente recebendo
 * as mensagens que chegam para o grupo Multicast, e processá-las para um determinado
 * peer invocando uma instância da classe MultipeerMassageManager.
 * @author geova
 */
public class MultipeerReceiveThread extends Thread {
    
    private Peer peer; //o peer que essa thread representa
    
    /**
     * Cria uma MultipeerReceiveThread para o peer especificado.
     * @param peer o peer a ser associado com esse objeto
     */
    public MultipeerReceiveThread (Peer peer) {
        this.peer = peer;
    }

    /**
     * Método herdado da classe Thread. Quando chamado o método <i>start</i>, ele é invocado
     * em uma nova thread, rodando em paralelo com os outros processos.<p>
     * Esse método instancia um objeto da classe MulticastSocket, recebe as mensagens enviadas
     * ao grupo Multicast e inicializa um objeto da classe MultipeerMessageManager para
     * processar a mensagem recebida.</p>
     */
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
                MultipeerMessageManager pmm = new MultipeerMessageManager(peer, m);
                pmm.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(MultipeerReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
