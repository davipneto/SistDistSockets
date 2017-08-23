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
public class SistDistSockets {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Peer p = new Peer(1, 3957, "226.2.2.3");
        p.start();
    }
    
}
