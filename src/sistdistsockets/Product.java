/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;
import java.text.*;

/**
 *
 * @author davi
 */
public class Product {
    
    private String descricao;
    private double preco;

    public Product(String descricao, double preco) {
        this.descricao = descricao;
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }
    
    @Override
    public String toString() {
        NumberFormat formatter = new DecimalFormat("#0.00");     
        return (descricao + formatter.format(preco));
    }
    
}
