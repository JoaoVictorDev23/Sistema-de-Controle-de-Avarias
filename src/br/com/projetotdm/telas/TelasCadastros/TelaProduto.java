
/*
 * The MIT License
 *
 * Copyright 2023 João Victor Souza de Faria.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.projetotdm.telas.TelasCadastros;

import java.sql.*;
import br.com.projetotdm.DAO.ModuloConexao;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;


/**
 * Tela do Produto
 *
 * @author João Victor Souza de Faria
 * @version 1.1
 *
 */
public class TelaProduto extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private static String usuariologado;


    /**
     * Criação e inicialização da Tela de Produtos
     */
    public TelaProduto(String usuariologado) {
        conexao = ModuloConexao.conector();
        initComponents();   
        this.usuariologado = usuariologado;
        btn_produto_atualizar.setEnabled(false);
        btn_produto_excluir.setEnabled(false);
        ((DefaultTableModel) tbl_produto.getModel()).setRowCount(0);
        

    }

    //codigo crud 
    //Função Limpar   
    
    private void limpar() {
        txt_produto_nome.setText(null);
        txt_produto_valor.setText(null);
        txtpane_produto_descri.setText(null);
        txt_produto_marca.setText(null);
        txt_produto_id.setText(null);
        btn_produto_adicionar.setEnabled(true);
        txt_produto_pesquisar.setText(null);
        btn_produto_atualizar.setEnabled(false);
        btn_produto_excluir.setEnabled(false);
        
        ((DefaultTableModel) tbl_produto.getModel()).setRowCount(0);
        


    }
    
    //Função Adicionar

    private void adicionar() {
        String instru_sql = "insert into tbproduto(produto_nome,produto_descri,produto_valor,produto_marca) values(?,?,?,?)";
        try {
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, txt_produto_nome.getText());
            pst.setString(2, txtpane_produto_descri.getText());
            pst.setString(3, txt_produto_valor.getText());
            pst.setString(4, txt_produto_marca.getText());

            if (txt_produto_nome.getText().isEmpty() || txt_produto_marca.getText().isEmpty() || txt_produto_valor.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios(*)!");

            } else {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Produto adicionado com sucesso");
                    

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro! \n" + e);
        }

    }
    //Função Alterar
    private void atualizar() {
        String instru_sql = "update tbproduto set produto_nome=?,produto_valor=?,produto_descri=?,produto_marca=? where produto_id=?";

        try {
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, txt_produto_nome.getText());
            pst.setString(2, txt_produto_valor.getText());
            pst.setString(3, txtpane_produto_descri.getText());
            pst.setString(4, txt_produto_marca.getText());
            pst.setString(5, txt_produto_id.getText());

            if (txt_produto_id.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Selecione um produto para realizar a alteração!");

            }

            else {

                if (txt_produto_nome.getText().isEmpty() || txt_produto_valor.getText().isEmpty() || txt_produto_marca.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Os dados obrigatorios '*' devem ser preenchidos!");

                } else {

                    int adicionado = pst.executeUpdate();
                    if (adicionado > 0) {
                        JOptionPane.showMessageDialog(null, "Produto atualizado com sucesso");
                        

                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de Atualização! \n"+e);

        }

    }
    
     //Função de Pesquisar e popular a tabela   


    private void buscar() {
        
        String instru_sql = "select produto_id as ID,produto_nome as Nome,produto_descri as Descrição,produto_valor as Valor,produto_marca as Marca from tbproduto where produto_nome like ?";
        
        try {
            pst=conexao.prepareCall(instru_sql);
            pst.setString(1,txt_produto_pesquisar.getText()+"%");
            rs=pst.executeQuery();
            
           // biblioteca rs2xml.jar para preencher as tabelas
           tbl_produto.setModel(DbUtils.resultSetToTableModel(rs));
           btn_produto_atualizar.setEnabled(true);
           btn_produto_excluir.setEnabled(true);
           
           
           
           
            
        } catch (Exception e) {
            System.out.println("Ocorreu um erro: \n"+e);
        }

    }
    
    
    //Função de Setar os campos com o clique na tabela   
    
    public void setar_campos(){

        int setar = tbl_produto.getSelectedRow();
        txt_produto_id.setText(tbl_produto.getModel().getValueAt(setar,0).toString());
        txt_produto_nome.setText(tbl_produto.getModel().getValueAt(setar, 1).toString());
        txtpane_produto_descri.setText(tbl_produto.getModel().getValueAt(setar, 2).toString());
        txt_produto_valor.setText(tbl_produto.getModel().getValueAt(setar, 3).toString());
        txt_produto_marca.setText(tbl_produto.getModel().getValueAt(setar, 4).toString());
        
        //desabilitar botao adicionar
        
        btn_produto_adicionar.setEnabled(false);
    
    }
    
      /**
     * Metodo para salvar logs no Banco de Dados.
     * @param usuariologado 
     */    
    public void salvarlog_adicionar_cliente (String usuariologado) {
        String instru_sql = "INSERT INTO tbLog (tb_log_usuario,tb_log_datahora,tb_log_operacao,tb_log_descri) VALUES (?, ?, ?, ?)";
        java.util.Date currentDate = new java.util.Date();
        Timestamp timestamp = new Timestamp(currentDate.getTime());

        try {
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, usuariologado);
            pst.setTimestamp(2, timestamp);
            pst.setString(3, "Cadastro de Produto.");
            pst.setString(4, "Produto cadastrado no Sistema:"+txt_produto_nome.getText());

            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro!\n"+e);
        }

    }
       /**
     * Metodo para salvar logs no Banco de Dados.
     * @param usuariologado 
     */    
    public void salvarlog_atualizar_cliente (String usuariologado) {
        String instru_sql = "INSERT INTO tbLog (tb_log_usuario,tb_log_datahora,tb_log_operacao,tb_log_descri) VALUES (?, ?, ?, ?)";
        java.util.Date currentDate = new java.util.Date();
        Timestamp timestamp = new Timestamp(currentDate.getTime());

        try {
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, usuariologado);
            pst.setTimestamp(2, timestamp);
            pst.setString(3, "Atualização na Tela de Usuário.");
            pst.setString(4, "Produto Alterado no Sistema - ID:"+txt_produto_id.getText());

            pst.executeUpdate();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro! \n"+e);
        }

    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        txt_produto_nome = new javax.swing.JTextField();
        lbl_produto_nome = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtpane_produto_descri = new javax.swing.JTextPane();
        txt_produto_marca = new javax.swing.JTextField();
        txt_produto_valor = new javax.swing.JTextField();
        lbl_produto_marca = new javax.swing.JLabel();
        lbl_produto_valor = new javax.swing.JLabel();
        lbl_produto_descri = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_produto = new javax.swing.JTable();
        txt_produto_pesquisar = new javax.swing.JTextField();
        btn_produto_pesquisar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txt_produto_id = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btn_produto_adicionar = new javax.swing.JButton();
        btn_produto_atualizar = new javax.swing.JButton();
        btn_produto_excluir = new javax.swing.JButton();
        lbl_produto_pesquisar = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Produtos");
        setToolTipText("");

        jLayeredPane1.setToolTipText("Painel de Cadastro");

        txt_produto_nome.setColumns(25);

        lbl_produto_nome.setText("Nome produto:*");

        jScrollPane1.setViewportView(txtpane_produto_descri);

        txt_produto_marca.setColumns(15);

        txt_produto_valor.setColumns(10);
        txt_produto_valor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_produto_valorKeyTyped(evt);
            }
        });

        lbl_produto_marca.setText("Marca:*");

        lbl_produto_valor.setText("Valor unitário:*");

        lbl_produto_descri.setText("Descrição:");

        tbl_produto = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        tbl_produto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Descrição", "Valor", "Marca"
            }
        ));
        tbl_produto.setToolTipText("");
        tbl_produto.setFocusable(false);
        tbl_produto.getTableHeader().setReorderingAllowed(false);
        tbl_produto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_produtoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_produto);

        txt_produto_pesquisar.setToolTipText("Pesquise seu item");
        txt_produto_pesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_produto_pesquisarKeyPressed(evt);
            }
        });

        btn_produto_pesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/projetotdm/icones/lupa20px.png"))); // NOI18N
        btn_produto_pesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_produto_pesquisarActionPerformed(evt);
            }
        });

        jLabel1.setText("ID produto:");

        txt_produto_id.setColumns(5);
        txt_produto_id.setEnabled(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_produto_adicionar.setBackground(new java.awt.Color(204, 255, 204));
        btn_produto_adicionar.setFont(new java.awt.Font("Serif", 1, 13)); // NOI18N
        btn_produto_adicionar.setText("Cadastrar");
        btn_produto_adicionar.setPreferredSize(new java.awt.Dimension(115, 32));
        btn_produto_adicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_produto_adicionarActionPerformed(evt);
            }
        });

        btn_produto_atualizar.setBackground(new java.awt.Color(255, 255, 153));
        btn_produto_atualizar.setFont(new java.awt.Font("Serif", 1, 13)); // NOI18N
        btn_produto_atualizar.setText("Atualizar");
        btn_produto_atualizar.setPreferredSize(new java.awt.Dimension(115, 32));
        btn_produto_atualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_produto_atualizarActionPerformed(evt);
            }
        });

        btn_produto_excluir.setBackground(new java.awt.Color(255, 51, 51));
        btn_produto_excluir.setFont(new java.awt.Font("Serif", 1, 13)); // NOI18N
        btn_produto_excluir.setText("Limpar campos");
        btn_produto_excluir.setToolTipText("Limpar Campos");
        btn_produto_excluir.setPreferredSize(new java.awt.Dimension(115, 32));
        btn_produto_excluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_produto_excluirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(btn_produto_adicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_produto_atualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_produto_excluir, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_produto_adicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_produto_atualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_produto_excluir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lbl_produto_pesquisar.setText("Pesquisar Produto:");

        jLayeredPane1.setLayer(txt_produto_nome, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lbl_produto_nome, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txt_produto_marca, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txt_produto_valor, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lbl_produto_marca, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lbl_produto_valor, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lbl_produto_descri, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jScrollPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txt_produto_pesquisar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(btn_produto_pesquisar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txt_produto_id, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lbl_produto_pesquisar, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txt_produto_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jLayeredPane1Layout.createSequentialGroup()
                            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lbl_produto_nome)
                                .addComponent(jLabel1)
                                .addComponent(lbl_produto_valor)
                                .addComponent(lbl_produto_descri))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1)
                                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                                    .addComponent(txt_produto_valor, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lbl_produto_marca))
                                .addComponent(txt_produto_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_produto_nome, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE))))
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addComponent(lbl_produto_pesquisar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_produto_pesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_produto_pesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_produto_pesquisar)
                        .addComponent(lbl_produto_pesquisar))
                    .addComponent(btn_produto_pesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_produto_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_produto_nome, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_produto_nome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_produto_valor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_produto_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_produto_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_produto_valor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_produto_descri))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );

        setBounds(180, 130, 639, 465);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_produto_adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_produto_adicionarActionPerformed
        //Setando o botao produto com a função
        adicionar();
        salvarlog_adicionar_cliente(usuariologado);
        limpar();
    }//GEN-LAST:event_btn_produto_adicionarActionPerformed

    private void tbl_produtoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_produtoMouseClicked
        //setando o click duplo do mouse na função
        if(evt.getClickCount()==2){
        setar_campos();
        }
    }//GEN-LAST:event_tbl_produtoMouseClicked

    private void btn_produto_atualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_produto_atualizarActionPerformed
        // TODO add your handling code here:
        atualizar();
        salvarlog_atualizar_cliente(usuariologado);
        limpar();
    }//GEN-LAST:event_btn_produto_atualizarActionPerformed

    private void btn_produto_pesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_produto_pesquisarActionPerformed
        // TODO add your handling code here:
        buscar();
    }//GEN-LAST:event_btn_produto_pesquisarActionPerformed

    private void btn_produto_excluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_produto_excluirActionPerformed
        // TODO add your handling code here:
        limpar();
    }//GEN-LAST:event_btn_produto_excluirActionPerformed

    private void txt_produto_valorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_produto_valorKeyTyped
        String caracteres = "0123456789." ;
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    
    }//GEN-LAST:event_txt_produto_valorKeyTyped

    private void txt_produto_pesquisarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_produto_pesquisarKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            // Chama a função f() quando a tecla Enter é pressionada
            buscar();
        }
    }//GEN-LAST:event_txt_produto_pesquisarKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_produto_adicionar;
    private javax.swing.JButton btn_produto_atualizar;
    private javax.swing.JButton btn_produto_excluir;
    private javax.swing.JButton btn_produto_pesquisar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_produto_descri;
    private javax.swing.JLabel lbl_produto_marca;
    private javax.swing.JLabel lbl_produto_nome;
    private javax.swing.JLabel lbl_produto_pesquisar;
    private javax.swing.JLabel lbl_produto_valor;
    private javax.swing.JTable tbl_produto;
    private javax.swing.JTextField txt_produto_id;
    private javax.swing.JTextField txt_produto_marca;
    private javax.swing.JTextField txt_produto_nome;
    private javax.swing.JTextField txt_produto_pesquisar;
    private javax.swing.JTextField txt_produto_valor;
    private javax.swing.JTextPane txtpane_produto_descri;
    // End of variables declaration//GEN-END:variables
}
