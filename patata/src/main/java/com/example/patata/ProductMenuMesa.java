package com.example.patata;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductMenuMesa {
    private Stage stage;
    private Scene productMenuScene;

    private List<Button> products;
    private FlowPane buttonsContainer;
    private VBox recipe;

    private float total;
    private Label totalLabel;

    private Table selectedTable;

    private static final String GET_CATEGORIES_SQL = "SELECT categoryName FROM category";
    private static final String GET_PRODUCTS_BY_CATEGORY_SQL = "SELECT productName, price FROM product WHERE Category = (SELECT idCategory FROM category WHERE categoryName = ?)";

    public ProductMenuMesa(Stage stage, Table selectedTable) {
        this.stage = stage;
        this.selectedTable = selectedTable;
        initialize();
    }

    public void initialize() {
        BorderPane base = new BorderPane();
        BorderPane centerComponents = new BorderPane();
        BorderPane rightComponents = new BorderPane();
        HBox topBar = new HBox();
        recipe = new VBox();
        buttonsContainer = new FlowPane();
        HBox categoriesBar = new HBox();
        products = new ArrayList<>();
        totalLabel = new Label("Total: " + total);
        Button addToAccountButton = new Button("Agregar a cuenta");
        Button cleanButton = new Button("Limpiar");

        // Botón atrás
        Button backButton = new Button("<-");
        backButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        topBar.getChildren().addAll(backButton);

        backButton.setOnAction(e -> {
            WaiterTables waiterTables = new WaiterTables(stage);
            stage.setScene(waiterTables.getScene());
        });

        // Barra de categorías
        categoriesBar.setAlignment(Pos.CENTER);
        categoriesBarLogic(categoriesBar);
        categoriesBar.setStyle("-fx-background-color: #1976D2;");
        centerComponents.setTop(categoriesBar);

        // Contenedor de botones de productos
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.setHgap(10);
        buttonsContainer.setVgap(10);
        buttonsContainer.setPadding(new Insets(10));
        centerComponents.setCenter(buttonsContainer);

        // Contenedor de receta
        recipe.setStyle("-fx-background-color: #673AB7; -fx-padding: 10;");
        recipe.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, null, null)));
        recipe.setMinWidth(300);
        rightComponents.setTop(recipe);
        rightComponents.setCenter(totalLabel);
        HBox rightComponentsButtons = new HBox();
        rightComponentsButtons.getChildren().addAll(cleanButton, addToAccountButton);
        rightComponents.setBottom(rightComponentsButtons);
        rightComponents.setStyle("-fx-background-color: #2196F3; -fx-padding: 10;");


        base.setRight(rightComponents);
        base.setCenter(centerComponents);
        base.setTop(topBar);
        base.setStyle("-fx-background-color: #FFFFFF;");
        productMenuScene = new Scene(base, 1280, 720);

        // Acciones de botones
        cleanButton.setOnAction(e -> {
            recipe.getChildren().clear();
            total = 0;
            totalLabel.setText("Total: " + total);
        });

        addToAccountButton.setOnAction(e -> {
            if (selectedTable != null) {
                addProductsToAccount();
                showAlert("Productos agregados", "Los productos han sido agregados a la cuenta de la mesa " + selectedTable.getTableNumber());
                selectedTable.setStatus("Ocupada");
                updateTableStatus();
                WaiterTables waiterTables = new WaiterTables(stage);
                stage.setScene(waiterTables.getScene());
            }
        });
    }

    public Scene getScene() {
        return productMenuScene;
    }

    public void categoriesBarLogic(HBox categoriesBar) {
        try {
            Connection conn = ConectionDB.obtenerInstancia();
            try (PreparedStatement pstmt = conn.prepareStatement(GET_CATEGORIES_SQL);
                 ResultSet resultSet = pstmt.executeQuery()) {

                List<String> categories = new ArrayList<>();
                while (resultSet.next()) {
                    categories.add(resultSet.getString("categoryName"));
                }

                for (String category : categories) {
                    Button categoryButton = new Button(category);
                    categoryButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10;");
                    categoryButton.setOnAction(e -> {
                        loadProductsByCategory(category);
                        System.out.println("Categoría seleccionada: " + category);
                    });
                    categoriesBar.getChildren().add(categoryButton);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error al cargar categorías");
        }
    }

    private void loadProductsByCategory(String category) {
        new Thread(() -> {
            try {
                Connection conn = ConectionDB.obtenerInstancia();
                String sql = "SELECT productName, price, picture FROM product WHERE Category = (SELECT idCategory FROM category WHERE categoryName = ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, category);
                    try (ResultSet resultSet = pstmt.executeQuery()) {
                        List<Button> newProductButtons = new ArrayList<>();

                        while (resultSet.next()) {
                            String productName = resultSet.getString("productName");
                            float productPrice = resultSet.getFloat("price");
                            String picturePath = resultSet.getString("picture");

                            Button productButton = new Button(productName);
                            productButton.getStyleClass().add("button");
                            productButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10;");


                            if (picturePath != null && !picturePath.isEmpty()) {
                                Image productImage = new Image(picturePath);
                                ImageView imageView = new ImageView(productImage);
                                imageView.setFitWidth(50);
                                imageView.setFitHeight(50);
                                productButton.setGraphic(imageView);
                            }

                            newProductButtons.add(productButton);

                            productButton.setOnAction(e -> {
                                total += productPrice;
                                Platform.runLater(() -> {
                                    Label newLabel = new Label(String.format("Producto: %s Precio: %.2f", productName, productPrice));
                                    newLabel.getStyleClass().add("label");
                                    newLabel.setStyle("-fx-text-fill: white;");
                                    recipe.getChildren().add(newLabel);
                                    totalLabel.setText("Total: " + total);
                                });
                            });
                        }

                        Platform.runLater(() -> {
                            buttonsContainer.getChildren().clear();
                            buttonsContainer.getChildren().addAll(newProductButtons);
                            products.clear();
                            products.addAll(newProductButtons);
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showErrorDialog("Error al cargar productos"));
            }
        }).start();
    }

    private void addProductsToAccount() {
        try {
            Connection conn = ConectionDB.obtenerInstancia();
            String sql = "INSERT INTO sales (tableId, productName, price) VALUES (?, ?, ?)";
            Pattern pattern = Pattern.compile("Producto: (.+?) Precio: (.+)");
            for (Label productLabel : recipe.getChildren().stream().map(node -> (Label) node).toList()) {
                Matcher matcher = pattern.matcher(productLabel.getText());
                if (matcher.matches()) {
                    String productName = matcher.group(1);
                    float productPrice = Float.parseFloat(matcher.group(2));
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setInt(1, selectedTable.getId());
                        pstmt.setString(2, productName);
                        pstmt.setFloat(3, productPrice);
                        pstmt.executeUpdate();
                    }
                } else {
                    System.err.println("Error: el formato del texto del producto no es válido - " + productLabel.getText());
                    showErrorDialog("Error: el formato del texto del producto no es válido - " + productLabel.getText());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error al agregar productos a la cuenta");
        }
    }

    private void updateTableStatus() {
        try {
            Connection conn = ConectionDB.obtenerInstancia();
            String sql = "UPDATE tablesCustomers SET status = ? WHERE idTable = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, selectedTable.getStatus());
                pstmt.setInt(2, selectedTable.getId());
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error al actualizar el estado de la mesa");
        }
    }

    private void showErrorDialog(String message) {
        System.err.println(message);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}