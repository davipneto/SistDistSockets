package sistdistsockets;

import frames.CompraEVenda;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Classe Principal do Projeto, onde se encontra o método main.
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public class SistDistSockets {

    //define um ip para o grupo multicast
    static final String group = "226.2.2.5";

    /**
     * Método Principal, onde é instanciado um par e suas Threads para receber
     * as mensagens via socket. É instanciado também um formulário que permite
     * ao usuário solicitar uma compra e/ou cadastrar produtos para venda.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.setProperty("java.net.preferIPv4Stack", "true");
        try {
            System.out.println("IP da maquina: " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
            Logger.getLogger(SistDistSockets.class.getName()).log(Level.SEVERE, null, ex);
        }
        //solicita ao usuário um id para o peer
        int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o ID do peer: "));

        //Inicializa o peer com o id e porta fornecidos, e o grupo Multicast definido
        Peer p = new Peer(id, group);
        System.out.println("----------PEER " + p.getID() + "----------");
        //Inicializa a Thread que vai ficar recebendo as mensagens do grupo Multicast com o peer correspondente
        MultipeerReceiveThread t = new MultipeerReceiveThread(p);
        t.start();
        //Inicializa a Thread que vai ficar recebendo as mensagens tcp em comunicação unicast
        UnicastReceiveThread u = new UnicastReceiveThread(p);
        u.start();
        System.out.println("Porta: " + p.getPort());

        //inicializa formulário para compra e venda
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CompraEVenda(p).setVisible(true);
            }
        });
    }

}
