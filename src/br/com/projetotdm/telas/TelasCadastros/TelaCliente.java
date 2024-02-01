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
/**
 * Tela de Cadastro de Clientes
 *
 * @author João Victor Souza de Faria
 * @version 1.1
 *
 */
import java.sql.*;
import br.com.projetotdm.DAO.ModuloConexao;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author joao.faria
 */
public class TelaCliente extends javax.swing.JInternalFrame {
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private static String usuariologado;

    
    

    /**
     * Metodo que inicializa a TelaCliente.
     */
    public TelaCliente(String usuariologado) {
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
        String instru_sql = "select*from tbclientes where cli_cpf=?";
        
        try {     
            
            
            
            String cpf = txt_cliente_cpf.getText();
            cpf = cpf.replaceAll("[^0-9]", "");            
            
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, cpf);
            rs = pst.executeQuery();
            if (rs.next()) {
                txt_cliente_nome.setText(rs.getString(2));
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

    private void adicionar() {
        String instru_sql = "insert into tbclientes(cli_nome,cli_cpf,cli_ender,cli_fone,cli_email)values(?,?,?,?,?)";
        try {
            
            String cpf = txt_cliente_cpf.getText();
            cpf = cpf.replaceAll("[^0-9]", "");            
            
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, txt_cliente_nome.getText());
            pst.setString(2, cpf);
            pst.setString(3, txt_cliente_ender.getText());
            pst.setString(4, txt_cliente_fone.getText());
            pst.setString(5, txt_cliente_email.getText());

            if (txt_cliente_nome.getText().isEmpty() || txt_cliente_cpf.getText().isEmpty() || txt_cliente_fone.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios!");

            } else {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário adicionado com sucesso");

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro!");
        }

    }
    /**
     * Metodo para atualizar dados no banco de dados
     */

    private void atualizar() {
        String instru_sql = "update tbclientes set cli_nome=?,cli_ender=?,cli_fone=?,cli_email=? where cli_cpf=?";

        try {
            
            String cpf = txt_cliente_cpf.getText();
            cpf = cpf.replaceAll("[^0-9]", "");            
            
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, txt_cliente_nome.getText());
            pst.setString(2, txt_cliente_ender.getText());
            pst.setString(3, txt_cliente_fone.getText());
            pst.setString(4, txt_cliente_email.getText());
            pst.setString(5, cpf);

            if (txt_cliente_cpf.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Realize a busca pelo CPF para encontrar seu cliente!");

            }

            if (txt_cliente_nome.getText().isEmpty() || txt_cliente_fone.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha os dados para atualização!");

            } else {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário atualizado com sucesso");

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de Atualização!");

        }

    }
    /**
     * Metodo para excluir dados no banco de dados.
     */

    public void excluir() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o usuário?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String instru_sql = "delete from tbclientes where cli_cpf=?";
            try {
                
                String cpf = txt_cliente_cpf.getText();
                cpf = cpf.replaceAll("[^0-9]", "");              
                
                pst = conexao.prepareStatement(instru_sql);
                pst.setString(1, cpf);
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário removido com sucesso.");
                    
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro de exclusao!");
            }
        }

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
            pst.setString(3, "Cadastro na Tela Cliente.");
            pst.setString(4, "CPF Adicionado no Sistema:"+txt_cliente_cpf.getText());

            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro!"+e);
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
            pst.setString(3, "Atualização na Tela Cliente.");
            pst.setString(4, "CPF Alterado do Sistema:"+txt_cliente_cpf.getText());

            pst.executeUpdate();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro!"+e);
        }

    }
    /**
     * Metodo para salvar log de exclusão no banco de dados.
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
            pst.setString(3, "Exclusão na Tela de Clientes.");
            pst.setString(4, "CPF Excluido do Sistema:"+txt_cliente_cpf.getText());

           pst.executeUpdate();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro!"+e);
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

        lbl_cliente_nome = new javax.swing.JLabel();
        lbl_cliente_cpf = new javax.swing.JLabel();
        lbl_cliente_ender = new javax.swing.JLabel();
        lbl_cliente_fone = new javax.swing.JLabel();
        lbl_cliente_email = new javax.swing.JLabel();
        txt_cliente_cpf = new javax.swing.JFormattedTextField();
        txt_cliente_nome = new javax.swing.JTextField();
        txt_cliente_ender = new javax.swing.JTextField();
        txt_cliente_email = new javax.swing.JTextField();
        btn_cliente_buscar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btn_cliente_excluir = new javax.swing.JButton();
        btn_cliente_atualizar = new javax.swing.JButton();
        btn_cliente_adicionar = new javax.swing.JButton();
        txt_cliente_fone = new javax.swing.JFormattedTextField();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Cadastro de Comprador");
        setToolTipText("");

        lbl_cliente_nome.setText("Nome :*");

        lbl_cliente_cpf.setText("CPF:*");

        lbl_cliente_ender.setText("Endereço:");

        lbl_cliente_fone.setText("Telefone:*");

        lbl_cliente_email.setText("E-mail:");

        txt_cliente_cpf.setColumns(15);
        try {
            txt_cliente_cpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_cliente_cpf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_cliente_cpfKeyPressed(evt);
            }
        });

        txt_cliente_nome.setColumns(30);
        txt_cliente_nome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cliente_nomeActionPerformed(evt);
            }
        });
        txt_cliente_nome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cliente_nomeKeyTyped(evt);
            }
        });

        txt_cliente_ender.setColumns(30);

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
        btn_cliente_excluir.setToolTipText("Excluir Cliente");
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
        btn_cliente_adicionar.setToolTipText("Adicionar Cliente");
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

        txt_cliente_fone.setColumns(12);
        try {
            txt_cliente_fone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)# ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

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
                                .addComponent(txt_cliente_cpf, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_cliente_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_cliente_ender, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_cliente_email)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_cliente_email, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_cliente_nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_cliente_nome))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cliente_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_cliente_cpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_cliente_cpf)
                        .addComponent(lbl_cliente_fone)
                        .addComponent(txt_cliente_fone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_cliente_ender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_cliente_ender))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_cliente_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_cliente_email))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );

        setBounds(290, 190, 514, 315);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Adiciona função ao botão
     * @param evt 
     */
    private void btn_cliente_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_buscarActionPerformed
        // TODO add your handling code here:
        buscar();
        
        
    }//GEN-LAST:event_btn_cliente_buscarActionPerformed

    /**
     * Adiciona metodo ao botão especifico.
     * @param evt 
     */
    private void btn_cliente_adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_adicionarActionPerformed
        // TODO add your handling code here:
        adicionar();
        salvarlog_adicionar_cliente(usuariologado);
        limpar();
    }//GEN-LAST:event_btn_cliente_adicionarActionPerformed

    /**
     * ]Adiciona metodo ao botão especifico.
     * @param evt 
     */
    private void btn_cliente_excluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_excluirActionPerformed
        // TODO add your handling code here:
        excluir();
        salvarlog_excluir_cliente(usuariologado);
        limpar();
    }//GEN-LAST:event_btn_cliente_excluirActionPerformed

    /**
     * Adiciona metodo ao botão especifico.
     * @param evt 
     */
    private void btn_cliente_atualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_atualizarActionPerformed
        // TODO add your handling code here:
        atualizar();
        salvarlog_atualizar_cliente(usuariologado);
        limpar();
    }//GEN-LAST:event_btn_cliente_atualizarActionPerformed

    private void txt_cliente_nomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cliente_nomeKeyTyped
        String caracteres = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZáéíóúãõâêîôûàèìòùÁÉÍÓÚÃÕÂÊÎÔÛÀÈÌÒÙ ";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }     
    }//GEN-LAST:event_txt_cliente_nomeKeyTyped

    private void txt_cliente_cpfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cliente_cpfKeyPressed
                
        if (evt.getKeyCode() == evt.VK_ENTER) {
            // Chama a função f() quando a tecla Enter é pressionada
            buscar();
        }
    }//GEN-LAST:event_txt_cliente_cpfKeyPressed

    private void txt_cliente_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cliente_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_cliente_emailActionPerformed

    private void txt_cliente_nomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cliente_nomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_cliente_nomeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cliente_adicionar;
    private javax.swing.JButton btn_cliente_atualizar;
    private javax.swing.JButton btn_cliente_buscar;
    private javax.swing.JButton btn_cliente_excluir;
    private javax.swing.JPanel jPanel1;
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
