/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

import java.io.Serializable;
import java.security.PublicKey;

/**
 *
 * @author davi
 */
public class PeerAnswer implements Serializable{
    
    private PublicKey publicKey;
    private String ip;
    private int port;

    public PeerAnswer(PublicKey publicKey, String ip, int port) {
        this.publicKey = publicKey;
        this.ip = ip;
        this.port = port;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    @Override
    public String toString(){
        return port + " " + ip + " "  + publicKey;
    }
}
