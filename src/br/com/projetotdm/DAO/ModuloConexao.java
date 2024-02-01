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
package br.com.projetotdm.DAO;

import java.sql.*;

/**
 * Conexao com Banco de Dados
 *
 * @author João Victor Souza de Faria
 * @version 1.1
 *
 */
public class ModuloConexao {

    /**
     * Metodo Responsavel pela conexão com o banco de dados
     *
     * @return conexao
     */
    public static Connection conector() {

        java.sql.Connection conexao = null;
              
        
        /**
         * MySql serverLocal
         */
        
//        String url = "jdbc:mysql://localhost:3306/dbavaria?characterEncoding=utf-8";
//        String driver = "com.mysql.cj.jdbc.Driver";
//        String user = "dba";
//        
        
        /**
         * SQL Server - Server VM
         */
        
       String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
       String url = "jdbc:sqlserver://192.168.0.17:1433;databaseName=dbavaria;encrypt=true;trustServerCertificate=true";
       String user = "sa";        
       String password = "System@123";

        //Estabelecer conexão       
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;

        } catch (Exception e) {
            System.out.println("Erro na conexão:" + e);
            return null;
        }

    }

}
