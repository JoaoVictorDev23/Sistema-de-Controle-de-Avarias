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
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author joao.faria
 */
public class SubTelaRelatorio extends javax.swing.JInternalFrame {

    Connection conexao = null;

    /**
     * Creates new form SubTelaRelatorio
     */
    public SubTelaRelatorio() {
        initComponents();
        conexao = ModuloConexao.conector();
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

                String datainicial = txt_datainicial.getText();
                String datafinal = txt_datafinal.getText();

                if (txt_datainicial.getText().isEmpty() || txt_datainicial.getText().isEmpty()) {

                } else {
                    Map<String, Object> parametros = new HashMap<>();
                    parametros.put("datainicial", datainicial);
                    parametros.put("datafinal", datafinal);

                    JasperPrint print = JasperFillManager.fillReport("C:\\Sistema de Avarias\\Relatorio\\RelatorioArmazem.jasper", parametros, conexao);
                    // Verifica se o relatório gerou páginas
                    if (print.getPages().isEmpty()) {
                        // Se não houver páginas, exibe uma mensagem personalizada
                        JOptionPane.showMessageDialog(null, "Armazém não localizado!");
                    } else {
                        // Se houver páginas, exibe o JasperViewer
                        JasperViewer.viewReport(print, false);
                    }
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

        jSeparator1 = new javax.swing.JSeparator();
        lbl_relatorio_dtinicial = new javax.swing.JLabel();
        lbl_relatorio_dtfinal = new javax.swing.JLabel();
        txt_datainicial = new javax.swing.JFormattedTextField();
        txt_datafinal = new javax.swing.JFormattedTextField();
        btn_gerar = new javax.swing.JButton();

        setClosable(true);
        setResizable(true);
        setTitle("Relátorio Armazém");

        lbl_relatorio_dtinicial.setText("Data Inicial:");

        lbl_relatorio_dtfinal.setText("Data Final:");

        txt_datainicial.setColumns(10);
        try {
            txt_datainicial.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        txt_datafinal.setColumns(10);
        try {
            txt_datafinal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        btn_gerar.setText("Gerar");
        btn_gerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_gerarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_relatorio_dtfinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(47, 47, 47)
                        .addComponent(txt_datafinal, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_relatorio_dtinicial)
                        .addGap(46, 46, 46)
                        .addComponent(txt_datainicial, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(93, 93, 93))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_gerar)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_datainicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_relatorio_dtinicial))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_datafinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_relatorio_dtfinal))
                .addGap(18, 18, 18)
                .addComponent(btn_gerar)
                .addGap(17, 17, 17))
        );

        setBounds(340, 250, 349, 198);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_gerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_gerarActionPerformed
        gerar_relatorio_armazem();
    }//GEN-LAST:event_btn_gerarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_gerar;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbl_relatorio_dtfinal;
    private javax.swing.JLabel lbl_relatorio_dtinicial;
    private javax.swing.JFormattedTextField txt_datafinal;
    private javax.swing.JFormattedTextField txt_datainicial;
    // End of variables declaration//GEN-END:variables
}
