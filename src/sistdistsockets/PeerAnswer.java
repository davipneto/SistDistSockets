package sistdistsockets;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * A classe PeerAnswer encapsula os dados de chave pública, IP e porta de um
 * peer para a comunicação unicast.
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public class PeerAnswer implements Serializable {

    private PublicKey publicKey;
    private String ip;
    private int port;

    /**
     * Construtor da classe que seta os valores de chave pública, ip e porta de
     * um peer
     *
     * @param publicKey com a chave pública do peer
     * @param ip com o ip de comunicação unicast do peer
     * @param port com a porta de comunicação unicast do peer
     */
    public PeerAnswer(PublicKey publicKey, String ip, int port) {
        this.publicKey = publicKey;
        this.ip = ip;
        this.port = port;
    }

    /**
     * Retorna a chave pública do peer
     *
     * @return do tipo <i>PublicKey</i> com a chave pública do peer
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Seta a chave pública do peer
     *
     * @param publicKey com a chave pública do peer
     */
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Retorna o ip do peer
     *
     * @return do tipo <i>String</i> com o ip associado ao peer
     */
    public String getIp() {
        return ip;
    }

    /**
     * Seta o ip do peer
     *
     * @param ip com o ip do peer
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Retorna a porta do peer
     *
     * @return do tipo <i>int</i> com a porta associada ao peer
     */
    public int getPort() {
        return port;
    }

    /**
     * Seta a porta do peer
     *
     * @param port com a porta do peer
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Converte este objeto para uma String colocando as informações em
     * sequência para enviar para um socket.
     *
     * @return uma <i>String</i> com as informações deste objeto em sequência
     */
    @Override
    public String toString() {
        return port + " " + ip + " " + publicKey;
    }
}
