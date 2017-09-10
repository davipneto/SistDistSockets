/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.security.PublicKey;

/**
 *
 * @author davi
 */
public class PeerAnswer {
    
    private PublicKey publicKey;
    private String ip;
    private int port;

    public PeerAnswer(PublicKey publicKey, String ip, int port) {
        this.publicKey = publicKey;
        this.ip = ip;
        this.port = port;
    }
    
}
