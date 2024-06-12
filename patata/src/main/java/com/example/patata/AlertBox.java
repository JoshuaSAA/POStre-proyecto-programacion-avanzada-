package com.example.patata;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public static void display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMaxWidth(400);

        Label messageDisplay = new Label(message);
        messageDisplay.setStyle("-fx-font-size: 16px;");

        Button closeButton = new Button("Cerrar");
        closeButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(20);
        layout.getChildren().addAll(messageDisplay, closeButton);
        layout.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(layout);
        root.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 20px;");

        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();
    }

}