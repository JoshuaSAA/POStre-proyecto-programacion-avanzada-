package com.example.patata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {
    public List<Table> getAllTables() throws Exception {
        Connection conn = ConectionDB.obtenerInstancia();
        String sql = "SELECT * FROM tablesCustomers";
        List<Table> tables = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Table table = new Table();
                table.setId(rs.getInt("idTable"));
                table.setTableNumber(rs.getInt("tableNumber"));
                table.setStatus(rs.getString("status"));
                tables.add(table);
            }
        }
        return tables;
    }

    private int getNextTableNumber() throws Exception {
        Connection conn = ConectionDB.obtenerInstancia();
        String sql = "SELECT MAX(tableNumber) AS maxTableNumber FROM tablesCustomers";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet resultSet = pstmt.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("maxTableNumber") + 1;
            } else {
                return 1;
            }
        }
    }

    public void markTableAsAvailable(int tableId) throws Exception {
        Connection conn = ConectionDB.obtenerInstancia();
        String sql = "UPDATE tablesCustomers SET status = 'Disponible' WHERE idTable = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tableId);
            pstmt.executeUpdate();
        }
    }

    public void addTable() throws Exception {
        int nextTableNumber = getNextTableNumber();
        Connection conn = ConectionDB.obtenerInstancia();
        String sql = "INSERT INTO tablesCustomers (tableNumber, status) VALUES (?, 'Disponible')";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, nextTableNumber);
            pstmt.executeUpdate();
        }
    }

}
