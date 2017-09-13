package sistdistsockets;

import java.io.Serializable;
import java.text.*;

/**
 * A Classe Product representa um produto que está disponível para compra e
 * venda.
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public class Product implements Serializable {

    private String descricao;
    private double preco;

    /**
     * Construtor da classe que seta a descrição e preço do produto.
     *
     * @param descricao com a descrição do produto vendido
     * @param preco com o valor do produto
     */
    public Product(String descricao, double preco) {
        this.descricao = descricao;
        this.preco = preco;
    }

    /**
     * Retorna a descrição do produto
     *
     * @return do tipo <i>String</i> com a descrição do produto armazenado
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna o preco do produto
     *
     * @return do tipo <i>double</i> com o valor do preço do produto armazenado
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Retorna o preço do produto com o formato descimal com duas casas depois
     * da vírgula. Subsitui ',' por '.' para exibir na tabela.
     *
     * @return do tipo <i>String</i> com o valor do preço do produto armazenado
     * formatado
     */
    public String getPreco2() {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(preco).replace(',', '.');
    }

    /**
     * Converte este objeto para uma String colocando as informações em
     * sequência para enviar para um socket.
     *
     * @return uma <i>String</i> com as informações deste objeto em sequência
     */
    @Override
    public String toString() {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return (descricao + " " + formatter.format(preco));
    }

}
