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
package br.com.projetotdm.telas.TelasPrincipais;

import br.com.projetotdm.telas.TelasCadastros.TelaAvarias;
import br.com.projetotdm.telas.TelasCadastros.TelaCadastroUser;
import br.com.projetotdm.telas.TelasCadastros.TelaProduto;
import br.com.projetotdm.telas.TelasCadastros.TelaCliente;
import br.com.projetotdm.telas.SubsTelas.TelaBuscaNFD;
import br.com.projetotdm.telas.SubsTelas.SubTelaRelatorio3;
import br.com.projetotdm.telas.SubsTelas.SubTelaRelatorio;
import br.com.projetotdm.telas.SubsTelas.SubTelaRelatorio2;
import br.com.projetotdm.DAO.ModuloConexao;
import br.com.projetotdm.telas.SubsTelas.SubTelaRelatorio1;
import br.com.projetotdm.telas.SubsTelas.TelaPesquisaMotora;
import br.com.projetotdm.telas.TelasCadastros.TelaCadastroClientes;
import br.com.projetotdm.telas.TelasCadastros.TelaCadastroMotora;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.util.*;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Tela Principal.
 *
 * @author João Victor Souza de Faria
 * @version 1.1
 *
 */
public class TelaPrincipal extends javax.swing.JFrame {

    Connection conexao = null;
    private static String usuariologado;

    /**
     * Creates new form TelaPrincipal
     */
    public TelaPrincipal(String usuariologado) {
        initComponents();
        conexao = ModuloConexao.conector();
        this.usuariologado = usuariologado;
        

    }


