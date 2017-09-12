package sistdistsockets;

/**
 * A classe ProductMessage é derivada da classe Message e encapusula um objeto
 * da classe Product com os dados para comunicação unicast de um determinado
 * peer.
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public class ProductMessage extends Message {

    private Product product;

    /**
     * Construtor da classe que seta os dados do peer e do produto associado a
     * ele
     *
     * @param senderID que armazena o id do peer que enviou a mensagem
     * @param message que armazena a mensagem recebida
     * @param product que armazena o produto associado ao peer
     */
    public ProductMessage(int senderID, String message, Product product) {
        super(senderID, message);
        this.product = product;
    }

    /**
     * Retorna o produto do peer
     *
     * @return do tipo <i>Product</i> que armazena o produto associado ao peer
     */
    public Product getProduct() {
        return product;
    }

}
