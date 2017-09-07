/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;
import java.security.*;

/**
 *
 * @author davi
 */
public class PublicKeyMessage extends Message {
    
    private PublicKey publicKey;
    
    public PublicKeyMessage(int senderID, String message, PublicKey publicKey) {
        super(senderID, message);
        this.publicKey = publicKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
    
}
