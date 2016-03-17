/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author c0666985
 */
public class DBUtils {

    public final static String SALT = "THISISArandomSTRINGofCHARACTERSusedTOsaltTHEpasswords";

    /**
     * Salts and hashes a password for semi-secure storage
     *
     * @param password the original password
     * @return the salted and hashed password
     */
    public static String hash(String password) {
        try {
            String salted = password + SALT;
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] hash = md.digest(salted.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(b & 0xff).toUpperCase();
                if (hex.length() == 1) {
                    sb.append("0");
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Establishes a connection with the database
     *
     * @return the database connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        String hostname = "IPRO";
        String port = "3306";
        String dbname = "simpleblog";
        String username = "simpleblog";
        String password = "February2016";
        String jdbc = String.format("jdbc:mysql://%s:%s/%s", hostname, port, dbname);
        return DriverManager.getConnection(jdbc, username, password);
    }
}