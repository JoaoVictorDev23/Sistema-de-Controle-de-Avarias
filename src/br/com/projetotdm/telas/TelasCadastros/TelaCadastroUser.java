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
 * Tela de Cadastro de Usuario
 *
 * @author João Victor Souza de Faria
 * @version 1.1
 *
 */
import java.sql.*;
import br.com.projetotdm.DAO.ModuloConexao;
import javax.swing.JOptionPane;

public class TelaCadastroUser extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private static String usuariologado;


    /**
     * Metodo para inicializar a Tela.
     */
    public TelaCadastroUser(String usuariologado) {
        conexao = ModuloConexao.conector();
        initComponents();
        this.usuariologado = usuariologado;
        btn_cadastro_atualizar.setEnabled(false);
        btn_cadastro_excluir.setEnabled(false);
    }
    /**
     * Metodo para limpar os campos
     */

    private void limpar() {
        txt_cadastro_nome.setText(null);
        txt_cadastro_senha.setText(null);
        box_cadastro_perfil.setSelectedItem("Usuario");
        txt_cadastro_user.setText(null);

    }
    /**
     * Metodo para Buscar dados no banco de Dados
     */

    private void buscar() {
        String instru_sql = "select*from tbusuarios where user_login=?";

        try {
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, txt_cadastro_user.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txt_cadastro_nome.setText(rs.getString(2));
                txt_cadastro_senha.setText(rs.getString(4));
                box_cadastro_perfil.setSelectedItem(rs.getString(5));
                btn_cadastro_atualizar.setEnabled(true);
                btn_cadastro_excluir.setEnabled(true);
                btn_cadastro_adicionar.setEnabled(false);

            } else {
                JOptionPane.showMessageDialog(null, "Usuário não encontrado.");
                box_cadastro_perfil.setSelectedItem("Usuario");
                txt_cadastro_nome.setText(null);
                txt_cadastro_senha.setText(null);
                txt_cadastro_user.setText(null);
                btn_cadastro_adicionar.setEnabled(true);
                btn_cadastro_atualizar.setEnabled(false);
                btn_cadastro_excluir.setEnabled(false);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
    /**
     * Metodo para adicionar dados no banco de dados
     */

    private void adicionar() {
        String instru_sql = "insert into tbusuarios(user_nome,user_login,user_senha,user_cargo) values(?,?,?,?)";
        try {
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, txt_cadastro_nome.getText());
            pst.setString(2, txt_cadastro_user.getText());
            pst.setString(3, txt_cadastro_senha.getText());
            pst.setString(4, box_cadastro_perfil.getSelectedItem().toString());

            if (txt_cadastro_user.getText().isEmpty() || txt_cadastro_nome.getText().isEmpty() || txt_cadastro_senha.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");

            } else {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário adicionado com sucesso");

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro! \n"+e);
        }

    }
    /**
     * Metodo para atualizar dados no banco de dados.
     */

    private void atualizar() {
        String instru_sql = "update tbusuarios set user_nome=?,user_senha=?,user_cargo=? where user_login=?";

        try {
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, txt_cadastro_nome.getText());
            pst.setString(2, txt_cadastro_senha.getText());
            pst.setString(3, box_cadastro_perfil.getSelectedItem().toString());
            pst.setString(4, txt_cadastro_user.getText());

            if (txt_cadastro_user.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Realize a busca pelos dados para atualização!");

            }

            if (txt_cadastro_nome.getText().isEmpty() || txt_cadastro_senha.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha os dados para atualização!");

            } else {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário atualizado com sucesso");

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de Atualização! \n"+e);

        }

    }
    /**
     * Metodo para excluir dados no banco de Dados.
     */

    public void excluir() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o usuário?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String instru_sql = "delete from tbusuarios where user_login=?";
            try {
                pst = conexao.prepareStatement(instru_sql);
                pst.setString(1, txt_cadastro_user.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário removido com sucesso.");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro de exclusao! \n"+e);
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
            pst.setString(3, "Cadastro de Usuário.");
            pst.setString(4, "Cadastro do Usuario no Sistema:"+txt_cadastro_user.getText());

            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro! \n"+e);
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
            pst.setString(4, "Usuário Alterado no Sistema:"+txt_cadastro_user.getText());

            pst.executeUpdate();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro! \n"+e);
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
            pst.setString(3, "Usuário excluido no Sistema.");
            pst.setString(4, "Usuário Excluido do Sistema:"+txt_cadastro_user.getText());

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

        jCheckBox1 = new javax.swing.JCheckBox();
        lbl_cadastro_nome = new javax.swing.JLabel();
        lbl_cadastro_user = new javax.swing.JLabel();
        lbl_cadastro_senha = new javax.swing.JLabel();
        lbl_cadastro_cargo = new javax.swing.JLabel();
        txt_cadastro_nome = new javax.swing.JTextField();
        txt_cadastro_user = new javax.swing.JTextField();
        txt_cadastro_senha = new javax.swing.JPasswordField();
        box_cadastro_perfil = new javax.swing.JComboBox<>();
        btn_cadastro_buscar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btn_cadastro_adicionar = new javax.swing.JButton();
        btn_cadastro_excluir = new javax.swing.JButton();
        btn_cadastro_atualizar = new javax.swing.JButton();

        jCheckBox1.setText("jCheckBox1");

        setClosable(true);
        setIconifiable(true);
        setTitle("Usuários");
        setPreferredSize(new java.awt.Dimension(650, 561));

        lbl_cadastro_nome.setText("Nome:");

        lbl_cadastro_user.setText("Usuário:");

        lbl_cadastro_senha.setText("Senha:");

        lbl_cadastro_cargo.setText("Cargo:");

        txt_cadastro_nome.setColumns(10);
        txt_cadastro_nome.setPreferredSize(null);
        txt_cadastro_nome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cadastro_nomeKeyTyped(evt);
            }
        });

        txt_cadastro_user.setColumns(10);
        txt_cadastro_user.setPreferredSize(null);
        txt_cadastro_user.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cadastro_userKeyTyped(evt);
            }
        });

        txt_cadastro_senha.setColumns(10);
        txt_cadastro_senha.setPreferredSize(null);

        box_cadastro_perfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Usuario", "Administrador" }));
        box_cadastro_perfil.setToolTipText("");
        box_cadastro_perfil.setPreferredSize(null);

        btn_cadastro_buscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/projetotdm/icones/lupa20px.png"))); // NOI18N
        btn_cadastro_buscar.setToolTipText("Buscar User");
        btn_cadastro_buscar.setPreferredSize(null);
        btn_cadastro_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cadastro_buscarActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_cadastro_adicionar.setBackground(new java.awt.Color(204, 255, 204));
        btn_cadastro_adicionar.setFont(new java.awt.Font("Serif", 1, 13)); // NOI18N
        btn_cadastro_adicionar.setText("Cadastrar");
        btn_cadastro_adicionar.setToolTipText("");
        btn_cadastro_adicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_cadastro_adicionar.setPreferredSize(new java.awt.Dimension(91, 32));
        btn_cadastro_adicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cadastro_adicionarActionPerformed(evt);
            }
        });

        btn_cadastro_excluir.setBackground(new java.awt.Color(255, 51, 51));
        btn_cadastro_excluir.setFont(new java.awt.Font("Serif", 1, 13)); // NOI18N
        btn_cadastro_excluir.setText("Remover");
        btn_cadastro_excluir.setToolTipText("Remover");
        btn_cadastro_excluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_cadastro_excluir.setPreferredSize(new java.awt.Dimension(91, 32));
        btn_cadastro_excluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cadastro_excluirActionPerformed(evt);
            }
        });

        btn_cadastro_atualizar.setBackground(new java.awt.Color(255, 255, 153));
        btn_cadastro_atualizar.setFont(new java.awt.Font("Serif", 1, 13)); // NOI18N
        btn_cadastro_atualizar.setText("Atualizar");
        btn_cadastro_atualizar.setToolTipText("Atualizar User");
        btn_cadastro_atualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_cadastro_atualizar.setPreferredSize(new java.awt.Dimension(91, 32));
        btn_cadastro_atualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cadastro_atualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(btn_cadastro_adicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_cadastro_atualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(btn_cadastro_excluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cadastro_adicionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_cadastro_atualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cadastro_excluir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_cadastro_senha, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_cadastro_cargo, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_cadastro_nome, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_cadastro_user, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(box_cadastro_perfil, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txt_cadastro_user, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_cadastro_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txt_cadastro_nome, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_cadastro_senha, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_cadastro_user, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_cadastro_user))
                    .addComponent(btn_cadastro_buscar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_cadastro_nome)
                    .addComponent(txt_cadastro_nome, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_cadastro_senha, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_cadastro_senha))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(box_cadastro_perfil, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_cadastro_cargo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        setBounds(250, 160, 448, 285);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Evento no botão para chamar o metodo de busca.
     * @param evt 
     */
    private void btn_cadastro_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cadastro_buscarActionPerformed
        // TODO add your handling code here:
        buscar();
    }//GEN-LAST:event_btn_cadastro_buscarActionPerformed

    /**
     * Evento no botão para chamar os metodos adicionar e limpar.
     * @param evt 
     */
    private void btn_cadastro_adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cadastro_adicionarActionPerformed
        // TODO add your handling code here:
        adicionar();
        salvarlog_adicionar_cliente(usuariologado);
        limpar();
    }//GEN-LAST:event_btn_cadastro_adicionarActionPerformed

    /**
     * Evento no botão que chama os metodos atualizar e limpar
     * @param evt 
     */
    private void btn_cadastro_atualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cadastro_atualizarActionPerformed
        // TODO add your handling code here:
        atualizar();
        salvarlog_atualizar_cliente(usuariologado);
        limpar();
    }//GEN-LAST:event_btn_cadastro_atualizarActionPerformed

    /**
     * Evento no botão que chama o metodo excluir.
     * @param evt 
     */
    private void btn_cadastro_excluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cadastro_excluirActionPerformed
        // TODO add your handling code here:
        excluir();
        salvarlog_excluir_cliente(usuariologado);
        limpar();
    }//GEN-LAST:event_btn_cadastro_excluirActionPerformed

    /**
     * Restrição na TextField em especifico para conter determinados caracteres.
     * @param evt 
     */
    private void txt_cadastro_userKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cadastro_userKeyTyped
        String caracteres = "abcdefghijklmnopqrstuvwxyz.";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }

    }//GEN-LAST:event_txt_cadastro_userKeyTyped

    /**
     * Restrição na TextField para conter determinados caracteres
     * @param evt 
     */
    private void txt_cadastro_nomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cadastro_nomeKeyTyped
        String caracteres = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZáéíóúãõâêîôûàèìòùÁÉÍÓÚÃÕÂÊÎÔÛÀÈÌÒÙ ";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_cadastro_nomeKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> box_cadastro_perfil;
    private javax.swing.JButton btn_cadastro_adicionar;
    private javax.swing.JButton btn_cadastro_atualizar;
    private javax.swing.JButton btn_cadastro_buscar;
    private javax.swing.JButton btn_cadastro_excluir;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_cadastro_cargo;
    private javax.swing.JLabel lbl_cadastro_nome;
    private javax.swing.JLabel lbl_cadastro_senha;
    private javax.swing.JLabel lbl_cadastro_user;
    private javax.swing.JTextField txt_cadastro_nome;
    private javax.swing.JPasswordField txt_cadastro_senha;
    private javax.swing.JTextField txt_cadastro_user;
    // End of variables declaration//GEN-END:variables
}
