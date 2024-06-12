package com.example.patata;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Payment {
    Stage stage;
    private Scene paymentScene;
    private int tableId;
    private SalesDAO salesDAO;
    private TableDAO tableDAO;
    float payment = 0;
    float change = 0;
    float tip = 0;
    float total;
    float totalPlusTip;

    public Payment(Stage stage, int tableId) {
        this.stage = stage;
        this.tableId = tableId;
        this.salesDAO = new SalesDAO();
        this.tableDAO = new TableDAO();
        this.total = calculateTotalForTable(tableId); // Inicializa total después de inicializar salesDAO
        this.totalPlusTip = total;
        initialize();
    }

    public void initialize() {
        this.total = calculateTotalForTable(tableId);
        this.totalPlusTip = total;

        BorderPane base = new BorderPane();
        VBox centerComponents = new VBox(10);
        VBox topRight = new VBox(10);
        topRight.setPadding(new Insets(20));
        topRight.setMinWidth(300);
        VBox bottomRight = new VBox(10);

        Label subTotalLabel = new Label("Subtotal: $" + total);
        Label totalPlusTipLabel = new Label("Total: $" + totalPlusTip);
        Label paymentLabel = new Label("Pago: $");
        TextField paymentTextField = new TextField();
        Button addPaymentButton = new Button("Añadir Pago");
        Label tipLabel = new Label("Propina: $");
        TextField tipTextField = new TextField();
        Button tipButton = new Button("Añadir Propina");
        Label changeLabel = new Label("Cambio: $");
        Label changeNumber = new Label("0.0");

        Button payButton = new Button("Pagar");
        Button cancelButton = new Button("Cancelar");

        // Aplicar estilos Material Design
        subTotalLabel.setStyle("-fx-text-fill: #212121;");
        totalPlusTipLabel.setStyle("-fx-text-fill: #212121;");
        paymentLabel.setStyle("-fx-text-fill: #212121;");
        tipLabel.setStyle("-fx-text-fill: #212121;");
        changeLabel.setStyle("-fx-text-fill: #212121;");

        paymentTextField.setStyle("-fx-prompt-text-fill: #9E9E9E; -fx-background-color: #FFFFFF; -fx-border-color: #BDBDBD; -fx-border-width: 1px; -fx-border-radius: 2px; -fx-padding: 10px;");
        tipTextField.setStyle("-fx-prompt-text-fill: #9E9E9E; -fx-background-color: #FFFFFF; -fx-border-color: #BDBDBD; -fx-border-width: 1px; -fx-border-radius: 2px; -fx-padding: 10px;");

        addPaymentButton.setStyle("-fx-background-color: #6200EA; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 2px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 4, 0.5, 0, 2);");
        addPaymentButton.setOnMouseEntered(e -> addPaymentButton.setStyle("-fx-background-color: #3700B3;"));
        addPaymentButton.setOnMouseExited(e -> addPaymentButton.setStyle("-fx-background-color: #6200EA;"));

        tipButton.setStyle("-fx-background-color: #6200EA; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 2px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 4, 0.5, 0, 2);");
        tipButton.setOnMouseEntered(e -> tipButton.setStyle("-fx-background-color: #3700B3;"));
        tipButton.setOnMouseExited(e -> tipButton.setStyle("-fx-background-color: #6200EA;"));

        payButton.setStyle("-fx-background-color: #6200EA; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 2px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 4, 0.5, 0, 2);");
        payButton.setOnMouseEntered(e -> payButton.setStyle("-fx-background-color: #3700B3;"));
        payButton.setOnMouseExited(e -> payButton.setStyle("-fx-background-color: #6200EA;"));

        cancelButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 2px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 4, 0.5, 0, 2);");
        cancelButton.setOnMouseEntered(e -> cancelButton.setStyle("-fx-background-color: #E64A19;"));
        cancelButton.setOnMouseExited(e -> cancelButton.setStyle("-fx-background-color: #FF5722;"));


        cancelButton.setOnAction(e -> {
            WaiterTables waiterTables = new WaiterTables(stage);
            stage.setScene(waiterTables.getScene());
        });

        payButton.setOnAction(event -> {
            float changeAux = payment - totalPlusTip;
            if (changeAux >= 0) {
                try {
                    salesDAO.markSalesAsPaidForTable(tableId);
                    tableDAO.markTableAsAvailable(tableId);
                    CashierTables cashierTables = new CashierTables(stage);
                    stage.setScene(cashierTables.getScene());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert("Error", "No se pudo realizar el pago");
                }
            } else {
                showAlert("Error", "El pago no cubre el total de la cuenta");
            }
        });

        addPaymentButton.setOnAction(event -> {
            payment = Float.parseFloat(paymentTextField.getText());
            change = (payment - totalPlusTip);
            changeNumber.setText(Float.toString(change));
            changeLabel.setText("Cambio: $"+changeNumber.getText());
        });

        tipButton.setOnAction(event -> {
            tip = Float.parseFloat(tipTextField.getText());
            totalPlusTip = total + tip;
            totalPlusTipLabel.setText("Total: $" + totalPlusTip);
            change = (payment - totalPlusTip);
            changeNumber.setText(Float.toString(change));
            changeLabel.setText("Cambio: $"+changeNumber.getText());
        });


        centerComponents.getChildren().addAll(paymentLabel, paymentTextField, addPaymentButton,tipLabel, tipTextField, tipButton);
        topRight.getChildren().addAll(subTotalLabel,totalPlusTipLabel,changeLabel,payButton);


        base.setRight(topRight);
        base.setBottom(bottomRight);

        base.setCenter(centerComponents);
        base.setBottom(cancelButton);

        paymentScene = new Scene(base, 1280, 700);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return paymentScene;
    }

    private float calculateTotalForTable(int tableId) {
        float total = 0;
        try {
            total = salesDAO.getTotalSalesForTable(tableId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
}