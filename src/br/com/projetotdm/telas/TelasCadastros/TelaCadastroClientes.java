/*
 * The MIT License
 *
 * Copyright 2023 joao.faria.
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

import br.com.projetotdm.DAO.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import javax.swing.JOptionPane;
import br.com.projetotdm.telas.TelasCadastros.TelaAvarias;

/**
 *
 * @author joao.faria
 */
public class TelaCadastroClientes extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private static String usuariologado;

    /**
     * Creates new form TelaCadastroMotora
     */
    public TelaCadastroClientes(String usuariologado) {
        conexao = ModuloConexao.conector();
        initComponents();
        this.usuariologado = usuariologado;
        btn_cliente_atualizar.setEnabled(false);
        btn_cliente_excluir.setEnabled(false);

    }

    /**
     * Metodo para limpar os textfields
     */
    private void limpar() {
        txt_cliente_nome.setText(null);
        txt_cliente_cpf.setText(null);
        txt_cliente_email.setText(null);
        txt_cliente_fone.setText(null);
        txt_cliente_ender.setText(null);
        btn_cliente_atualizar.setEnabled(false);
        btn_cliente_excluir.setEnabled(false);
        btn_cliente_adicionar.setEnabled(true);

    }

    /**
     * Metodo para buscar dados no banco de dados.
     */
    private void buscar() {
        String instru_sql_select = "select * from tbfornecedor where fornecedor_nome=?";

        try {

            pst = conexao.prepareStatement(instru_sql_select);
            pst.setString(1, txt_cliente_nome.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txt_cliente_cpf.setText(rs.getString(3));
                txt_cliente_fone.setText(rs.getString(5));
                txt_cliente_ender.setText(rs.getString(4));
                txt_cliente_email.setText(rs.getString(6));
                btn_cliente_atualizar.setEnabled(true);
                btn_cliente_excluir.setEnabled(true);
                btn_cliente_adicionar.setEnabled(false);

            } else {
                JOptionPane.showMessageDialog(null, "Cliente não encontrado.");
                btn_cliente_atualizar.setEnabled(false);
                btn_cliente_excluir.setEnabled(false);
                btn_cliente_adicionar.setEnabled(true);
                limpar();

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Metodo para adicionar dados no banco de dados.
     */
    private boolean adicionar() {
        String instru_sql_insert = "insert into tbfornecedor(fornecedor_nome, fornecedor_cpf, fornecedor_endereco, fornecedor_fone, fornecedor_email) values(?,?,?,?,?)";
        try {

            pst = conexao.prepareStatement(instru_sql_insert);
            pst.setString(1, txt_cliente_nome.getText());
            pst.setString(2, txt_cliente_cpf.getText());
            pst.setString(3, txt_cliente_ender.getText());
            pst.setString(4, txt_cliente_fone.getText());
            pst.setString(5, txt_cliente_email.getText());

            if (txt_cliente_nome.getText().isEmpty() || txt_cliente_fone.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios!");
                return false;

            } else {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso");
                    limpar();
                    return true;

                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro!" + e);
        }
        return false;

    }

    /**
     * Metodo para atualizar dados no banco de dados
     */
    private boolean atualizar() {
        String instru_sql_update = "update tbfornecedor set fornecedor_nome=?, fornecedor_endereco=?, fornecedor_fone=?, fornecedor_email=? where fornecedor_cpf=?";

        try {

            pst = conexao.prepareStatement(instru_sql_update);
            pst.setString(1, txt_cliente_nome.getText());
            pst.setString(2, txt_cliente_ender.getText());
            pst.setString(3, txt_cliente_fone.getText());
            pst.setString(4, txt_cliente_email.getText());
            pst.setString(5, txt_cliente_cpf.getText());

            if (txt_cliente_cpf.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Realize a busca pelo CPF para encontrar seu cliente!");
                return false;

            }

            if (txt_cliente_nome.getText().isEmpty() || txt_cliente_fone.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha os dados para atualização!");

            } else {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente atualizado com sucesso");
                    limpar();

                    return true;
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de Atualização!");

        }

        return false;
    }

    /**
     * Metodo para excluir dados no banco de dados.
     */
    public boolean excluir() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o Cliente?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String instru_sql_delete = "delete from tbfornecedor where fornecedor_cpf=?";
            try {

                pst = conexao.prepareStatement(instru_sql_delete);
                pst.setString(1, txt_cliente_cpf.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente removido com sucesso.");
                    limpar();
                    return true;

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro de exclusao!");
            }
        }
        return false;

    }

    /**
     * Metodo para salvar logs no Banco de Dados.
     *
     * @param usuariologado
     */
    public void salvarlog_adicionar_cliente(String usuariologado) {
        String instru_sql = "INSERT INTO tbLog (tb_log_usuario,tb_log_datahora,tb_log_operacao,tb_log_descri) VALUES (?, ?, ?, ?)";
        java.util.Date currentDate = new java.util.Date();
        Timestamp timestamp = new Timestamp(currentDate.getTime());

        try {
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, usuariologado);
            pst.setTimestamp(2, timestamp);
            pst.setString(3, "Cadastro na Tela Clientes/Fornecedor.");
            pst.setString(4, "CPF Adicionado no Sistema:" + txt_cliente_cpf.getText());

            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro!" + e);
        }

    }

    /**
     * Metodo para salvar logs no Banco de Dados.
     *
     * @param usuariologado
     */
    public void salvarlog_atualizar_cliente(String usuariologado) {
        String instru_sql = "INSERT INTO tbLog (tb_log_usuario,tb_log_datahora,tb_log_operacao,tb_log_descri) VALUES (?, ?, ?, ?)";
        java.util.Date currentDate = new java.util.Date();
        Timestamp timestamp = new Timestamp(currentDate.getTime());

        try {
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, usuariologado);
            pst.setTimestamp(2, timestamp);
            pst.setString(3, "Atualização na Tela Clientes/Fornecedor.");
            pst.setString(4, "CPF Alterado do Sistema:" + txt_cliente_cpf.getText());

            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro!" + e);
        }

    }

    /**
     * Metodo para salvar log de exclusão no banco de dados.
     *
     * @param usuariologado
     */
    public void salvarlog_excluir_cliente(String usuariologado) {
        String instru_sql = "INSERT INTO tbLog (tb_log_usuario,tb_log_datahora,tb_log_operacao,tb_log_descri) VALUES (?, ?, ?, ?)";
        java.util.Date currentDate = new java.util.Date();
        Timestamp timestamp = new Timestamp(currentDate.getTime());

        try {
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, usuariologado);
            pst.setTimestamp(2, timestamp);
            pst.setString(3, "Exclusão na Tela de Clientes/Fornecedor.");
            pst.setString(4, "CPF Excluido do Sistema:" + txt_cliente_cpf.getText());

            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro!" + e);
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

        jProgressBar1 = new javax.swing.JProgressBar();
        lbl_cliente_nome = new javax.swing.JLabel();
        lbl_cliente_cpf = new javax.swing.JLabel();
        lbl_cliente_ender = new javax.swing.JLabel();
        lbl_cliente_fone = new javax.swing.JLabel();
        lbl_cliente_email = new javax.swing.JLabel();
        txt_cliente_cpf = new javax.swing.JFormattedTextField();
        txt_cliente_nome = new javax.swing.JTextField();
        txt_cliente_ender = new javax.swing.JTextField();
        txt_cliente_fone = new javax.swing.JFormattedTextField();
        txt_cliente_email = new javax.swing.JTextField();
        btn_cliente_buscar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btn_cliente_excluir = new javax.swing.JButton();
        btn_cliente_atualizar = new javax.swing.JButton();
        btn_cliente_adicionar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Cliente");
        setToolTipText("Cadastro de Cliente");

        lbl_cliente_nome.setText("Nome :*");

        lbl_cliente_cpf.setText("CNPJ:");

        lbl_cliente_ender.setText("Endereço:");

        lbl_cliente_fone.setText("Telefone:*");

        lbl_cliente_email.setText("E-mail:");

        txt_cliente_cpf.setColumns(15);
        try {
            txt_cliente_cpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_cliente_cpf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cliente_cpfActionPerformed(evt);
            }
        });
        txt_cliente_cpf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_cliente_cpfKeyPressed(evt);
            }
        });

        txt_cliente_nome.setColumns(30);
        txt_cliente_nome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cliente_nomeKeyTyped(evt);
            }
        });

        txt_cliente_ender.setColumns(30);
        txt_cliente_ender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cliente_enderActionPerformed(evt);
            }
        });

        txt_cliente_fone.setColumns(12);
        try {
            txt_cliente_fone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)# ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        txt_cliente_email.setColumns(30);
        txt_cliente_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cliente_emailActionPerformed(evt);
            }
        });

        btn_cliente_buscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/projetotdm/icones/lupa20px.png"))); // NOI18N
        btn_cliente_buscar.setToolTipText("Busca pelo CPF");
        btn_cliente_buscar.setPreferredSize(new java.awt.Dimension(64, 64));
        btn_cliente_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cliente_buscarActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_cliente_excluir.setBackground(new java.awt.Color(255, 51, 51));
        btn_cliente_excluir.setFont(new java.awt.Font("Serif", 1, 13)); // NOI18N
        btn_cliente_excluir.setText("Excluir");
        btn_cliente_excluir.setToolTipText("Excluir");
        btn_cliente_excluir.setPreferredSize(new java.awt.Dimension(91, 32));
        btn_cliente_excluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cliente_excluirActionPerformed(evt);
            }
        });

        btn_cliente_atualizar.setBackground(new java.awt.Color(255, 255, 153));
        btn_cliente_atualizar.setFont(new java.awt.Font("Serif", 1, 13)); // NOI18N
        btn_cliente_atualizar.setText("Atualizar");
        btn_cliente_atualizar.setToolTipText("Atualizar");
        btn_cliente_atualizar.setPreferredSize(new java.awt.Dimension(64, 64));
        btn_cliente_atualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cliente_atualizarActionPerformed(evt);
            }
        });

        btn_cliente_adicionar.setBackground(new java.awt.Color(204, 255, 204));
        btn_cliente_adicionar.setFont(new java.awt.Font("Serif", 1, 13)); // NOI18N
        btn_cliente_adicionar.setText("Cadastrar");
        btn_cliente_adicionar.setToolTipText("Adicionar");
        btn_cliente_adicionar.setPreferredSize(new java.awt.Dimension(91, 32));
        btn_cliente_adicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cliente_adicionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(btn_cliente_adicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_cliente_atualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_cliente_excluir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cliente_excluir, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cliente_atualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cliente_adicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_cliente_fone)
                            .addComponent(lbl_cliente_ender, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_cliente_nome, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_cliente_nome, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txt_cliente_fone, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_cliente_cpf)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_cliente_cpf, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_cliente_ender, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_cliente_email)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(txt_cliente_email, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(btn_cliente_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_cliente_nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_cliente_nome))
                    .addComponent(btn_cliente_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_cliente_cpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_cliente_cpf)
                    .addComponent(lbl_cliente_fone)
                    .addComponent(txt_cliente_fone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_cliente_ender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_cliente_ender))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_cliente_email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_cliente_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );

        setBounds(290, 190, 514, 323);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_cliente_nomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cliente_nomeKeyTyped
        String caracteres = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZáéíóúãõâêîôûàèìòùÁÉÍÓÚÃÕÂÊÎÔÛÀÈÌÒÙ ";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_cliente_nomeKeyTyped

    private void btn_cliente_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_buscarActionPerformed
        buscar();

    }//GEN-LAST:event_btn_cliente_buscarActionPerformed

    private void btn_cliente_excluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_excluirActionPerformed
        if (excluir()) {
            salvarlog_excluir_cliente(usuariologado);
            limpar();
        }

    }//GEN-LAST:event_btn_cliente_excluirActionPerformed

    private void btn_cliente_atualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_atualizarActionPerformed

        if (atualizar()) {
            salvarlog_atualizar_cliente(usuariologado);
            limpar();
        }
    }//GEN-LAST:event_btn_cliente_atualizarActionPerformed

    private void btn_cliente_adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_adicionarActionPerformed
        if (adicionar()) {
            salvarlog_adicionar_cliente(usuariologado);
            limpar();
        }

    }//GEN-LAST:event_btn_cliente_adicionarActionPerformed

    private void txt_cliente_cpfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cliente_cpfKeyPressed

        if (evt.getKeyCode() == evt.VK_ENTER) {
            // Chama a função f() quando a tecla Enter é pressionada
            buscar();
        }
    }//GEN-LAST:event_txt_cliente_cpfKeyPressed

    private void txt_cliente_cpfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cliente_cpfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_cliente_cpfActionPerformed

    private void txt_cliente_enderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cliente_enderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_cliente_enderActionPerformed

    private void txt_cliente_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cliente_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_cliente_emailActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cliente_adicionar;
    private javax.swing.JButton btn_cliente_atualizar;
    private javax.swing.JButton btn_cliente_buscar;
    private javax.swing.JButton btn_cliente_excluir;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JLabel lbl_cliente_cpf;
    private javax.swing.JLabel lbl_cliente_email;
    private javax.swing.JLabel lbl_cliente_ender;
    private javax.swing.JLabel lbl_cliente_fone;
    private javax.swing.JLabel lbl_cliente_nome;
    private javax.swing.JFormattedTextField txt_cliente_cpf;
    private javax.swing.JTextField txt_cliente_email;
    private javax.swing.JTextField txt_cliente_ender;
    private javax.swing.JFormattedTextField txt_cliente_fone;
    private javax.swing.JTextField txt_cliente_nome;
    // End of variables declaration//GEN-END:variables
}
