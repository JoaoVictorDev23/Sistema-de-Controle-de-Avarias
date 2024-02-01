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

import br.com.projetotdm.DAO.ModuloConexao;
import br.com.projetotdm.telas.SubsTelas.TelaPesquisaMotora;
import br.com.projetotdm.telas.SubsTelas.TelaPesquisarCli;
import br.com.projetotdm.telas.SubsTelas.TelaPesquisarFornecedor;
import br.com.projetotdm.telas.SubsTelas.TelaPesquisarProd;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import javax.swing.*;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Tela Avarias
 *
 * @author João Victor Souza de Faria
 * @version 1.1
 *
 */
public class TelaAvarias extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private static String usuariologado;
    String idmotorista = "1";
    String idcliente = "2";
    String idfornecedor = "3";

    /**
     * Inicialização da Tela Avaria
     */
    public TelaAvarias(String usuariologado) throws Exception {
        conexao = ModuloConexao.conector();
        initComponents();

        String dataAtual = obterDataAtual();
        txt_avarias_data.setText(dataAtual);
        btn_avarias_update.setEnabled(false);

        jComboBox1.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                jComboBox1.showPopup();
            }
        });
        this.usuariologado = usuariologado;
        DefaultTableModel model = (DefaultTableModel) tbl_avarias_produtos.getModel();
        model.setRowCount(0);

    }

    /**
     * Função para obter a Data
     *
     * @return data atual do servidor
     * @throws Exception
     */
    public String obterDataAtual() throws Exception {
        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(1000);

        InetAddress hostAddr = InetAddress.getByName("time.google.com"); // Ou qualquer outro servidor NTP confiável
        TimeInfo timeInfo = client.getTime(hostAddr);
        long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();

        // Convertendo para o tipo java.util.Date
        java.util.Date currentDate = new java.util.Date(returnTime);

        // Formatando a data para o formato desejado (opcional)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataAtualFormatada = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter);

        client.close();

        return dataAtualFormatada;
    }

    /**
     * Metodo para Limpar campos
     *
     */
    private void limpar() {
        DefaultTableModel model = (DefaultTableModel) tbl_avarias_produtos.getModel();
        model.setRowCount(0);
        txt_avarias_comprador.setText(null);
        txt_avarias_numero.setText(null);
        box_avarias_status.setSelectedItem("Prejuizo");
        txt_avarias_venda.setText(null);
        jTextArea1.setText(null);
        txt_avaria_desconto.setText(null);
        txt_avaria_prejuizo.setText(null);
//       txt_avaria_cli_id.setText(null);

        idmotorista = "1";
        idcliente = "2";
        idfornecedor = "3";
        txt_avarias_debitado.setText(null);
        txt_valor_emestoque.setText(null);

        txt_avarias_filial.setText(null);
        txt_avarias_cte.setText(null);
        txt_avarias_serie.setText(null);

//        txt_avaria_idmotorista.setText(null);
        txt_avarias_motorista.setText(null);

        txt_usuario_atualizar.setText(null);
        txt_usuario_cadastrar.setText(null);

        txt_avaria_debitadocliente.setText(null);
        box_cliente_debitado.setSelected(false);
        

    }

    /**
     * Metodo para Adicionar uma Avaria no banco de Dados
     *
     * @return true,false
     */
    private boolean adicionar_avaria() {
        // Antes de executar a inserção, verifica os checkboxes

        String instru_sql = "INSERT INTO tbavarias (avaria_numeronfe, avaria_cli_id, avaria_status, avaria_valor_venda, avaria_observacao"
                + ", avaria_valor_prejuizo, avaria_valor_desconto, avaria_motivo, avaria_valor_debitado, avaria_valor_emestoque"
                + ",avaria_cte,avaria_serie,avaria_filial, avaria_data, avaria_motorista_id,"
                + "avaria_usuario_cadastrar,avaria_usuario_atualizar,avaria_debitado_docliente,fornecedor_id,avaria_debitado_sim)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {

            // Condicional que verifica Vazio ou não vazio em áreas obrigatórias.
            if (txt_avarias_numero.getText().isEmpty() || txt_avarias_serie.getText().isEmpty()
                    || txt_avarias_cte.getText().isEmpty() || txt_avarias_filial.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
                return false;
            } else {
                pst = conexao.prepareStatement(instru_sql);
                pst.setString(1, txt_avarias_numero.getText());

                // Condicional para vinculo de cliente, acaso nao tenha vincula um cliente inexistente
//                if (txt_avaria_cli_id.getText().isEmpty()) {
//                    pst.setString(2, "3");
//                } else {
//                    pst.setString(2, txt_avaria_cli_id.getText());
//                }
                pst.setString(2, idcliente);

                pst.setString(3, box_avarias_status.getSelectedItem().toString());

                // Condicional para Preço venda
                if (txt_avarias_venda.getText().isEmpty()) {
                    pst.setString(4, "0.0");
                } else {
                    pst.setString(4, txt_avarias_venda.getText());
                }

                pst.setString(5, jTextArea1.getText());

                // Condicional Prejuizo.
                if (txt_avaria_prejuizo.getText().isEmpty()) {
                    pst.setString(6, "0.0");
                } else {
                    pst.setString(6, txt_avaria_prejuizo.getText());
                }

                // Condicional desconto.
                if (txt_avaria_desconto.getText().isEmpty()) {
                    pst.setString(7, "0.0");
                } else {
                    pst.setString(7, txt_avaria_desconto.getText());
                }

                pst.setString(8, box_avarias_motivo.getSelectedItem().toString());

                //Condicional Debitado
                if (txt_avarias_debitado.getText().isEmpty()) {
                    pst.setString(9, "0.0");
                } else {
                    pst.setString(9, txt_avarias_debitado.getText());
                }

                // Condicional em Estoque.
                if (txt_valor_emestoque.getText().isEmpty()) {
                    pst.setString(10, "0.0");
                } else {
                    pst.setString(10, txt_valor_emestoque.getText());
                }
                //Dados do CTE
                pst.setString(11, txt_avarias_cte.getText());
                //Dados Serie
                pst.setString(12, txt_avarias_serie.getText());
                //Dados Filial
                pst.setString(13, txt_avarias_filial.getText());
                //Data atual pego do Google.
                pst.setString(14, txt_avarias_data.getText());

                // Condicional para vinculo de motorista, acaso nao tenha vincula um cliente inexistente
                pst.setString(15, idmotorista);

                //adicionar usuario na tabela
                pst.setString(16, usuariologado);
                pst.setString(17, "-");

                //adicionar valordebitado do cliente
                if (txt_avaria_debitadocliente.getText().isEmpty()) {
                    pst.setString(18, "0.0");
                } else {

                    pst.setString(18, txt_avaria_debitadocliente.getText());
                }
                //valor do id fonecedor pra armazenagem
                
                pst.setString(19,idfornecedor);
                pst.setBoolean(20,box_cliente_debitado.isSelected());

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Avaria cadastrada com sucesso");
                    return true;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro! Verifique os dados! \n" + e);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Metodo para adicionar na tabela de estoque os valores adicionados na NFe
     */
    private void adicionar_estoque() {
        String instru_sql2 = "INSERT INTO tbestoque_produtos (est_avaria_numeronfe, est_produto_id, est_quantidade, est_armazem, est_produto_situacao, "
                + "est_produto_permissao, est_prod_valor_unitario) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            // Obtém o modelo da tabela
            DefaultTableModel model = (DefaultTableModel) tbl_avarias_produtos.getModel();

            // Percorre as linhas da tabela
            for (int i = 0; i < model.getRowCount(); i++) {
                // Obtém os dados da tabela para cada linha
                String est_produto_id = model.getValueAt(i, 0).toString(); // Coluna 0 é o Produto ID
                String est_produto_valor = model.getValueAt(i,2).toString();//Coluna2 é o valor unitario
                String est_quantidade = model.getValueAt(i, 3).toString(); // Coluna 3 é a Quantidade
                String est_situacao = model.getValueAt(i, 4).toString();// Coluna 4 é a Situação 

                // Obter o valor booleano da coluna 5 (Assumindo que a coluna 5 é do tipo JCheckBox)
                Object value = model.getValueAt(i, 5);
                boolean est_permissao = (value != null) && (boolean) value;

                // Preparar a instrução SQL
                pst = conexao.prepareStatement(instru_sql2);
                pst.setString(1, txt_avarias_numero.getText());
                pst.setString(2, est_produto_id);
                pst.setString(3, est_quantidade);
                pst.setString(4, box_avarias_armazem.getSelectedItem().toString());
                pst.setString(5, est_situacao);
                pst.setBoolean(6, est_permissao);
                pst.setString(7, est_produto_valor);

                // Executar a instrução SQL
                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar produtos: " + e.getMessage());
        } finally {
            // Certifique-se de fechar a PreparedStatement no bloco finally
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void adicionar_armazem() {
        String instru_sql = "INSERT INTO tbarmazem (armazem_nome, armazem_quant, armazem_produto_id, armazem_produto_valor, armazem_avaria_numeronf) VALUES (?, ?, ?, ?, ?)";
        int adicionado = 0;

        try {
            // Obtém o modelo da tabela
            DefaultTableModel model = (DefaultTableModel) tbl_avarias_produtos.getModel();

            // Percorre as linhas da tabela
            for (int i = 0; i < model.getRowCount(); i++) {
                // Obtém os dados da tabela para cada linha
                String est_produto_id = model.getValueAt(i, 0).toString(); // Coluna 0 é o Produto ID
                String est_produto_valor = model.getValueAt(i, 2).toString(); // Coluna 2 é o valor unitário
                String est_quantidade = model.getValueAt(i, 3).toString(); // Coluna 3 é a Quantidade
                String est_situacao = model.getValueAt(i, 4).toString(); // Coluna 4 é a Situação

                // Verifica se a situação é "Em estoque"
                if ("Em estoque".equals(est_situacao)) {
                    pst = conexao.prepareStatement(instru_sql);
                    pst.setString(1, box_avarias_armazem.getSelectedItem().toString());
                    pst.setString(2, est_quantidade);
                    pst.setString(3, est_produto_id);
                    pst.setString(4, est_produto_valor);
                    pst.setString(5, txt_avarias_numero.getText());

                    // Execute a instrução SQL
                    adicionado = pst.executeUpdate();
                }
            }

            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Armazém atualizado com sucesso");
            }// else {
            // JOptionPane.showMessageDialog(null, "Nenhum produto foi adicionado!");
            // }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar produtos: " + e.getMessage());
        }
    }

    private boolean atualizarAvaria() {
        String instru_sql = "update tbavarias set avaria_cli_id=?, avaria_status=?, avaria_valor_venda=?, avaria_observacao=?, avaria_valor_prejuizo=?, "
                + "avaria_valor_desconto=?, avaria_motivo=?, avaria_valor_debitado=?, avaria_valor_emestoque=?, avaria_cte=?, "
                + "avaria_serie=?, avaria_filial=?, avaria_motorista_id=?,avaria_usuario_atualizar=?,avaria_debitado_docliente=?,fornecedor_id=?,avaria_debitado_sim=?"
                + " where avaria_numeronfe=?";

        try {
            if (txt_avarias_numero.getText().isEmpty() || txt_avarias_venda.getText().isEmpty()
                    || txt_avarias_serie.getText().isEmpty() || txt_avarias_cte.getText().isEmpty() || txt_avarias_filial.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
                return false;
            } else {

                pst = conexao.prepareStatement(instru_sql);
                pst.setString(1, idcliente);

//                pst.setString(1, txt_avaria_cli_id.getText());
                pst.setString(2, box_avarias_status.getSelectedItem().toString());

// Converter txt_avarias_venda para numeric
                String valorVendaTexto = txt_avarias_venda.getText();
                double valorVenda = valorVendaTexto.isEmpty() ? 0 : Double.parseDouble(valorVendaTexto);
                pst.setDouble(3, valorVenda);

                pst.setString(4, jTextArea1.getText());

// Converter txt_avaria_prejuizo para numeric
                String valorPrejuizoTexto = txt_avaria_prejuizo.getText();
                double valorPrejuizo = valorPrejuizoTexto.isEmpty() ? 0 : Double.parseDouble(valorPrejuizoTexto);
                pst.setDouble(5, valorPrejuizo);

// Converter txt_avaria_desconto para numeric
                String valorDescontoTexto = txt_avaria_desconto.getText();
                double valorDesconto = valorDescontoTexto.isEmpty() ? 0 : Double.parseDouble(valorDescontoTexto);
                pst.setDouble(6, valorDesconto);

                pst.setString(7, box_avarias_motivo.getSelectedItem().toString());

// Converter txt_avarias_debitado para numeric
                String valorDebitadoTexto = txt_avarias_debitado.getText();
                double valorDebitado = valorDebitadoTexto.isEmpty() ? 0 : Double.parseDouble(valorDebitadoTexto);
                pst.setDouble(8, valorDebitado);

// Converter txt_valor_emestoque para numeric
                String valorEmEstoqueTexto = txt_valor_emestoque.getText();
                double valorEmEstoque = valorEmEstoqueTexto.isEmpty() ? 0 : Double.parseDouble(valorEmEstoqueTexto);
                pst.setDouble(9, valorEmEstoque);

                pst.setString(10, txt_avarias_cte.getText());
                pst.setString(11, txt_avarias_serie.getText());
                pst.setString(12, txt_avarias_filial.getText());
                
                String idmotoristaTexto = idmotorista;
                String idmotoristaFormat = (idmotoristaTexto == null || idmotoristaTexto.isEmpty()) ? "1" : idmotoristaTexto;
                pst.setString(13, idmotoristaFormat);
                
                pst.setString(14, usuariologado);

                String valordebitadodocliente = txt_avaria_debitadocliente.getText();
                double valordebitadodocliente2 = valordebitadodocliente.isEmpty() ? 0 : Double.parseDouble(valordebitadodocliente);
                pst.setDouble(15, valordebitadodocliente2);
                
                String idfornecedorTexto = idfornecedor;
                String idfornecedorFormat = (idfornecedorTexto == null || idfornecedorTexto.isEmpty()) ? "3" : idfornecedorTexto;

                pst.setString(16, idfornecedorFormat);
                
                pst.setBoolean(17,box_cliente_debitado.isSelected());


                pst.setString(18, txt_avarias_numero.getText());

// Restante do código...
                int atualizado = pst.executeUpdate();

                if (atualizado > 0) {
                    JOptionPane.showMessageDialog(null, "Avaria atualizada com sucesso");
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Avaria não atualizada.");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro na atualização da Avaria! Verifique os dados! " + e.getMessage());
            e.printStackTrace(); // Isso imprime detalhes do erro no console para fins de depuração
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void atualizarEstoque() {
        String instruSqlDelete = "DELETE FROM tbestoque_produtos WHERE est_avaria_numeronfe = ? ";

        try {

            pst = conexao.prepareStatement(instruSqlDelete);
            pst.setString(1, txt_avarias_numero.getText());
            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir os registros do estoque: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * Metodo para buscar Dados no Banco de Dados
     */
    public class ComboBoxCellEditor extends DefaultCellEditor {

        public ComboBoxCellEditor(String[] items) {
            super(new JComboBox<>(items));
        }
    }

    private void buscarcte() {
        
        

        String instru_sql = "select * from tbavarias where avaria_cte=? and avaria_serie=? and avaria_filial=?";
        String instru_sql_2 = "select * from tbclientes where cli_id=?";
        String instru_sql_motorista = "select*from tbmotorista where motorista_id=?"; 
        String instru_sql_fornecedor = "select*from tbfornecedor where fornecedor_id=?";
        String instru_sql_3 = "select * from tbestoque_produtos where est_avaria_numeronfe=?";
        String instru_sql_4 = "SELECT p.produto_id AS ID, p.produto_nome AS Nome, e.est_prod_valor_unitario AS Valor, e.est_quantidade AS Quantidade,"
                + " e.est_produto_situacao AS Situação, e.est_produto_permissao AS NaopermiteVenda \n"
                + "FROM tbproduto p\n"
                + "JOIN tbestoque_produtos e ON p.produto_id = e.est_produto_id\n"
                + "WHERE e.est_avaria_numeronfe = ?;";


        try {
            if (txt_avarias_cte.getText().isEmpty() || txt_avarias_filial.getText().isEmpty() || txt_avarias_serie.getText().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Digite todos os três campos (FILIAL,SERIE,CTE) para realizar a busca!");
            } else {
                // Consulta 1 - Realiza a consulta na tabela avarias
                pst = conexao.prepareStatement(instru_sql);
                pst.setString(1, txt_avarias_cte.getText());
                pst.setString(2, txt_avarias_serie.getText());
                pst.setString(3, txt_avarias_filial.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    txt_avarias_numero.setText(rs.getString(1));
                    idcliente = rs.getString(2);
//                    txt_avaria_cli_id.setText(rs.getString(2));
                    box_avarias_status.setSelectedItem(rs.getString(3));
                    txt_avarias_venda.setText(rs.getString(4));
                    jTextArea1.setText(rs.getString(5));
                    txt_avaria_prejuizo.setText(rs.getString(6));
                    txt_avaria_desconto.setText(rs.getString(7));
                    box_avarias_motivo.setSelectedItem(rs.getString(8));
                    txt_avarias_debitado.setText(rs.getString(9));
                    txt_valor_emestoque.setText(rs.getString(10));
                    idmotorista = rs.getString(15);
//                    txt_avaria_idmotorista.setText(rs.getString(15));
                    idfornecedor = rs.getString(16);
                    txt_usuario_cadastrar.setText(rs.getString(17));
                    txt_usuario_atualizar.setText(rs.getString(18));
                    txt_avaria_debitadocliente.setText(rs.getString(19));
                    box_cliente_debitado.setSelected(rs.getBoolean(20));
                    pst.close();

                    // Consulta 2 - Realiza uma consulta na tabela clientes
                    pst = conexao.prepareStatement(instru_sql_2);
                    pst.setString(1, idcliente);
//                        pst.setString(1, txt_avaria_cli_id.getText());
                    ResultSet rsClientes = pst.executeQuery();
                    if (rsClientes.next()) {
                        txt_avarias_comprador.setText(rsClientes.getString(2));
                        pst.close();

                        // Consulta 3 - Realiza uma consulta na tabela Produtos_Controle
                        pst = conexao.prepareStatement(instru_sql_3);
                        pst.setString(1, txt_avarias_numero.getText());
                        ResultSet rsEstoque = pst.executeQuery();

                        if (rsEstoque.next()) {

                            box_avarias_armazem.setSelectedItem(rsEstoque.getString(5));

                            pst.close();
                            // ...

                            // Consulta 4 - Realiza uma consulta na tabela produto/também na tabela estoque
                            pst = conexao.prepareStatement(instru_sql_4);
                            pst.setString(1, txt_avarias_numero.getText());
                            ResultSet rsProdutos = pst.executeQuery();

                            // Criar um modelo de tabela para a JTable
                            DefaultTableModel tableModel = new DefaultTableModel() {
                                @Override
                                public Class<?> getColumnClass(int columnIndex) {
                                    // Retornar a classe JComboBox para a coluna "Situação"
                                    if (getColumnName(columnIndex).equals("Situação")) {
                                        return JComboBox.class;
                                    } else if (getColumnName(columnIndex).equals("Não Permite Venda")) {
                                        return Boolean.class; // Definir a classe para a coluna booleana "Não Permite Venda"
                                    }
                                    return super.getColumnClass(columnIndex);
                                }
                                    @Override
                                public boolean isCellEditable(int row, int column) {
                                    // Torna as colunas "ID" e "Nome" não editáveis
                                    return !(getColumnName(column).equals("ID") || getColumnName(column).equals("Nome"));
                                }
                            };

                            // Adicionar colunas ao modelo
                            tableModel.addColumn("ID");
                            tableModel.addColumn("Nome");
                            tableModel.addColumn("Valor");
                            tableModel.addColumn("Quantidade");
                            tableModel.addColumn("Situação");
                            tableModel.addColumn("Não Permite Venda"); // Nova coluna booleana

                            // Adicionar linhas ao modelo com os dados do ResultSet
                            while (rsProdutos.next()) {
                                Object[] rowData = {
                                    rsProdutos.getString("ID"),
                                    rsProdutos.getString("Nome"),
                                    (rsProdutos.getObject("Valor") != null) ? rsProdutos.getString("Valor") : "0.0",
                                    rsProdutos.getString("Quantidade"),
                                    rsProdutos.getString("Situação"),
                                   (rsProdutos.getObject("NaopermiteVenda") != null) ? rsProdutos.getBoolean("NaopermiteVenda") : false
                                };
                                tableModel.addRow(rowData);
                            }

                            // Configurar a JTable com o modelo de tabela
                            tbl_avarias_produtos.setModel(tableModel);

                            // Configurar o editor personalizado para a coluna "Situação"
                            int situacaoColumnIndex = tableModel.findColumn("Situação");
                            tbl_avarias_produtos.getColumnModel().getColumn(situacaoColumnIndex).setCellEditor(new ComboBoxCellEditor(new String[]{"Venda", "Prejuizo", "Em estoque", "Descarte", "Devolução"}));

                            // Configurar o editor personalizado para a coluna "Não Permite Venda"
                            int naoPermiteVendaColumnIndex = tableModel.findColumn("Não Permite Venda");
                            tbl_avarias_produtos.getColumnModel().getColumn(naoPermiteVendaColumnIndex).setCellEditor(new DefaultCellEditor(new JCheckBox()));

                            // Fechar o ResultSet e o PreparedStatement
                            rsProdutos.close();
                            pst.close();

                            // Consulta Motorista - Realiza uma consulta na tabela clientes
                            pst = conexao.prepareStatement(instru_sql_motorista);
                            pst.setString(1, idmotorista);//txt_avaria_idmotorista.getText());
                            ResultSet rsMotorista = pst.executeQuery();

                            if (rsMotorista.next()) {
                                txt_avarias_motorista.setText(rsMotorista.getString(2));
                                pst.close();
                                btn_avarias_update.setEnabled(true);
                            }

                            //Consulta Tabela Fornecedor(cliente)
                            pst = conexao.prepareStatement(instru_sql_fornecedor);
                            pst.setString(1, idfornecedor);
                            ResultSet rsFornecedor = pst.executeQuery();
                            if (rsFornecedor.next()) {
                                txt_avaria_cliente.setText(rsFornecedor.getString(2));
                                pst.close();
                            }

                        }

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Avaria não encontrado.");
//                        txt_avaria_cli_id.setText(null);
                    idmotorista = "1";
                    idcliente = "2";
                    idfornecedor = "3";
                    txt_avarias_comprador.setText(null);
                    txt_avarias_numero.setText(null);
                    txt_avarias_venda.setText(null);
                    jTextArea1.setText(null);
                    box_avarias_status.setSelectedItem("Prejuizo");
                    txt_avaria_prejuizo.setText(null);
                    txt_avaria_desconto.setText(null);
                    DefaultTableModel model = (DefaultTableModel) tbl_avarias_produtos.getModel();
                    model.setRowCount(0);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * metodo de busca para preenchimento de dados na tela.
     *
     */

    private void buscar() {
        String instru_sql = "select * from tbavarias where avaria_numeronfe=?";
        String instru_sql_2 = "select * from tbclientes where cli_id=?";
        String instru_sql_motorista = "select*from tbmotorista where motorista_id=?";
        String instru_sql_fornecedor = "select*from tbfornecedor where fornecedor_id=?";
        String instru_sql_3 = "select * from tbestoque_produtos where est_avaria_numeronfe=?";
        String instru_sql_4 = "SELECT p.produto_id AS ID, p.produto_nome AS Nome, e.est_prod_valor_unitario AS Valor, e.est_quantidade AS Quantidade,"
                + " e.est_produto_situacao AS Situação, e.est_produto_permissao AS NaopermiteVenda \n"
                + "FROM tbproduto p\n"
                + "JOIN tbestoque_produtos e ON p.produto_id = e.est_produto_id\n"
                + "WHERE e.est_avaria_numeronfe = ?;";

        try {
            // Consulta 1 - Realiza a consulta na tabela avarias
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, txt_avarias_numero.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
//                txt_avaria_cli_id.setText(rs.getString(2));
                idcliente = rs.getString(2);
                box_avarias_status.setSelectedItem(rs.getString(3));
                txt_avarias_venda.setText(rs.getString(4));
                jTextArea1.setText(rs.getString(5));
                txt_avaria_prejuizo.setText(rs.getString(6));
                txt_avaria_desconto.setText(rs.getString(7));
                box_avarias_motivo.setSelectedItem(rs.getString(8));
                txt_avarias_debitado.setText(rs.getString(9));
                txt_valor_emestoque.setText(rs.getString(10));
                txt_avarias_cte.setText(rs.getString(11));
                txt_avarias_serie.setText(rs.getString(12));
                txt_avarias_filial.setText(rs.getString(13));
                idmotorista = rs.getString(15);
//                    txt_avaria_idmotorista.setText(rs.getString(15));
                idfornecedor = rs.getString(16);
                txt_usuario_cadastrar.setText(rs.getString(17));
                txt_usuario_atualizar.setText(rs.getString(18));
                txt_avaria_debitadocliente.setText(rs.getString(19));
                box_cliente_debitado.setSelected(rs.getBoolean(20));
                pst.close();

                // Consulta 2 - Realiza uma consulta na tabela clientes
                pst = conexao.prepareStatement(instru_sql_2);
                pst.setString(1, idcliente);
//                    pst.setString(1, txt_avaria_cli_id.getText());
                ResultSet rsClientes = pst.executeQuery();
                if (rsClientes.next()) {
                    txt_avarias_comprador.setText(rsClientes.getString(2));
                    pst.close();

                    // Consulta 3 - Realiza uma consulta na tabela Produtos_Controle
                    pst = conexao.prepareStatement(instru_sql_3);
                    pst.setString(1, txt_avarias_numero.getText());
                    ResultSet rsEstoque = pst.executeQuery();

                    if (rsEstoque.next()) {

                        box_avarias_armazem.setSelectedItem(rsEstoque.getString(5));

                        pst.close();
                        // ...

                        // Consulta 4 - Realiza uma consulta na tabela produto/também na tabela estoque
                        pst = conexao.prepareStatement(instru_sql_4);
                        pst.setString(1, txt_avarias_numero.getText());
                        ResultSet rsProdutos = pst.executeQuery();

// Criar um modelo de tabela para a JTable
                        DefaultTableModel tableModel = new DefaultTableModel() {
                            @Override
                            public Class<?> getColumnClass(int columnIndex) {
                                // Retornar a classe JComboBox para a coluna "Situação"
                                if (getColumnName(columnIndex).equals("Situação")) {
                                    return JComboBox.class;
                                } else if (getColumnName(columnIndex).equals("Não Permite Venda")) {
                                    return Boolean.class; // Definir a classe para a coluna booleana "Não Permite Venda"
                                }
                                return super.getColumnClass(columnIndex);
                            }

                            @Override
                            public boolean isCellEditable(int row, int column) {
                                // Torna as colunas "ID" e "Nome" não editáveis
                                return !(getColumnName(column).equals("ID") || getColumnName(column).equals("Nome"));
                            }
                        };

// Adicionar colunas ao modelo
                        tableModel.addColumn("ID");
                        tableModel.addColumn("Nome");
                        tableModel.addColumn("Valor");
                        tableModel.addColumn("Quantidade");
                        tableModel.addColumn("Situação");
                        tableModel.addColumn("Não Permite Venda"); // Nova coluna booleana

// Adicionar linhas ao modelo com os dados do ResultSet
                        while (rsProdutos.next()) {
                            Object[] rowData = {
                                rsProdutos.getString("ID"),
                                rsProdutos.getString("Nome"),
                                (rsProdutos.getObject("Valor") != null) ? rsProdutos.getString("Valor") : "0.0",
                                rsProdutos.getString("Quantidade"),
                                rsProdutos.getString("Situação"),
                                (rsProdutos.getObject("NaopermiteVenda") != null) ? rsProdutos.getBoolean("NaopermiteVenda") : false
                            };
                            tableModel.addRow(rowData);
                        }



                        // Configurar a JTable com o modelo de tabela
                        tbl_avarias_produtos.setModel(tableModel);


                        // Configurar o editor personalizado para a coluna "Situação"
                        int situacaoColumnIndex = tableModel.findColumn("Situação");
                        tbl_avarias_produtos.getColumnModel().getColumn(situacaoColumnIndex).setCellEditor(new ComboBoxCellEditor(new String[]{"Venda", "Prejuizo", "Em estoque", "Descarte", "Devolução"}));

                        // Configurar o editor personalizado para a coluna "Não Permite Venda"
                        int naoPermiteVendaColumnIndex = tableModel.findColumn("Não Permite Venda");
                        tbl_avarias_produtos.getColumnModel().getColumn(naoPermiteVendaColumnIndex).setCellEditor(new DefaultCellEditor(new JCheckBox()));
                        




                        // Fechar o ResultSet e o PreparedStatement
                        rsProdutos.close();
                        pst.close();
                        // Consulta Motorista - Realiza uma consulta na tabela motorista

                        pst = conexao.prepareStatement(instru_sql_motorista);
                        pst.setString(1, idmotorista);//txt_avaria_idmotorista.getText());
                        ResultSet rsMotorista = pst.executeQuery();
                        if (rsMotorista.next()) {
                            txt_avarias_motorista.setText(rsMotorista.getString(2));
                            pst.close();
                        }

                        //Consulta Tabela Fornecedor(cliente)
                        pst = conexao.prepareStatement(instru_sql_fornecedor);
                        pst.setString(1, idfornecedor);
                        ResultSet rsFornecedor = pst.executeQuery();
                        if (rsFornecedor.next()) {
                            txt_avaria_cliente.setText(rsFornecedor.getString(2));
                            pst.close();
                        }

                    }
                }
                //}
            } else {
                JOptionPane.showMessageDialog(null, "Avaria não encontrado.");
//                txt_avaria_cli_id.setText(null);
                idmotorista = "1";
                idcliente = "2";
                idfornecedor = "3";
                txt_avarias_comprador.setText(null);
                txt_avarias_numero.setText(null);
                txt_avarias_venda.setText(null);
                jTextArea1.setText(null);
                box_avarias_status.setSelectedItem("Prejuizo");
                txt_avaria_prejuizo.setText(null);
                txt_avaria_desconto.setText(null);
                DefaultTableModel model = (DefaultTableModel) tbl_avarias_produtos.getModel();
                model.setRowCount(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metodo para setar os dados do produto na tabela.
     *
     * @param id
     * @param nome
     * @param descri
     * @param quant
     */
    public void setDadosTelaAvarias(String id, String nome, String descri, String quant) {
        // Adiciona os dados à tabela
        DefaultTableModel model = (DefaultTableModel) tbl_avarias_produtos.getModel();
        Object[] rowData = {id, nome, descri, quant, "Venda"};
        model.addRow(rowData);

        // Define o valor "Venda" na última linha da coluna correspondente
        int lastRowIndex = model.getRowCount() - 1;
        tbl_avarias_produtos.setValueAt("Venda", lastRowIndex, 4);
    }

    /**
     * Metodo para Setar os dados dos clientes
     *
     * @param idcli
     *
     * @param nome
     */
    public void setDadosCliTelaAvarias(String idcli, String nome) {
        // Adiciona os dados à tabela
//        txt_avaria_cli_id.setText(idcli);
        idcliente = idcli;
        txt_avarias_comprador.setText(nome);

    }

    public void setDadosMotoraTelaAvarias(String idmotoristas, String nome) {
        // Adiciona os dados à tabela
        idmotorista = idmotoristas;
        //txt_avaria_idmotorista.setText(idmotorista);
        txt_avarias_motorista.setText(nome);

    }

    public void setDadosFornecedorTelaAvarias(String idcli, String nome) {
        // Adiciona os dados à tabela
//        txt_avaria_cli_id.setText(idcli);
        idfornecedor = idcli;
        txt_avaria_cliente.setText(nome);

    }

    /**
     * Metodo para somar o valor total dos produtos adicionados na tabela.
     *
     * @param tabela
     * @param coluna1
     * @param coluna2
     * @param campoResultado
     */
    public static void valortotal(JTable tabela, int coluna1, int coluna2, JTextField campoResultado) {
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        int rowCount = model.getRowCount();

        double somaPrejuizo = 0.0;
        double somaVenda = 0.0;
        double desconto = 0.0;
        double somaEstoque = 0.0;

        for (int i = 0; i < rowCount; i++) {
            // Obtém os valores das colunas especificadas (convertendo para Double)
            double valorColuna1 = Double.parseDouble(model.getValueAt(i, coluna1).toString());
            double valorColuna2 = Double.parseDouble(model.getValueAt(i, coluna2).toString());

            // Obtém o valor da coluna 5 (índice 4)
            String tipoOperacao = model.getValueAt(i, 4).toString();

            // Multiplica os valores e soma ao total
            double resultado = valorColuna1 * valorColuna2;

            // Verifica o tipo de operação na coluna 5
            if (tipoOperacao.equals("Prejuizo")) {
                somaPrejuizo += resultado;

            } else if (tipoOperacao.equals("Venda")) {
                somaVenda += resultado;

            } else if (tipoOperacao.equals("Em estoque")) {
                somaEstoque += resultado;
            }
            if (txt_avaria_desconto.getText().isEmpty()) {
                desconto = 0.0;
            } else {
                desconto = Double.parseDouble(txt_avaria_desconto.getText());
            }
        }

        // Define os resultados nas JTextFields
        // Definindo o Locale para usar ponto como separador decimal
        Locale locale = new Locale("en", "US");
// Crie um objeto DecimalFormat para formatar os números com três casas decimais
        DecimalFormat formatoDecimal = new DecimalFormat("#.###", new DecimalFormatSymbols(locale));

// Defina os textos nas caixas de texto com os valores arredondados
        txt_avaria_prejuizo.setText(formatoDecimal.format(somaPrejuizo));
        txt_avarias_venda.setText(formatoDecimal.format(somaVenda - desconto));
        txt_valor_emestoque.setText(formatoDecimal.format(somaEstoque));

    }

    public static void removeproduto(JTable tabela) {
        // Obtém o índice da linha selecionada
        int indiceLinhaSelecionada = tabela.getSelectedRow();

        // Verifica se alguma linha está selecionada
        if (indiceLinhaSelecionada >= 0) {
            // Remove a linha selecionada
            ((javax.swing.table.DefaultTableModel) tabela.getModel()).removeRow(indiceLinhaSelecionada);

        } else {
            // Se nenhuma linha estiver selecionada, exibe uma mensagem de aviso
            JOptionPane.showMessageDialog(null, "Selecione uma linha para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void remover_armazem() {
        String instru_sql = "DELETE FROM tbarmazem WHERE armazem_avaria_numeronf = ?";

        try {
            pst = conexao.prepareStatement(instru_sql);

            // Substitua "armazem_nf" abaixo pelo nome do campo correto em sua tabela
            pst.setString(1, txt_avarias_numero.getText());

            // Execute a instrução SQL
            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao remover produtos: " + e.getMessage());
        }
    }

    /**
     * Metodo para salvar logs no computador.
     *
     * @param usuariologado
     */
    public void salvarlog_adicionar_avaria(String usuariologado) {
        String instru_sql = "INSERT INTO tbLog (tb_log_usuario,tb_log_datahora,tb_log_operacao,tb_log_descri) VALUES (?, ?, ?, ?)";
        Date currentDate = new Date();
        Timestamp timestamp = new Timestamp(currentDate.getTime());

        try {
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, usuariologado);
            pst.setTimestamp(2, timestamp);
            pst.setString(3, "Cadastro na Tela Avaria.");
            pst.setString(4, "Cadastro de Avaria Numero NFe ou Codigo Identificador: " + txt_avarias_numero.getText());

            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro no Log!");
        }

    }

    public void salvarlog_atualizar_avaria(String usuariologado) {
        String instru_sql = "INSERT INTO tbLog (tb_log_usuario,tb_log_datahora,tb_log_operacao,tb_log_descri) VALUES (?, ?, ?, ?)";
        java.util.Date currentDate = new java.util.Date();
        Timestamp timestamp = new Timestamp(currentDate.getTime());

        try {
            pst = conexao.prepareStatement(instru_sql);
            pst.setString(1, usuariologado);
            pst.setTimestamp(2, timestamp);
            pst.setString(3, "Atualização na Tela de Avarias.");
            pst.setString(4, "Cadastro de Avaria Numero NFe ou Codigo Identificador: " + txt_avarias_numero.getText());

            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de cadastro! \n" + e);
        }

    }

    public static boolean verificarCheckboxVenda(JTable tabela) {
        if (tabela == null) {
            // Trate o caso em que a tabela é nula
            return false;
        }

        TableModel model = tabela.getModel();

        if (!(model instanceof DefaultTableModel)) {
            // Trate o caso em que o modelo não é do tipo DefaultTableModel
            return false;
        }

        DefaultTableModel defaultModel = (DefaultTableModel) model;

        int rowCount = defaultModel.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            Object checkboxValue = defaultModel.getValueAt(i, 5);
            Object situacaoValue = defaultModel.getValueAt(i, 4);

            if (checkboxValue instanceof Boolean && situacaoValue instanceof String) {
                boolean checkboxMarcado = (boolean) checkboxValue;
                String situacao = (String) situacaoValue;

                if (checkboxMarcado && "Venda".equalsIgnoreCase(situacao)) {
                    return true;  // Se encontrou uma linha, retorna true
                }
            }
        }

        return false;  // Se não encontrou nenhuma linha, retorna false
    }

    private void checarcheckbox(java.awt.event.ItemEvent evt) {
        if (box_cliente_debitado.isSelected()) {
            txt_avaria_debitadocliente.setEnabled(true);
            txt_avaria_debitadocliente.setEditable(true);
        } else {
            txt_avaria_debitadocliente.setEnabled(false);
            txt_avaria_debitadocliente.setEditable(false);
            txt_avaria_debitadocliente.setText("");
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

        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        txt_avarias_numero = new javax.swing.JTextField();
        lbl_avarias_numero = new javax.swing.JLabel();
        lbl_avarias_status = new javax.swing.JLabel();
        box_avarias_status = new javax.swing.JComboBox<>();
        btn_avaria_busca = new javax.swing.JButton();
        lbl_avaria_comprador = new javax.swing.JLabel();
        txt_avarias_comprador = new javax.swing.JTextField();
        lbl_avaria_obs = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        btn_avarias_atualizar = new javax.swing.JButton();
        box_avarias_armazem = new javax.swing.JComboBox<>();
        lbl_avaria_armazem = new javax.swing.JLabel();
        lbl_avaria_motivo = new javax.swing.JLabel();
        box_avarias_motivo = new javax.swing.JComboBox<>();
        lbl_avarias_cte = new javax.swing.JLabel();
        lbl_avarias_filial = new javax.swing.JLabel();
        lbl_avarias_serie = new javax.swing.JLabel();
        txt_avarias_cte = new javax.swing.JTextField();
        txt_avarias_filial = new javax.swing.JTextField();
        txt_avarias_serie = new javax.swing.JTextField();
        btn_avarias_buscarcte = new javax.swing.JButton();
        lbl_avarias_motorista = new javax.swing.JLabel();
        txt_avarias_motorista = new javax.swing.JTextField();
        btn_avarias_buscarmotorista = new javax.swing.JButton();
        lbl_avaria_cliente = new javax.swing.JLabel();
        txt_avaria_cliente = new javax.swing.JTextField();
        btn_avarias_cliente = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txt_avarias_venda = new javax.swing.JTextField();
        lbl_avarias_quantidade = new javax.swing.JLabel();
        btn_avarias_abrirprod = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_avarias_produtos = new javax.swing.JTable();
        btn_avaria_calcular = new javax.swing.JButton();
        btn_avarias_adicionar = new javax.swing.JButton();
        lbl_avaria_desconto = new javax.swing.JLabel();
        txt_avaria_desconto = new javax.swing.JTextField();
        lbl_avaria_prejuizo = new javax.swing.JLabel();
        txt_avaria_prejuizo = new javax.swing.JTextField();
        btn_avaria_removprod = new javax.swing.JButton();
        btn_avarias_update = new javax.swing.JButton();
        lbl_avaria_debitado = new javax.swing.JLabel();
        txt_avarias_debitado = new javax.swing.JTextField();
        lbl_avaria_estoque = new javax.swing.JLabel();
        txt_valor_emestoque = new javax.swing.JTextField();
        box_cliente_debitado = new javax.swing.JCheckBox();
        lbl_avaria_valordebitadocliente = new javax.swing.JLabel();
        txt_avaria_debitadocliente = new javax.swing.JTextField();
        lbl_avaria_checkboxdebitadocliente = new javax.swing.JLabel();
        txt_usuario_atualizar = new javax.swing.JTextField();
        txt_usuario_cadastrar = new javax.swing.JTextField();
        lbl_usuario_cadastrar = new javax.swing.JLabel();
        lbl_usuario_atualizar = new javax.swing.JLabel();
        lbl_avarias_iconetdm = new javax.swing.JLabel();
        txt_avarias_data = new javax.swing.JFormattedTextField();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Prejuizo", "Venda", "Em estoque", "Descarte", "Devolução", "Falta procede" }));

        setClosable(true);
        setIconifiable(true);
        setTitle("Avarias");
        setToolTipText("Tela de Cadastro de Avarias");
        setPreferredSize(new java.awt.Dimension(800, 610));
        setRequestFocusEnabled(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setToolTipText("Tela de Cadastro de Avarias"); // NOI18N

        txt_avarias_numero.setColumns(25);
        txt_avarias_numero.setToolTipText("Digite o código identificador");
        txt_avarias_numero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_avarias_numeroKeyTyped(evt);
            }
        });

        lbl_avarias_numero.setText("Número da NFd:");
        lbl_avarias_numero.setToolTipText("");

        lbl_avarias_status.setText("Status:");
        lbl_avarias_status.setToolTipText("null");

        box_avarias_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pendente", "Processando", "Em espera", "Finalizado", "Devolução" }));
        box_avarias_status.setToolTipText("Status");
        box_avarias_status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                box_avarias_statusActionPerformed(evt);
            }
        });

        btn_avaria_busca.setBackground(new java.awt.Color(255, 255, 255));
        btn_avaria_busca.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btn_avaria_busca.setText("Buscar comprador");
        btn_avaria_busca.setPreferredSize(new java.awt.Dimension(103, 25));
        btn_avaria_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_avaria_buscaActionPerformed(evt);
            }
        });

        lbl_avaria_comprador.setText("Comprador:");
        lbl_avaria_comprador.setToolTipText("");

        txt_avarias_comprador.setEditable(false);
        txt_avarias_comprador.setColumns(25);
        txt_avarias_comprador.setToolTipText("Nome do Cliente ");
        txt_avarias_comprador.setEnabled(false);
        txt_avarias_comprador.setPreferredSize(null);

        lbl_avaria_obs.setText("Observação:");
        lbl_avaria_obs.setToolTipText("");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setToolTipText("Digite observações necessárias");
        jScrollPane1.setViewportView(jTextArea1);

        btn_avarias_atualizar.setBackground(new java.awt.Color(255, 255, 255));
        btn_avarias_atualizar.setFont(new java.awt.Font("Serif", 1, 13)); // NOI18N
        btn_avarias_atualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/projetotdm/icones/lupa20px.png"))); // NOI18N
        btn_avarias_atualizar.setToolTipText("Atualizar Avaria");
        btn_avarias_atualizar.setPreferredSize(new java.awt.Dimension(64, 64));
        btn_avarias_atualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_avarias_atualizarActionPerformed(evt);
            }
        });

        box_avarias_armazem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "OUTROS", "FILIAL Açailândia", "FILIAL Alagoinhas", "FILIAL Alexânia", "FILIAL Amparo", "FILIAL Anápolis", "FILIAL Aparecida de Goiânia", "FILIAL Barra Mansa", "FILIAL Belém", "FILIAL Brasília", "FILIAL Cariacica", "FILIAL Campo Grande", "FILIAL Campina Grande", "FILIAL Caucaia", "FILIAL Colatina/ES", "FILIAL Contagem/MG", "FILIAL Corumbaíba", "FILIAL Cuiabá", "FILIAL Dourados/MS", "FILIAL Feira de Santana", "FILIAL Fortaleza", "FILIAL Imperatriz", "FILIAL Itajaí", "FILIAL Itu", "FILIAL Itumbiara", "FILIAL Itupeva", "FILIAL Jacareí", "FILIAL Jaguariúna", "FILIAL Jaboatão dos Guararapes", "FILIAL Jarinu", "FILIAL Juiz de Fora", "FILIAL Luziânia", "FILIAL Minaçu", "FILIAL Marabá", "FILIAL Montes Claros", "FILIAL Parauapebas", "FILIAL Piracicaba", "FILIAL Parnamirim/RN", "FILIAL Ponta Grossa", "FILIAL Ribeirão Preto", "FILIAL Rio de Janeiro", "FILIAL São Luís", "FILIAL Simões Filho", "FILIAL Serra/ES", "FILIAL Teresina/PI", "FILIAL Uberaba", "FILIAL Várzea Grande", "OP APT LOGISTICA - Dourados/MS", "OP AD Transportes - Parobé/RS", "OP CALEGARIO - Colatina/ES", "OP FEC - Feira de Santana/BA", "OP JAND LOG - Jandira/SP", "OP LOGUS - Teresina/PI", "OP MAGNA LOG - Aparecida de Goiânia/GO", "OP MK TRANSPORTES - Campo Grande/MS", "OP PALETIZAR - Jaboatão dos Guararapes/BA", "OP PHEX - Cuiabá/MT", "OP ROTA BAHIA - Simões Bahia/BA", "OP SAMMEX - Parnamirim/RN", "OP TEIXEIRA - Caruaru/BA", "OP TRANSATLANTICA - Marabá/PA", "OP TRANSFORTES - Contagem/MG", "OP TRANSLAPAS - Rio de Janeiro/RJ", "OP TRANSMEDEIROS", "OP TRD - Serra/ES", "OP VAPT VUPT" }));
        box_avarias_armazem.setToolTipText("Estado");

        lbl_avaria_armazem.setText("Armazem:");
        lbl_avaria_armazem.setToolTipText("null");

        lbl_avaria_motivo.setText("Motivo:");

        box_avarias_motivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Falta", "Avaria", "Inversao", "Sem pedido", "Preço", "Qualidade", "Sobra" }));
        box_avarias_motivo.setToolTipText("Motivo");

        lbl_avarias_cte.setText("Cte:*");

        lbl_avarias_filial.setText("Filial:*");

        lbl_avarias_serie.setText("Serie:*");

        txt_avarias_cte.setColumns(5);
        txt_avarias_cte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_avarias_cteKeyTyped(evt);
            }
        });

        txt_avarias_filial.setColumns(2);
        txt_avarias_filial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_avarias_filialKeyTyped(evt);
            }
        });

        txt_avarias_serie.setColumns(2);
        txt_avarias_serie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_avarias_serieKeyTyped(evt);
            }
        });

        btn_avarias_buscarcte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/projetotdm/icones/lupa20px.png"))); // NOI18N
        btn_avarias_buscarcte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_avarias_buscarcteActionPerformed(evt);
            }
        });

        lbl_avarias_motorista.setText("Motorista:");

        txt_avarias_motorista.setEditable(false);
        txt_avarias_motorista.setEnabled(false);

        btn_avarias_buscarmotorista.setBackground(new java.awt.Color(255, 255, 255));
        btn_avarias_buscarmotorista.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btn_avarias_buscarmotorista.setText("Buscar motorista");
        btn_avarias_buscarmotorista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_avarias_buscarmotoristaActionPerformed(evt);
            }
        });

        lbl_avaria_cliente.setText("Cliente:");

        txt_avaria_cliente.setEditable(false);
        txt_avaria_cliente.setEnabled(false);

        btn_avarias_cliente.setBackground(new java.awt.Color(255, 255, 255));
        btn_avarias_cliente.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btn_avarias_cliente.setText("Buscar cliente");
        btn_avarias_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_avarias_clienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_avarias_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_avarias_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_avarias_atualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_avarias_motorista)
                                    .addComponent(lbl_avaria_cliente)
                                    .addComponent(lbl_avaria_comprador, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_avarias_comprador, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                    .addComponent(txt_avarias_motorista)
                                    .addComponent(txt_avaria_cliente, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl_avarias_filial, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_avarias_filial, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_avarias_serie)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_avarias_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_avarias_cte)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_avarias_cte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_avarias_buscarcte, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_avarias_buscarmotorista, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_avaria_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_avarias_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_avaria_obs)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_avarias_status, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_avaria_motivo)
                            .addComponent(lbl_avaria_armazem))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(box_avarias_status, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(box_avarias_motivo, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(box_avarias_armazem, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_avarias_status, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(box_avarias_status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_avaria_armazem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(box_avarias_armazem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_avaria_motivo, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(box_avarias_motivo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(lbl_avaria_obs, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btn_avarias_atualizar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(txt_avarias_numero, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_avarias_numero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_avaria_comprador, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_avarias_comprador, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                            .addComponent(btn_avaria_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_avarias_motorista)
                            .addComponent(txt_avarias_motorista, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_avarias_buscarmotorista, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_avaria_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_avaria_cliente)
                            .addComponent(btn_avarias_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_avarias_buscarcte, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_avarias_cte, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl_avarias_cte, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_avarias_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl_avarias_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_avarias_filial, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl_avarias_filial, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(35, 35, 35))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setToolTipText("Tela de Cadastro de Avarias");
        jPanel3.setPreferredSize(new java.awt.Dimension(732, 212));

        txt_avarias_venda.setEditable(false);
        txt_avarias_venda.setColumns(20);
        txt_avarias_venda.setToolTipText("Valor de Venda");
        txt_avarias_venda.setEnabled(false);

        lbl_avarias_quantidade.setText("Valor Venda:");
        lbl_avarias_quantidade.setToolTipText("");

        btn_avarias_abrirprod.setBackground(new java.awt.Color(204, 255, 204));
        btn_avarias_abrirprod.setFont(new java.awt.Font("Serif", 0, 13)); // NOI18N
        btn_avarias_abrirprod.setText("Adicionar Item");
        btn_avarias_abrirprod.setToolTipText("Adicionar");
        btn_avarias_abrirprod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_avarias_abrirprodActionPerformed(evt);
            }
        });

        jLabel6.setText("Produtos:");

        jScrollPane4.setFocusable(false);

        tbl_avarias_produtos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Valor Unitário", "Quantidade", "Situação", "Não Permite Venda"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_avarias_produtos.setFocusable(false);
        tbl_avarias_produtos.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(tbl_avarias_produtos);
        if (tbl_avarias_produtos.getColumnModel().getColumnCount() > 0) {
            tbl_avarias_produtos.getColumnModel().getColumn(4).setCellEditor(new javax.swing.DefaultCellEditor(jComboBox1));
        }

        btn_avaria_calcular.setBackground(new java.awt.Color(204, 255, 255));
        btn_avaria_calcular.setFont(new java.awt.Font("Serif", 0, 13)); // NOI18N
        btn_avaria_calcular.setText("Calcular");
        btn_avaria_calcular.setToolTipText("Calcular");
        btn_avaria_calcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_avaria_calcularActionPerformed(evt);
            }
        });

        btn_avarias_adicionar.setBackground(new java.awt.Color(204, 255, 204));
        btn_avarias_adicionar.setFont(new java.awt.Font("Serif", 1, 13)); // NOI18N
        btn_avarias_adicionar.setText("Cadastrar");
        btn_avarias_adicionar.setToolTipText("Cadastrar Avaria");
        btn_avarias_adicionar.setPreferredSize(new java.awt.Dimension(64, 64));
        btn_avarias_adicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_avarias_adicionarActionPerformed(evt);
            }
        });

        lbl_avaria_desconto.setText("Desconto:");
        lbl_avaria_desconto.setToolTipText("");

        txt_avaria_desconto.setToolTipText("Aplique desconto sobre a venda");
        txt_avaria_desconto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_avaria_descontoKeyTyped(evt);
            }
        });

        lbl_avaria_prejuizo.setText("Prejuízo:");
        lbl_avaria_prejuizo.setToolTipText("");

        txt_avaria_prejuizo.setEditable(false);
        txt_avaria_prejuizo.setToolTipText("Valor de Prejuizo");
        txt_avaria_prejuizo.setEnabled(false);
        txt_avaria_prejuizo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_avaria_prejuizoActionPerformed(evt);
            }
        });

        btn_avaria_removprod.setBackground(new java.awt.Color(255, 204, 204));
        btn_avaria_removprod.setFont(new java.awt.Font("Serif", 0, 13)); // NOI18N
        btn_avaria_removprod.setText("Remover Item");
        btn_avaria_removprod.setToolTipText("Remover");
        btn_avaria_removprod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_avaria_removprodActionPerformed(evt);
            }
        });

        btn_avarias_update.setBackground(new java.awt.Color(255, 255, 153));
        btn_avarias_update.setFont(new java.awt.Font("Serif", 1, 13)); // NOI18N
        btn_avarias_update.setText("Atualizar");
        btn_avarias_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_avarias_updateActionPerformed(evt);
            }
        });

        lbl_avaria_debitado.setText("Valor Debitado");

        txt_avarias_debitado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_avarias_debitadoKeyTyped(evt);
            }
        });

        lbl_avaria_estoque.setText("Valor em Estoque:");

        txt_valor_emestoque.setEditable(false);
        txt_valor_emestoque.setEnabled(false);

        box_cliente_debitado.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        box_cliente_debitado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                box_cliente_debitadoItemStateChanged(evt);
            }
        });
        box_cliente_debitado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                box_cliente_debitadoActionPerformed(evt);
            }
        });

        lbl_avaria_valordebitadocliente.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_avaria_valordebitadocliente.setText("Valor:");

        txt_avaria_debitadocliente.setEnabled(false);
        txt_avaria_debitadocliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_avaria_debitadoclienteKeyTyped(evt);
            }
        });

        lbl_avaria_checkboxdebitadocliente.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_avaria_checkboxdebitadocliente.setText("Débitado do cliente?");

        txt_usuario_atualizar.setEditable(false);
        txt_usuario_atualizar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txt_usuario_atualizar.setEnabled(false);

        txt_usuario_cadastrar.setEditable(false);
        txt_usuario_cadastrar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txt_usuario_cadastrar.setEnabled(false);

        lbl_usuario_cadastrar.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lbl_usuario_cadastrar.setText("Cadastrado por:");

        lbl_usuario_atualizar.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lbl_usuario_atualizar.setText("Atualizado por:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_avaria_removprod, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_avarias_abrirprod, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lbl_avaria_prejuizo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_avaria_prejuizo, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(lbl_avaria_desconto)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_avaria_desconto, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_avarias_quantidade)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_avarias_venda, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_avaria_debitado)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_avarias_debitado, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_avaria_estoque)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_valor_emestoque, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_avaria_calcular))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(lbl_avaria_valordebitadocliente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_avaria_debitadocliente))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(lbl_avaria_checkboxdebitadocliente, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(box_cliente_debitado)))
                                .addGap(197, 197, 197)
                                .addComponent(btn_avarias_update, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_avarias_adicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(149, 149, 149)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lbl_usuario_atualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbl_usuario_cadastrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_usuario_cadastrar)
                                    .addComponent(txt_usuario_atualizar)))))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_avaria_removprod, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_avarias_abrirprod, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_avaria_prejuizo)
                    .addComponent(txt_avaria_desconto, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_avarias_quantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_avarias_venda, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_avaria_calcular)
                    .addComponent(lbl_avaria_desconto)
                    .addComponent(txt_avarias_debitado, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_avaria_debitado)
                    .addComponent(lbl_avaria_estoque)
                    .addComponent(txt_valor_emestoque, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_avaria_prejuizo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_usuario_cadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_usuario_cadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_usuario_atualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_usuario_atualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lbl_avaria_checkboxdebitadocliente)
                                .addGap(13, 13, 13))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(box_cliente_debitado)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbl_avaria_valordebitadocliente)
                                .addComponent(txt_avaria_debitadocliente, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btn_avarias_adicionar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(btn_avarias_update, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addGap(33, 33, 33))))
        );

        lbl_avarias_iconetdm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/projetotdm/icones/tdmtransporte.png"))); // NOI18N

        txt_avarias_data.setEditable(false);
        try {
            txt_avarias_data.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_avarias_data.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(lbl_avarias_iconetdm, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_avarias_data, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 983, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_avarias_iconetdm)
                    .addComponent(txt_avarias_data, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );

        setBounds(14, 15, 1023, 715);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Metodo para dar função aos botões e abrir uma sub-tela
     *
     * @param evt
     */
    private void btn_avarias_abrirprodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_avarias_abrirprodActionPerformed
        TelaPesquisarProd pesquisar_prod = new TelaPesquisarProd();
        pesquisar_prod.setTelaAvarias(this);
        pesquisar_prod.setVisible(true);

    }//GEN-LAST:event_btn_avarias_abrirprodActionPerformed

    /**
     * Metodo para adicionar a função de adicionar nas tabelas avaria e estoque
     * ao botão btn_avarias_adicionar
     *
     * @param evt
     */
    private void btn_avarias_adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_avarias_adicionarActionPerformed

        if (verificarCheckboxVenda(tbl_avarias_produtos)) {
            JOptionPane.showMessageDialog(rootPane, "Verifique seus produtos, você está tentando vender um produto que não é possível a venda!", "Atenção", 1);

        } else {
            if (adicionar_avaria()) {
                adicionar_estoque();
                adicionar_armazem();
                salvarlog_adicionar_avaria(usuariologado);
                limpar();
            };
        }


    }//GEN-LAST:event_btn_avarias_adicionarActionPerformed

    private void txt_avaria_descontoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_avaria_descontoKeyTyped
        String caracteres = "0123456789.";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_avaria_descontoKeyTyped

    /**
     * Metodo para adicionar a função de soma dos valores ao botão somar
     *
     * @param evt
     */
    private void btn_avaria_calcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_avaria_calcularActionPerformed

        valortotal(tbl_avarias_produtos, 2, 3, txt_avarias_venda);
    }//GEN-LAST:event_btn_avaria_calcularActionPerformed

    private void btn_avaria_removprodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_avaria_removprodActionPerformed
        removeproduto(tbl_avarias_produtos);
    }//GEN-LAST:event_btn_avaria_removprodActionPerformed

    private void btn_avarias_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_avarias_updateActionPerformed
        if (verificarCheckboxVenda(tbl_avarias_produtos)) {
            JOptionPane.showMessageDialog(rootPane, "Verifique seus produtos, você está tentando vender um produto que não é possivel a venda!", "Atenção", 1);

        } else {
            valortotal(tbl_avarias_produtos, 2, 3, txt_avarias_venda);

            if (atualizarAvaria()) {

                atualizarEstoque();
                adicionar_estoque();
                remover_armazem();
                adicionar_armazem();
                salvarlog_atualizar_avaria(usuariologado);
                limpar();
            }
        }


    }//GEN-LAST:event_btn_avarias_updateActionPerformed

    private void txt_avarias_debitadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_avarias_debitadoKeyTyped
        //Função que limita os caracteres para os selecionados
        String caracteres = "0123456789.";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_avarias_debitadoKeyTyped

    private void txt_avaria_prejuizoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_avaria_prejuizoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_avaria_prejuizoActionPerformed

    /**
     * Metodo para colocar função de buscar e verificação se o textfield
     * especificado está vazio.
     *
     * @param evt
     */
    private void btn_avarias_atualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_avarias_atualizarActionPerformed
        if (txt_avarias_numero.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha a nota fiscal para pesquisar uma Avaria!");
            limpar();

        } else {
            buscar();
            btn_avarias_update.setEnabled(true);
        }
    }//GEN-LAST:event_btn_avarias_atualizarActionPerformed

    /**
     * metodo para adicionar a função de abrir uma subtela para adicionar os
     * dados do cliente
     *
     * @param evt
     */
    private void btn_avaria_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_avaria_buscaActionPerformed
        TelaPesquisarCli pesquisar_cli = new TelaPesquisarCli();
        pesquisar_cli.setTelaAvarias(this);
        pesquisar_cli.setVisible(true);
    }//GEN-LAST:event_btn_avaria_buscaActionPerformed

    private void box_avarias_statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_box_avarias_statusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_box_avarias_statusActionPerformed

    /**
     * metodo para limitar caracteres em uma determinada textfield
     *
     * @param evt
     */
    private void txt_avarias_numeroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_avarias_numeroKeyTyped
        //Função que limita os caracteres para os selecionados
        String caracteres = "0123456789";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_avarias_numeroKeyTyped

    private void btn_avarias_buscarcteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_avarias_buscarcteActionPerformed
        buscarcte();


    }//GEN-LAST:event_btn_avarias_buscarcteActionPerformed

    private void txt_avarias_filialKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_avarias_filialKeyTyped
        //Função que limita os caracteres para os selecionados
        String caracteres = "0123456789";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_avarias_filialKeyTyped

    private void txt_avarias_serieKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_avarias_serieKeyTyped
        //Função que limita os caracteres para os selecionados
        String caracteres = "0123456789";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_avarias_serieKeyTyped

    private void txt_avarias_cteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_avarias_cteKeyTyped
        //Função que limita os caracteres para os selecionados
        String caracteres = "0123456789";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_avarias_cteKeyTyped

    private void btn_avarias_buscarmotoristaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_avarias_buscarmotoristaActionPerformed
        TelaPesquisaMotora motora = new TelaPesquisaMotora();
        motora.setTelaAvariasMotora(this);
        motora.setVisible(true);

    }//GEN-LAST:event_btn_avarias_buscarmotoristaActionPerformed

    private void box_cliente_debitadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_box_cliente_debitadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_box_cliente_debitadoActionPerformed

    private void box_cliente_debitadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_box_cliente_debitadoItemStateChanged
        checarcheckbox(evt);
    }//GEN-LAST:event_box_cliente_debitadoItemStateChanged

    private void txt_avaria_debitadoclienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_avaria_debitadoclienteKeyTyped
        String caracteres = "0123456789.";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_avaria_debitadoclienteKeyTyped

    private void btn_avarias_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_avarias_clienteActionPerformed
        TelaPesquisarFornecedor fornecedor = new TelaPesquisarFornecedor();
        fornecedor.setTelaAvarias(this);
        fornecedor.setVisible(true);
    }//GEN-LAST:event_btn_avarias_clienteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> box_avarias_armazem;
    private javax.swing.JComboBox<String> box_avarias_motivo;
    private javax.swing.JComboBox<String> box_avarias_status;
    private javax.swing.JCheckBox box_cliente_debitado;
    private javax.swing.JButton btn_avaria_busca;
    private javax.swing.JButton btn_avaria_calcular;
    private javax.swing.JButton btn_avaria_removprod;
    private javax.swing.JButton btn_avarias_abrirprod;
    private javax.swing.JButton btn_avarias_adicionar;
    private javax.swing.JButton btn_avarias_atualizar;
    private javax.swing.JButton btn_avarias_buscarcte;
    private javax.swing.JButton btn_avarias_buscarmotorista;
    private javax.swing.JButton btn_avarias_cliente;
    private javax.swing.JButton btn_avarias_update;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lbl_avaria_armazem;
    private javax.swing.JLabel lbl_avaria_checkboxdebitadocliente;
    private javax.swing.JLabel lbl_avaria_cliente;
    private javax.swing.JLabel lbl_avaria_comprador;
    private javax.swing.JLabel lbl_avaria_debitado;
    private javax.swing.JLabel lbl_avaria_desconto;
    private javax.swing.JLabel lbl_avaria_estoque;
    private javax.swing.JLabel lbl_avaria_motivo;
    private javax.swing.JLabel lbl_avaria_obs;
    private javax.swing.JLabel lbl_avaria_prejuizo;
    private javax.swing.JLabel lbl_avaria_valordebitadocliente;
    private javax.swing.JLabel lbl_avarias_cte;
    private javax.swing.JLabel lbl_avarias_filial;
    private javax.swing.JLabel lbl_avarias_iconetdm;
    private javax.swing.JLabel lbl_avarias_motorista;
    private javax.swing.JLabel lbl_avarias_numero;
    private javax.swing.JLabel lbl_avarias_quantidade;
    private javax.swing.JLabel lbl_avarias_serie;
    private javax.swing.JLabel lbl_avarias_status;
    private javax.swing.JLabel lbl_usuario_atualizar;
    private javax.swing.JLabel lbl_usuario_cadastrar;
    private javax.swing.JTable tbl_avarias_produtos;
    private javax.swing.JTextField txt_avaria_cliente;
    private javax.swing.JTextField txt_avaria_debitadocliente;
    private static javax.swing.JTextField txt_avaria_desconto;
    private static javax.swing.JTextField txt_avaria_prejuizo;
    private javax.swing.JTextField txt_avarias_comprador;
    private javax.swing.JTextField txt_avarias_cte;
    private javax.swing.JFormattedTextField txt_avarias_data;
    private static javax.swing.JTextField txt_avarias_debitado;
    private javax.swing.JTextField txt_avarias_filial;
    private javax.swing.JTextField txt_avarias_motorista;
    private javax.swing.JTextField txt_avarias_numero;
    private javax.swing.JTextField txt_avarias_serie;
    private static javax.swing.JTextField txt_avarias_venda;
    private javax.swing.JTextField txt_usuario_atualizar;
    private javax.swing.JTextField txt_usuario_cadastrar;
    private static javax.swing.JTextField txt_valor_emestoque;
    // End of variables declaration//GEN-END:variables
}
