package com.example.patata;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public void addProduct(Product product) throws Exception {
        Connection conn = ConectionDB.obtenerInstancia();
        String sql = "INSERT INTO product (productName, price, description, picture, category) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProductName());
            stmt.setFloat(2, product.getPrice());
            stmt.setString(3, product.getDescription());
            stmt.setString(4, product.getPicture());
            stmt.setInt(5, product.getCategory());
            stmt.executeUpdate();
        }
    }

    public List<Product> getAllProducts() throws Exception {
        Connection conn = ConectionDB.obtenerInstancia();
        String sql = "SELECT * FROM product";
        List<Product> products = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("idProduct"),
                        rs.getString("productName"),
                        rs.getFloat("price"),
                        rs.getString("description"),
                        rs.getString("picture"),
                        rs.getInt("category")
                );
                products.add(product);
            }
        }
        return products;
    }

    public void updateProduct(Product product) throws Exception {
        Connection conn = ConectionDB.obtenerInstancia();
        String sql = "UPDATE product SET productName = ?, price = ?, description = ?, picture = ?, category = ? WHERE idproduct = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProductName());
            stmt.setFloat(2, product.getPrice());
            stmt.setString(3, product.getDescription());
            stmt.setString(4, product.getPicture());
            stmt.setInt(5, product.getCategory());
            stmt.setInt(6, product.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteProduct(int productId) throws Exception {
        Connection conn = ConectionDB.obtenerInstancia();
        String sql = "DELETE FROM product WHERE idprodcut = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        }
    }
}
