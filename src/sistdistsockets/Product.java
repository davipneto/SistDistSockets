/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistdistsockets;
import java.io.Serializable;
import java.text.*;

/**
 *
 * @author davi
 */
public class Product implements Serializable {
    
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
    
    public String getPreco2(){
        NumberFormat formatter = new DecimalFormat("#0.00");     
        return formatter.format(preco).replace(',', '.');
    }
    
    @Override
    public String toString() {
        NumberFormat formatter = new DecimalFormat("#0.00");     
        return (descricao + formatter.format(preco));
    }
    
}