    /**
     * Metodo para gerar Relatorio Avaria
     */
    public void gerar_relatorio_avarias() {
        // Relatorio Geral
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a geração deste relatório?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {

            //IMPRIMINDO O RELATORIO COM O jasper
            try {

                String n_nf = JOptionPane.showInputDialog("Digite o número da NFe da Avaria");
                Map<String, Object> parametros = new HashMap<>();
                parametros.put("NF", n_nf);

                JasperPrint print = JasperFillManager.fillReport("C:\\Sistema de Avarias\\Relatorio\\RelatorioGeral.jasper", parametros, conexao);
                // Verifica se o relatório gerou páginas
                if (print.getPages().isEmpty()) {
                    // Se não houver páginas, exibe uma mensagem personalizada
                    JOptionPane.showMessageDialog(null, "Você pesquisou um número de NFe não cadastrada!");
                } else {
                    // Se houver páginas, exibe o JasperViewer
                    JasperViewer.viewReport(print, false);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }
        public void gerar_relatorio_geral() {
        // Relatorio Geral
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a geração deste relatório?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {

            //IMPRIMINDO O RELATORIO COM O jasper
            try {

                

                JasperPrint print = JasperFillManager.fillReport("C:\\Sistema de Avarias\\Relatorio\\RELATORIO-geral.jasper", null, conexao);
                // Verifica se o relatório gerou páginas
                if (print.getPages().isEmpty()) {
                    // Se não houver páginas, exibe uma mensagem personalizada
                    JOptionPane.showMessageDialog(null, "Erro!!");
                } else {
                    // Se houver páginas, exibe o JasperViewer
                    JasperViewer.viewReport(print, false);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }

    /**
     * Metodo para Relatorio do Armazem
     */
    public void gerar_relatorio_armazem() {
        // Relatorio Geral
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a geração deste relatório?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {

            //IMPRIMINDO O RELATORIO COM O jasper
            try {

                String estoque = JOptionPane.showInputDialog("Digite o nome do Armazém");
                Map<String, Object> parametros = new HashMap<>();
                parametros.put("armazem", estoque);

                JasperPrint print = JasperFillManager.fillReport("C:\\Sistema de Avarias\\Relatorio\\Relatorio - Filtro - NF.jasper", parametros, conexao);
                // Verifica se o relatório gerou páginas
                if (print.getPages().isEmpty()) {
                    // Se não houver páginas, exibe uma mensagem personalizada
                    JOptionPane.showMessageDialog(null, "Armazém não localizado!");
                } else {
                    // Se houver páginas, exibe o JasperViewer
                    JasperViewer.viewReport(print, false);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
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

        Desktop = new javax.swing.JDesktopPane();
        txtLogo = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        lblData = new javax.swing.JLabel();
        lbl_principal_bemvindo = new javax.swing.JLabel();
        Menu = new javax.swing.JMenuBar();
        menucadastros = new javax.swing.JMenu();
        Menucliente = new javax.swing.JMenuItem();
        Menuusuario = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        menunf = new javax.swing.JMenuItem();
        menuavaria = new javax.swing.JMenuItem();
        menupesquisas = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        menurelatorios = new javax.swing.JMenu();
        menuservico = new javax.swing.JMenuItem();
        menuclientes = new javax.swing.JMenuItem();
        menustatus = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        geral_data = new javax.swing.JMenuItem();
        menuajuda = new javax.swing.JMenu();
        menusobre = new javax.swing.JMenuItem();
        menuopcoes = new javax.swing.JMenu();
        menusair = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Controle de Avarias");
        setIconImage(new ImageIcon(getClass().getResource("/br/com/projetotdm/icones/logotdms.png")).getImage());
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        Desktop.setPreferredSize(new java.awt.Dimension(800, 700));

        javax.swing.GroupLayout DesktopLayout = new javax.swing.GroupLayout(Desktop);
        Desktop.setLayout(DesktopLayout);
        DesktopLayout.setHorizontalGroup(
            DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1055, Short.MAX_VALUE)
        );
        DesktopLayout.setVerticalGroup(
            DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        txtLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/projetotdm/icones/tdmtransporte.png"))); // NOI18N

        lblUsuario.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblUsuario.setText("Usuário:");

        lblData.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblData.setText("Data:");

        lbl_principal_bemvindo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbl_principal_bemvindo.setText("Seja bem-vindo,");

        menucadastros.setText("Cadastro");

        Menucliente.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        Menucliente.setText("Comprador");
        Menucliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuclienteActionPerformed(evt);
            }
        });
        menucadastros.add(Menucliente);

        Menuusuario.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_MASK));
        Menuusuario.setText("Usuário");
        Menuusuario.setEnabled(false);
        Menuusuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuusuarioActionPerformed(evt);
            }
        });
        menucadastros.add(Menuusuario);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem3.setText("Motorista");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        menucadastros.add(jMenuItem3);

        jMenuItem4.setText("Cliente");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        menucadastros.add(jMenuItem4);

        menunf.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK));
        menunf.setText("Produtos");
        menunf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menunfActionPerformed(evt);
            }
        });
        menucadastros.add(menunf);

        menuavaria.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        menuavaria.setText("Avaria");
        menuavaria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuavariaActionPerformed(evt);
            }
        });
        menucadastros.add(menuavaria);

        Menu.add(menucadastros);

        menupesquisas.setText("Pesquisar");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Pesquisar Devoluções");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        menupesquisas.add(jMenuItem1);

        Menu.add(menupesquisas);

        menurelatorios.setText("Relatorios");

        menuservico.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK));
        menuservico.setText("Armazém");
        menuservico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuservicoActionPerformed(evt);
            }
        });
        menurelatorios.add(menuservico);

        menuclientes.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        menuclientes.setText("NFD");
        menuclientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuclientesActionPerformed(evt);
            }
        });
        menurelatorios.add(menuclientes);

        menustatus.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        menustatus.setText("Status");
        menustatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menustatusActionPerformed(evt);
            }
        });
        menurelatorios.add(menustatus);

        jMenuItem2.setText("Filial/Serie/CTE");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        menurelatorios.add(jMenuItem2);

        geral_data.setText("Geral");
        geral_data.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                geral_dataActionPerformed(evt);
            }
        });
        menurelatorios.add(geral_data);

        Menu.add(menurelatorios);

        menuajuda.setText("Ajuda");

        menusobre.setText("Sobre");
        menusobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menusobreActionPerformed(evt);
            }
        });
        menuajuda.add(menusobre);

        Menu.add(menuajuda);

        menuopcoes.setText("Opções");

        menusair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        menusair.setText("Sair");
        menusair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menusairActionPerformed(evt);
            }
        });
        menuopcoes.add(menusair);

        Menu.add(menuopcoes);

        setJMenuBar(Menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Desktop, javax.swing.GroupLayout.PREFERRED_SIZE, 1055, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(lblData)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lblUsuario))
                            .addComponent(lbl_principal_bemvindo, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(430, Short.MAX_VALUE)
                .addComponent(lbl_principal_bemvindo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblUsuario)
                .addGap(139, 139, 139)
                .addComponent(lblData)
                .addGap(38, 38, 38)
                .addComponent(txtLogo))
            .addGroup(layout.createSequentialGroup()
                .addComponent(Desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        java.util.Date data = new java.util.Date();
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.LONG);
        lblData.setText(formatador.format(data));


    }//GEN-LAST:event_formWindowActivated

    private void menusairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menusairActionPerformed
        // TODO add your handling code here:
        int sair = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja sair?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (sair == JOptionPane.YES_OPTION) {
            System.exit(0);
        }

    }//GEN-LAST:event_menusairActionPerformed

    private void menusobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menusobreActionPerformed
        // TODO add your handling code here:
        //chama a tela sobre
        TelaSobre sobre = new TelaSobre();

        sobre.setVisible(true);


    }//GEN-LAST:event_menusobreActionPerformed

    private void MenuusuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuusuarioActionPerformed
        // TODO add your handling code here:
        TelaCadastroUser usuario = new TelaCadastroUser(usuariologado);
        usuario.setVisible(true);
        Desktop.add(usuario);
    }//GEN-LAST:event_MenuusuarioActionPerformed

    private void MenuclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuclienteActionPerformed
        TelaCliente cliente = new TelaCliente(usuariologado);
        cliente.setVisible(true);
        Desktop.add(cliente);
    }//GEN-LAST:event_MenuclienteActionPerformed

    private void menuavariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuavariaActionPerformed

        TelaAvarias avaria = null;
        try {
            avaria = new TelaAvarias(usuariologado);
        } catch (Exception ex) {
            Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        avaria.setVisible(true);
        Desktop.add(avaria);
    }//GEN-LAST:event_menuavariaActionPerformed

    private void menunfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menunfActionPerformed
        // TODO add your handling code here:
        TelaProduto produto = new TelaProduto(usuariologado);
        produto.setVisible(true);
        Desktop.add(produto);
    }//GEN-LAST:event_menunfActionPerformed

    private void menuclientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuclientesActionPerformed
        gerar_relatorio_avarias();

    }//GEN-LAST:event_menuclientesActionPerformed

    private void menuservicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuservicoActionPerformed
        SubTelaRelatorio relatorio = new SubTelaRelatorio();
        relatorio.setVisible(true);
        Desktop.add(relatorio);

    }//GEN-LAST:event_menuservicoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        TelaBuscaNFD telanfd = new TelaBuscaNFD();
        telanfd.setVisible(true);
        Desktop.add(telanfd);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void menustatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menustatusActionPerformed
        SubTelaRelatorio2 telastatus = new SubTelaRelatorio2();
        telastatus.setVisible(true);
        Desktop.add(telastatus);
    }//GEN-LAST:event_menustatusActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        SubTelaRelatorio3 telacte = new SubTelaRelatorio3();
        telacte.setVisible(true);
        Desktop.add(telacte);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        TelaCadastroMotora motora = new TelaCadastroMotora(usuariologado);
        motora.setVisible(true);
        Desktop.add(motora);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        TelaCadastroClientes cliente = new TelaCadastroClientes(usuariologado);
        cliente.setVisible(true);
        Desktop.add(cliente);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void geral_dataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_geral_dataActionPerformed
        SubTelaRelatorio1 relatoriogeral = new SubTelaRelatorio1();
        relatoriogeral.setVisible(true);
        Desktop.add(relatoriogeral);
    }//GEN-LAST:event_geral_dataActionPerformed

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
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal(usuariologado).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane Desktop;
    private javax.swing.JMenuBar Menu;
    private javax.swing.JMenuItem Menucliente;
    public static javax.swing.JMenuItem Menuusuario;
    private javax.swing.JMenuItem geral_data;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JLabel lblData;
    public static javax.swing.JLabel lblUsuario;
    public static javax.swing.JLabel lbl_principal_bemvindo;
    private javax.swing.JMenu menuajuda;
    private javax.swing.JMenuItem menuavaria;
    private javax.swing.JMenu menucadastros;
    private javax.swing.JMenuItem menuclientes;
    private javax.swing.JMenuItem menunf;
    private javax.swing.JMenu menuopcoes;
    private javax.swing.JMenu menupesquisas;
    public static javax.swing.JMenu menurelatorios;
    private javax.swing.JMenuItem menusair;
    private javax.swing.JMenuItem menuservico;
    private javax.swing.JMenuItem menusobre;
    private javax.swing.JMenuItem menustatus;
    private javax.swing.JLabel txtLogo;
    // End of variables declaration//GEN-END:variables
}
