package com.example.patata;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConectionDB {
    private static Connection conn=null;
    private static final String DbUrl = "jdbc:mysql://localhost/POStreDB?";
    private static final String DbUser = "root";
    private static final String DbPassword = "12345";
    protected ConectionDB() {

    }
    public static Connection obtenerInstancia() throws Exception {
        if (conn == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DbUrl,DbUser,DbPassword);
        }
        return conn;
    }

}