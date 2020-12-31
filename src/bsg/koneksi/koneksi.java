/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsg.koneksi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author kevadamargalih
 */
public class koneksi {
    public static String PathReport=System.getProperty("user.dir") + "/src/bsg/report/";
    private static Connection connection = null;
        public static Connection getConnection() {
            if (connection != null)
                return connection;
            else {
                String dbUrl = "jdbc:mysql://localhost:8889/siakadbsg?user=root&password=root&useSSL=false&autoReconnect=true";
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    connection = DriverManager.getConnection(dbUrl);
                } catch (ClassNotFoundException | SQLException e) {
                }
            return connection;
            }
        }
}
