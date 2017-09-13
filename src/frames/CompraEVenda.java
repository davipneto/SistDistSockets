package frames;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.DatatypeConverter;
import sistdistsockets.*;

/**
 * A classe CompraEVenda implementa um JFrame que exibe as opções de compra e
 * venda de produtos na aplicação de forma visual.
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public class CompraEVenda extends javax.swing.JFrame {

    /**
     * Creates new form CompraEVenda
     */
    boolean venda = false;
    boolean compra = false;
    private Peer peer;

    /**
     * Construtor da classe que inicializa componentes e associa ao JFrame um
     * peer recebido por parâmetro.
     *
     * @param peer associado ao formulário
     */
    public CompraEVenda(Peer peer) {
        initComponents();
        jInternalFrame1.setVisible(false);
        jInternalFrame2.setVisible(false);
        jInternalFrame3.setVisible(false);
        jInternalFrame4.setVisible(true);
        this.peer = peer;
        //atualiza a tabela de produtos a vender caso já exista um produto cadastrado
        setUpTable();
        //troca o nome da janela para o nome específico do id
        this.setTitle("Peer " + peer.getID());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        BVenda = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        DescVenda = new javax.swing.JTextField();
        PrecoVenda = new javax.swing.JTextField();
        jInternalFrame2 = new javax.swing.JInternalFrame();
        jLabel5 = new javax.swing.JLabel();
        DescCompra = new javax.swing.JTextField();
        BPesquisa = new javax.swing.JButton();
        jInternalFrame4 = new javax.swing.JInternalFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jInternalFrame3 = new javax.swing.JInternalFrame();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        BComprar1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Compra e Venda");

        jInternalFrame1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jInternalFrame1.setTitle("Novos Produtos");
        jInternalFrame1.setVisible(true);

        BVenda.setText("Vender");
        BVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BVendaActionPerformed(evt);
            }
        });

        jLabel1.setText("Novo Produto");

        jLabel2.setText("Descrição: ");

        jLabel3.setText("Preço: ");

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(DescVenda)
                            .addComponent(PrecoVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(BVenda)))
                .addContainerGap())
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(DescVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(PrecoVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BVenda)
                .addGap(0, 33, Short.MAX_VALUE))
        );

        jInternalFrame2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jInternalFrame2.setTitle("Compra");
        jInternalFrame2.setVisible(true);

        jLabel5.setText("Descrição: ");

        BPesquisa.setText("Pesquisar");
        BPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BPesquisaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jInternalFrame2Layout = new javax.swing.GroupLayout(jInternalFrame2.getContentPane());
        jInternalFrame2.getContentPane().setLayout(jInternalFrame2Layout);
        jInternalFrame2Layout.setHorizontalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(DescCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jInternalFrame2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(BPesquisa)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jInternalFrame2Layout.setVerticalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(DescCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(BPesquisa)
                .addGap(0, 47, Short.MAX_VALUE))
        );

        jInternalFrame4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jInternalFrame4.setResizable(true);
        jInternalFrame4.setTitle("Produtos à Venda");
        jInternalFrame4.setVisible(true);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descrição", "Preço"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jInternalFrame4Layout = new javax.swing.GroupLayout(jInternalFrame4.getContentPane());
        jInternalFrame4.getContentPane().setLayout(jInternalFrame4Layout);
        jInternalFrame4Layout.setHorizontalGroup(
            jInternalFrame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jInternalFrame4Layout.setVerticalGroup(
            jInternalFrame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 76, Short.MAX_VALUE))
        );

        jInternalFrame3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jInternalFrame3.setTitle("Pesquisa");
        jInternalFrame3.setVisible(true);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Descrição", "Preço", "ID Vendedor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        BComprar1.setText("Comprar");
        BComprar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BComprar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jInternalFrame3Layout = new javax.swing.GroupLayout(jInternalFrame3.getContentPane());
        jInternalFrame3.getContentPane().setLayout(jInternalFrame3Layout);
        jInternalFrame3Layout.setHorizontalGroup(
            jInternalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame3Layout.createSequentialGroup()
                .addGroup(jInternalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jInternalFrame3Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jInternalFrame3Layout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(BComprar1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jInternalFrame3Layout.setVerticalGroup(
            jInternalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(BComprar1)
                .addGap(25, 25, 25))
        );

        jMenu1.setText("Novos Produtos");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Compra");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Sair");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);
        jMenuBar1.getAccessibleContext().setAccessibleName("Novos Produtos");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jInternalFrame2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jInternalFrame4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jInternalFrame3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jInternalFrame4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jInternalFrame1)
                    .addComponent(jInternalFrame2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jInternalFrame3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Evento associado ao menu corresponde ao cadastro de novos produtos para a
     * venda. Quando acionado exibe o formulário interno que possui as caixas de
     * texto relacionadas com o cadastro de um novo produto, e desabilita o
     * formulário interno responsável pela compra.
     *
     * @param evt
     */
    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        DescCompra.setText("");
        DescVenda.setText("");
        PrecoVenda.setText("");
        //verifica se o formulário não está sendo exibido
        if (venda == false) {
            venda = true;
            jInternalFrame1.setVisible(true);
            //desabilita o formulário de pesquisa para compra
            compra = false;
            jInternalFrame2.setVisible(false);
            jInternalFrame3.setVisible(false);
        }
    }//GEN-LAST:event_jMenu1MouseClicked

    /**
     * Evento associado ao menu corresponde à pesquisa de produtos para compra.
     * Quando acionado exibe o formulário interno que possui as caixas de texto
     * relacionadas com busca de um produto para compra, e desabilita o
     * formulário interno responsável pelo cadastro de um novo produto para
     * venda.
     *
     * @param evt
     */
    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked
        DescCompra.setText("");
        DescVenda.setText("");
        PrecoVenda.setText("");
        //verifica se o formulario de compra não está sendo exibido
        if (compra == false) {
            compra = true;
            jInternalFrame2.setVisible(true);
            //desabilita o formulário de venda
            venda = false;
            jInternalFrame1.setVisible(false);
        }


    }//GEN-LAST:event_jMenu2MouseClicked

    /**
     * Evento associado ao botão que cadastra um novo produto para venda.
     *
     * @param evt
     */
    private void BVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BVendaActionPerformed
        //armazena os valores escritos nas caixas de texto
        String descricao = DescVenda.getText();
        double preco = Double.parseDouble(PrecoVenda.getText().replace(',', '.'));
        //cria um novo produto com os dados fornecidos pelo usuário
        Product prod = new Product(descricao, preco);
        //atualiza o set de produtos do peer
        peer.setProduct(prod);
        //enviar para o indexador o novo produto (se já houver um indexador)
        if (peer.getIndexerPort() != -1) {
            ProductMessage pm = new ProductMessage(peer.getID(), "myProduct", prod);
            peer.send(pm, peer.getIndexerPort());
        }
        //atualizar a tabela de produtos
        setUpTable();
        //limpar as caixas de texto
        DescVenda.setText("");
        PrecoVenda.setText("");
        jInternalFrame1.setVisible(false);
    }//GEN-LAST:event_BVendaActionPerformed

    /**
     * Evento associado ao botão que realiza uma pesquisa a partir da descrição
     * de um produto e exibe o produto escolhido na tabela de compra
     *
     * @param evt
     */
    private void BPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BPesquisaActionPerformed
        //armazena o valor escrito na caixa de texto
        String pesq = DescCompra.getText();
        //envia a descrição para o indexador (se já houver algum)
        if (peer.getIndexerPort() != -1) {
            //cria a mensagem de deseja comprar com a descrição do produto
            Message m = new Message(peer.getID(), "wannabuy" + pesq);
            //enviar para pesquisar na thread do indexador se há o produto
            //o indexador retorna um map com todos os vendedores que possuem o produto desejado
            Map<Integer, Product> prods = peer.sendBuyRequest(m, peer.getIndexerPort());
            if (!prods.isEmpty()) {
                double preco = 0;
                int iDEscolhido = -1;
                //escolhe dentro da lista o melhor vendedor
                for (int iD : prods.keySet()) {
                    //exibe o id do peer e o produto de cada um
                    System.out.println(iD + " prods " + prods.get(iD));
                    //se ainda não escolheu nenhum pega o primeiro que ter o produto
                    if (iDEscolhido == -1) {
                        preco = prods.get(iD).getPreco();
                        iDEscolhido = iD;
                    }//se já escolheu algum vendedor verifica se o preço do próximo é menor ou a reputação melhor 
                    else if (iDEscolhido != -1) {
                        //verifica se o preço do próximo é menor
                        if (prods.get(iD).getPreco() < preco) {
                            preco = prods.get(iD).getPreco();
                            iDEscolhido = iD;
                        }//se o preço for igual analiza a reputação 
                        else if (prods.get(iD).getPreco() == preco) {
                            Map<Integer, Integer> reputations = peer.getReputations();
                            //se o peer escolhido já possuir uma reputação analisa se ele é maior ou menor que a do próximo peer
                            if (reputations.containsKey(iDEscolhido)) {
                                //se os dois possuirem reputação mas a do próximo é maior, escolhe o próximo peer para comprar
                                if (reputations.containsKey(iD) && reputations.get(iD) > reputations.get(iDEscolhido)) {
                                    preco = prods.get(iD).getPreco();
                                    iDEscolhido = iD;
                                }//se a reputação do peer escolhido é negativa, prefere comprar do próximo peer
                                else if (reputations.get(iDEscolhido) < 0) {
                                    preco = prods.get(iD).getPreco();
                                    iDEscolhido = iD;
                                }
                            }//se o próximo peer possuir uma reputação e ela é positiva, prefere comprar do novo peer  
                            else if (reputations.containsKey(iD) && reputations.get(iD) >= 0) {
                                preco = prods.get(iD).getPreco();
                                iDEscolhido = iD;
                            }
                        }
                    }
                }

                //exibe os dados da compra na tabela de compra
                jInternalFrame3.setVisible(true);
                DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
                //atualizar a tabela a partir dos dados em produtos
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{prods.get(iDEscolhido).getDescricao(), prods.get(iDEscolhido).getPreco2(), iDEscolhido});

                jTable2.setModel(tableModel);
                tableModel.fireTableDataChanged();
            }//se não houver nenhum produto exibe uma mensagem de alerta
            else {
                JOptionPane.showMessageDialog(null, "Nenhum produto encontrado!");
            }
        }//se não houver um indexador exibe uma mensagem de alerta 
        else {
            JOptionPane.showMessageDialog(null, "Aguarde indexador para completar ação!");
        }


    }//GEN-LAST:event_BPesquisaActionPerformed

    /**
     * Evento associado ao botão de comprar, que armazena o produto selecionado
     * da tabela e envia solicitação de compra.
     *
     * @param evt
     */
    private void BComprar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BComprar1ActionPerformed
        DescCompra.setText("");
        //se nenhum produto foi selecionado na tabela exibe mensagem de alerta
        if (jTable2.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Nenhum produto selecionado!");
        } else {
            //armazena os dados do produto exibido na tabela
            String descricao = jTable2.getValueAt(jTable2.getSelectedRow(), 0).toString();
            double preco = Double.parseDouble(jTable2.getValueAt(jTable2.getSelectedRow(), 1).toString());
            int id = Integer.parseInt(jTable2.getValueAt(jTable2.getSelectedRow(), 2).toString());

            DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
            tableModel.setRowCount(0);
            tableModel.fireTableDataChanged();
            //envia mensagem ao indexador solicitando ip, porta e chave pública do peer vendedor
            PeerAnswer pkey = peer.sendBuy(id);
            try {
                //encriptografa mensagem de compra
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, pkey.getPublicKey());
                byte[] encrypted = cipher.doFinal(descricao.getBytes(StandardCharsets.UTF_8));
                String buyMessage = new String(DatatypeConverter.printHexBinary(encrypted));
                Message mess = new Message(peer.getID(), buyMessage);
                //envia mensagem de compra ao peer vendedor e armazena a resposta se foi bem sucedida ou não a compra (1 ou -1
                int sucess = peer.sendBuyFS(mess, pkey.getIp(), pkey.getPort());
                //se não foi bem sucedida
                if (sucess == -1) {
                    JOptionPane.showMessageDialog(null, "Compra mal sucedida :(");
                    peer.setReputations(id, sucess);
                }//se foi bem sucedida
                else {
                    JOptionPane.showMessageDialog(null, "Compra realizada com sucesso!");
                    peer.setReputations(id, sucess);
                }
            jInternalFrame3.setVisible(false);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(CompraEVenda.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(CompraEVenda.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(CompraEVenda.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(CompraEVenda.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(CompraEVenda.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_BComprar1ActionPerformed

    /**
     * Evento associado ao menu de saída que envia uma mensagem de despedida ao
     * indexador e fecha o processo.
     *
     * @param evt
     */
    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        if (peer.isIndexerOn()) {
            peer.sendBye();
        }
        System.exit(0);
    }//GEN-LAST:event_jMenu3MouseClicked

    /**
     * Atualiza a tabela de produtos a serem vendidos pelo peer de acordo com a
     * lista de produtos do peer
     */
    void setUpTable() {
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        //atualizar a tabela a partir dos dados em produtos
        tableModel.setRowCount(0);
        for (Product p : peer.getProdutos()) {
            tableModel.addRow(new Object[]{p.getDescricao(), p.getPreco2()});
        }
        jTable1.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BComprar1;
    private javax.swing.JButton BPesquisa;
    private javax.swing.JButton BVenda;
    private javax.swing.JTextField DescCompra;
    private javax.swing.JTextField DescVenda;
    private javax.swing.JTextField PrecoVenda;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JInternalFrame jInternalFrame2;
    private javax.swing.JInternalFrame jInternalFrame3;
    private javax.swing.JInternalFrame jInternalFrame4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
