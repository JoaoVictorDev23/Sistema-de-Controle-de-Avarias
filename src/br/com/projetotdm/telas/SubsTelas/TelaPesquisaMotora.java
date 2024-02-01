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
package br.com.projetotdm.telas.SubsTelas;

import br.com.projetotdm.DAO.ModuloConexao;
import br.com.projetotdm.telas.TelasCadastros.TelaAvarias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author joao.faria
 */
public class TelaPesquisaMotora extends javax.swing.JFrame {


Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    private TelaAvarias telaAvarias;

    /**
     * Creates new form TelaPesquisarMotora
     */
    public TelaPesquisaMotora() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    public void setTelaAvariasMotora(TelaAvarias telaAvarias) {
        this.telaAvarias = telaAvarias;

    }

    /**
     * metodo para buscar dados
     */

    private void buscar() {

        String instru_sql = "select motorista_id as ID, motorista_nome as Nome, motorista_cpf as CPF, motorista_ender as Endereço, motorista_fone as Telefone, motorista_email as Email from tbmotorista where motorista_nome like ?";

        try {
            pst = conexao.prepareCall(instru_sql);
            pst.setString(1, txt_motorista_buscar.getText() + "%");
            rs = pst.executeQuery();

            // biblioteca rs2xml.jar para preencher as tabelas
            tbl_motorista_produtos.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            System.out.println("Ocorreu um erro:" + e);
        }

    }
        /**
     * Metodo para adicionar 
     */

    public void adicionar_motorista() {
        int selectedRow = tbl_motorista_produtos.getSelectedRow();

        if (selectedRow != -1) {  
            

            String id = tbl_motorista_produtos.getValueAt(selectedRow, 0).toString();
            String nome = tbl_motorista_produtos.getValueAt(selectedRow, 1).toString();  
            telaAvarias.setDadosMotoraTelaAvarias(id, nome);
            dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um motorista antes de adicionar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_motorista_buscar = new javax.swing.JLabel();
        txt_motorista_buscar = new javax.swing.JTextField();
        btn_motorista_buscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_motorista_produtos = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/br/com/projetotdm/icones/logotdms.png")).getImage());
        setResizable(false);

        lbl_motorista_buscar.setText("Busca de motorista");

        txt_motorista_buscar.setColumns(25);
        txt_motorista_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_motorista_buscarActionPerformed(evt);
            }
        });
        txt_motorista_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_motorista_buscarKeyPressed(evt);
            }
        });

        btn_motorista_buscar.setText("Buscar");
        btn_motorista_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_motorista_buscarActionPerformed(evt);
            }
        });

        tbl_motorista_produtos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;

            }
        };
        tbl_motorista_produtos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "CPF", "Endereço", "Telefone", "Email"
            }
        ));
        tbl_motorista_produtos.setFocusable(false);
        tbl_motorista_produtos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbl_motorista_produtos);

        jButton1.setText("Adicionar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(lbl_motorista_buscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_motorista_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(btn_motorista_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_motorista_buscar)
                    .addComponent(txt_motorista_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_motorista_buscar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jButton1))
        );

        setSize(new java.awt.Dimension(620, 274));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_motorista_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_motorista_buscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_motorista_buscarActionPerformed

    private void txt_motorista_buscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_motorista_buscarKeyPressed

        if (evt.getKeyCode() == evt.VK_ENTER) {
            // Chama a função f() quando a tecla Enter é pressionada
            buscar();
        }
    }//GEN-LAST:event_txt_motorista_buscarKeyPressed

    private void btn_motorista_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_motorista_buscarActionPerformed
        // TODO add your handling code here:
        buscar();
    }//GEN-LAST:event_btn_motorista_buscarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        adicionar_motorista();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPesquisaMotora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPesquisaMotora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPesquisaMotora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPesquisaMotora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPesquisaMotora().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_motorista_buscar;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_motorista_buscar;
    private javax.swing.JTable tbl_motorista_produtos;
    private javax.swing.JTextField txt_motorista_buscar;
    // End of variables declaration//GEN-END:variables
}
