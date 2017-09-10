/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;

/**
 *
 * @author davi
 */
public class ProductMessage extends Message {
    
    private Product product;

    public ProductMessage(int senderID, String message, Product product) {
        super(senderID, message);
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
    
    
}
