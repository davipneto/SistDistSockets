package sistdistsockets;

/**
 * A classe PublickKeyMessage é derivada da classe Message e encapusula um
 * objeto da classe PeerAnswer com os dados para comunicação unicast de um
 * determinado peer.
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public class PublicKeyMessage extends Message {

    private PeerAnswer publicKey;

    /**
     * Construtor da classe que seta os dados do peer e da PeerAnswer associado
     * a ele
     *
     * @param senderID que armazena o id do peer que enviou a mensagem
     * @param message que armazena a mensagem recebida
     * @param publicKey que armazena a chave pública do peer
     */
    public PublicKeyMessage(int senderID, String message, PeerAnswer publicKey) {
        super(senderID, message);
        this.publicKey = publicKey;
    }

    /**
     * Retorna o valor da chave pública do peer
     *
     * @return do tipo <i>PeerAnswer</i> com os dados de chave pública, ip e
     * porta do peer
     */
    public PeerAnswer getPublicKey() {
        return publicKey;
    }

}
