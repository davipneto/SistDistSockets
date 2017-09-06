/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.util.Scanner;

/**
 * A classe Message encapsula os dados das mensagens trocadas no grupo Multicast,
 * e guarda o iD do par que enviou a mensagem.
 * @author davi
 */
public class Message {
    
    private int senderID;
    private int length;
    private String message;

    /**
     * Cria uma Message com o id do rementente, o tamanho da mensagem, e a mensagem em si
     * especificados.
     * @param senderID um <i>int</i> representando o identificador do remetente
     * @param length um <i>int</i> contendo o tamanho da mensagem
     * @param message uma <i>String</i> com a mensagem
     */
    
    public Message(int senderID, int length, String message) {
        this.senderID = senderID;
        this.length = length;
        this.message = message;
    }
    
    /**
     * Cria uma Message a partir de uma String com todas as informações em sequência
     * recebida no buffer do datagrama de recebimento.
     * @param s a <i>String recebida no buffer</i>
     */
    public Message(String s) {
        Scanner sc = new Scanner(s);
        this.senderID = sc.nextInt();
        this.length = sc.nextInt();
        this.message = sc.next().substring(0, length);
    }
    
    /**
     * Converte este objeto para uma String colocando as informações em sequencia para enviar
     * para um socket.
     * @return uma <i>String</i> com as informações deste objeto em sequência
     */
    @Override
    public String toString() {
        return senderID + " " + length + " " + message;
    }
    
    /**
     * Retorna o iD do par que enviou a mensagem.
     * @return um <i>int</i> representando o identificador do par remetente
     */
    public int getSenderID() {
        return senderID;
    }

    /**
     * Retorna o tamanho da mensagem.
     * @return um <i>int</i> contendo o tamanho da mensagem
     */
    public int getLength() {
        return length;
    }

    /**
     * Retorna a mensagem.
     * @return uma <i>String</i> com a mensagem
     */
    public String getMessage() {
        return message;
    }
    
    
    
}
