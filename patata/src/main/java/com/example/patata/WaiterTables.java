package com.example.patata;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class WaiterTables {
    private TableDAO tableDAO;
    private Stage stage;
    private Scene scene;
    private Table selectedTable;

    public WaiterTables(Stage stage) {
        this.tableDAO = new TableDAO();
        this.stage = stage;
        initialize();
    }

    public void initialize() {
        BorderPane base = new BorderPane();
        base.setStyle("-fx-background-color: #FFFFFF;");

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #1976D2; -fx-padding: 10px;");
        topBar.setMinHeight(40);

        Label titleLabel = new Label("Mesero");
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-font-size: 20px;");

        Button backButton = new Button("Cerrar sesión");
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px;");
        backButton.setOnAction(e -> {
            LogIn logIn = new LogIn(stage);
            stage.setScene(logIn.getScene());
        });

        HBox.setHgrow(backButton, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(titleLabel, spacer, backButton);

        ListView<Label> listView = new ListView<>();
        listView.setStyle("-fx-background-color: #FFFFFF;");
        listView.setPrefWidth(400);
        listView.setPrefHeight(500);
        listView.setPadding(new Insets(10));

        try {
            List<Table> tables = tableDAO.getAllTables();
            for (Table table : tables) {
                Label tableLabel = new Label("Mesa " + table.getTableNumber());
                if ("Disponible".equalsIgnoreCase(table.getStatus())) {
                    tableLabel.setTextFill(Color.GREEN);
                } else if ("Ocupada".equalsIgnoreCase(table.getStatus())) {
                    tableLabel.setTextFill(Color.ORANGE);
                }
                listView.getItems().add(tableLabel);

                tableLabel.setOnMouseClicked(event -> {
                    selectedTable = table;
                    listView.getSelectionModel().select(tableLabel);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button addButton = new Button("Agregar Productos");
        addButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        addButton.setOnAction(e -> {
            if (selectedTable != null) {
                ProductMenuMesa productMenuMesa = new ProductMenuMesa(stage, selectedTable);
                stage.setScene(productMenuMesa.getScene());
            } else {
                showAlert("Seleccione una mesa", "Por favor, seleccione una mesa antes de continuar.");
            }
        });

        Button addTableButton = new Button("Agregar Mesa");
        addTableButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        addTableButton.setOnAction(e -> {
            try {
                tableDAO.addTable();
                refreshTablesList(listView); // Aquí refrescamos la lista de mesas
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "No se pudo agregar la mesa");
            }
        });



        HBox bottomButtons = new HBox(10);
        bottomButtons.setAlignment(Pos.CENTER_RIGHT);
        bottomButtons.setPadding(new Insets(10));
        bottomButtons.getChildren().addAll(addButton, addTableButton);

        base.setTop(topBar);
        base.setCenter(listView);
        base.setBottom(bottomButtons);

        scene = new Scene(base, 1280, 700);
    }


    private void refreshTablesList(ListView<Label> listView) {
        listView.getItems().clear();
        try {
            List<Table> tables = tableDAO.getAllTables();
            for (Table table : tables) {
                Label tableLabel = new Label("Mesa " + table.getTableNumber());
                if ("Disponible".equalsIgnoreCase(table.getStatus())) {
                    tableLabel.setTextFill(Color.GREEN);
                } else if ("Ocupada".equalsIgnoreCase(table.getStatus())) {
                    tableLabel.setTextFill(Color.ORANGE);
                }
                listView.getItems().add(tableLabel);

                tableLabel.setOnMouseClicked(event -> {
                    selectedTable = table;
                    listView.getSelectionModel().select(tableLabel);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}
