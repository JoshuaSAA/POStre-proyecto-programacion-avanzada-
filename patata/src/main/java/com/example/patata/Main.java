package com.example.patata;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    @Override
    public void start(Stage window) throws IOException {
        window.setTitle("POS-tre");
      LogIn logIn = new LogIn(window);
      window.setScene(logIn.getScene());
        window.show();
    }

    public static void main(String[] args) {
        launch();
    }
}