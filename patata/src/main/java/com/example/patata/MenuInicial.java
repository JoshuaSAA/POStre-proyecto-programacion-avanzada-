package com.example.patata;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class MenuInicial{

    Scene initialMenuScene;
    Stage stage;
    
    public MenuInicial(Stage stage){
        this.stage=stage;
        initialize();
    }

    public void initialize() {
        HBox base = new HBox(20);
        base.setAlignment(Pos.CENTER);

        Button cajaButton = createStyledButton("CAJA", "caja.png");
        Button meseroButton = createStyledButton("Meseros", "mesero2.png");
        Button corteCajaButton = createStyledButton("Corte de caja", "corte.png");
        Button baseDeDatosButton = createStyledButton("Base de datos", "baseDatos.png");

        cajaButton.setOnAction(e -> {
            CashierTablesAdmin cashierTablesAdmin = new CashierTablesAdmin(stage);
            stage.setScene(cashierTablesAdmin.getScene());
        });

        baseDeDatosButton.setOnAction(e -> {
            CRUDWindow crudWindow = new CRUDWindow(stage);
            stage.setScene(crudWindow.getScene());
        });

        meseroButton.setOnAction(e -> {
            WaiterTablesAdmin waiterTablesAdmin = new WaiterTablesAdmin(stage);
            stage.setScene(waiterTablesAdmin.getScene());
        });

        corteCajaButton.setOnAction(e -> {
            CorteCaja corteCaja = new CorteCaja();
            corteCaja.generarReportePDF();
            AlertBox.display("Corte", "Se a generado el corte correctamente");
        });

        base.getChildren().addAll(cajaButton, meseroButton, baseDeDatosButton, corteCajaButton);

        StackPane root = new StackPane(base);
        root.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 20px;");

        initialMenuScene = new Scene(root, 1280, 720);
    }

    private Button createStyledButton(String text, String iconFileName) {
        Button button = new Button(text);
        button.setPrefSize(400, 400);
        System.out.println(iconFileName);
        String originalStyle = "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;";
        button.setStyle(originalStyle);

        ImageView icon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/"+iconFileName))));
        icon.setFitWidth(250);
        icon.setFitHeight(250);
        button.setGraphic(icon);

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #1976D2;"));
        button.setOnMouseExited(e -> button.setStyle(originalStyle)); // Restablecer el estilo original

        return button;
    }



    public Scene getScene(){
        return initialMenuScene;
    }

}
