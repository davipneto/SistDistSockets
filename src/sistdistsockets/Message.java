package sistdistsockets;

import java.util.Scanner;
import java.io.*;

/**
 * A classe Message encapsula os dados das mensagens trocadas no grupo
 * Multicast, e guarda o iD do par que enviou a mensagem.
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public class Message implements Serializable {

    private int senderID;
    private String message;

    /**
     * Cria uma Message com o id do rementente, o tamanho da mensagem, e a
     * mensagem em si especificados.
     *
     * @param senderID um <i>int</i> representando o identificador do remetente
     * @param length um <i>int</i> contendo o tamanho da mensagem
     * @param message uma <i>String</i> com a mensagem
     */
    public Message(int senderID, String message) {
        this.senderID = senderID;
        this.message = message;
    }

    /**
     * Cria uma Message a partir de uma String com todas as informações em
     * sequência recebida no buffer do datagrama de recebimento.
     *
     * @param s a <i>String recebida no buffer</i>
     */
    public Message(String s) {
        Scanner sc = new Scanner(s);
        this.senderID = sc.nextInt();
        int length = sc.nextInt();
        this.message = sc.next().substring(0, length);
    }

    /**
     * Converte este objeto para uma String colocando as informações em
     * sequência para enviar para um socket.
     *
     * @return uma <i>String</i> com as informações deste objeto em sequência
     */
    @Override
    public String toString() {
        return senderID + " " + message.length() + " " + message;
    }

    /**
     * Retorna o iD do par que enviou a mensagem.
     *
     * @return um <i>int</i> representando o identificador do par remetente
     */
    public int getSenderID() {
        return senderID;
    }

    /**
     * Retorna a mensagem.
     *
     * @return uma <i>String</i> com a mensagem
     */
    public String getMessage() {
        return message;
    }
}
