package com.example.patata;



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

public class CashierTablesAdmin {
    private TableDAO tableDAO;
    private Stage stage;
    private Scene scene;
    private Table selectedTable;

    public CashierTablesAdmin(Stage stage) {
        this.tableDAO = new TableDAO();
        this.stage = stage;
        initialize();
    }

    public void initialize() {
        BorderPane base = new BorderPane();
        base.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10px;");

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 10, 10, 10));
        topBar.setStyle("-fx-background-color: #1976D2;");

        Label titleLabel = new Label("Mesas");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");

        Region spacer = new Region(); // Región para empujar el botón "Cerrar sesión" hacia la derecha
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeSesionButton = new Button("Cerrar sesión");
        closeSesionButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px;");
        closeSesionButton.setOnAction(e -> {
            LogIn logIn = new LogIn(stage);
            stage.setScene(logIn.getScene());
        });
        Button backButton = new Button("<-");
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px;");
        backButton.setOnAction(e -> {
            MenuInicial menuInicial = new MenuInicial(stage);
            stage.setScene(menuInicial.getScene());
        });

        topBar.getChildren().addAll(backButton,titleLabel, spacer, closeSesionButton); // Agregamos la región entre el título y el botón

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

        Button chargeButton = new Button("Cobrar");
        chargeButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");
        chargeButton.setOnAction(e -> {
            if (selectedTable != null) {
                Payment paymentScene = new Payment(stage, selectedTable.getId());
                stage.setScene(paymentScene.getScene());
            } else {
                showAlert("Seleccione una mesa", "Por favor, seleccione una mesa antes de continuar.");
            }
        });

        HBox bottomButtons = new HBox();
        bottomButtons.setAlignment(Pos.CENTER_RIGHT);
        bottomButtons.setPadding(new Insets(10));
        bottomButtons.getChildren().addAll(chargeButton);

        base.setTop(topBar);
        base.setCenter(listView);
        base.setBottom(bottomButtons);
        BorderPane.setMargin(bottomButtons, new Insets(10));

        scene = new Scene(base, 1280, 700);
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
