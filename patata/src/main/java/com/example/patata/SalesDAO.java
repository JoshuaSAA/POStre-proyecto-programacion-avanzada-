package com.example.patata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO {

    public List<Sale> getAllSalesOfDay() throws Exception {
        Connection conn = ConectionDB.obtenerInstancia();
        String sql = "SELECT tableId, productName, price, quantity, saleTime FROM sales WHERE DATE(saleTime) = CURDATE()";
        List<Sale> sales = new ArrayList<>();
        float totalSales = 0.0f; // Variable para almacenar la suma total de ventas

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Sale sale = new Sale();
                sale.setTableId(rs.getInt("tableId"));
                sale.setProductName(rs.getString("productName"));
                sale.setPrice(rs.getFloat("price"));
                sale.setQuantity(rs.getInt("quantity"));
                sale.setSaleTime(rs.getTimestamp("saleTime"));
                sales.add(sale);

                // Calcular el monto de esta venta
                float saleAmount = sale.getPrice();
                totalSales += saleAmount; // Sumar el monto al total
            }
        }

        // Crear un objeto Sale adicional para almacenar el total
        Sale totalSale = new Sale();
        totalSale.setProductName("Total de Ventas");
        totalSale.setPrice(totalSales);
        sales.add(totalSale);

        return sales;
    }

    public float getTotalSalesForTable(int tableId) throws Exception {
        Connection conn = ConectionDB.obtenerInstancia();
        String sql = "SELECT SUM(price) as total FROM sales WHERE tableId = ? AND status = 'No Pagada'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tableId);
            System.out.println("SQL statement: " + stmt); // Imprime la consulta SQL para verificarla

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    float total = rs.getFloat("total");
                    System.out.println("Total for table " + tableId + ": " + total); // Imprime el total para verificarlo
                    return total;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Manejo de excepciones
        }
        return 0;
    }


    public void markSalesAsPaidForTable(int tableId) throws Exception {
        Connection conn = ConectionDB.obtenerInstancia();
        String sql = "UPDATE sales SET status = 'Pagado' WHERE tableId = ? AND status = 'No Pagada'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tableId);
            stmt.executeUpdate();
        }
    }
}
