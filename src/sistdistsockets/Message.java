/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.util.Scanner;

/**
 *
 * @author davi
 */
public class Message {
    
    private int senderID;
    private int length;
    private String message;

    //inicializa um objeto a partir do id do rementente, e a mensagem em si informados de forma separada
    public Message(int senderID, int length, String message) {
        this.senderID = senderID;
        this.length = length;
        this.message = message;
    }
    
    //a partir de uma String com todas as informacoes em sequencia, recebidos no buffer do datagrama de recebimento
    //separa as informacoes e inicializa o objeto
    public Message(String s) {
        Scanner sc = new Scanner(s);
        this.senderID = sc.nextInt();
        this.length = sc.nextInt();
        this.message = sc.next().substring(0, length);
    }
    
    //gera a string a ser enviada pelo buffer com as informacoes en sequencia
    @Override
    public String toString() {
        return senderID + " " + length + " " + message;
    }

    public int getSenderID() {
        return senderID;
    }

    public int getLength() {
        return length;
    }

    public String getMessage() {
        return message;
    }
    
    
    
}
