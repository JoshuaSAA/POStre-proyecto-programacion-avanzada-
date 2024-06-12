
package com.example.patata;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class LogIn {

    private static Connection conn=null;
    Stage stage;
    private Scene logIn;
    public LogIn(Stage stage){
        this.stage=stage;
        initialize();
    }
    /* public void initialize(){
         VBox base = new VBox();
         Button logInButton = new Button("Iniciar sesión");
         TextField user = new TextField();
         PasswordField pass = new PasswordField();
         Label welcomeMessage = new Label("Bienvenido");

         user.setPromptText("Usuario");
         pass.setPromptText("Contraseña");

         logInButton.setOnAction(e ->{
         logInLogic(user.getText(), pass.getText());
             System.out.println("lOGIN");
         });


         base.getChildren().addAll(welcomeMessage,user,pass,logInButton);
         logIn = new Scene(base);
     }
 */
    public void initialize() {
        VBox base = new VBox(10);
        base.setPadding(new Insets(20));
        base.setStyle("-fx-background-color: #fafafa;");

        Label welcomeMessage = new Label("Bienvenido");
        welcomeMessage.setFont(Font.font("Arial", 24));

        TextField user = new TextField();
        user.setPromptText("Usuario");
        user.setStyle("-fx-prompt-text-fill: #757575; -fx-font-size: 14px;");

        PasswordField pass = new PasswordField();
        pass.setPromptText("Contraseña");
        pass.setStyle("-fx-prompt-text-fill: #757575; -fx-font-size: 14px;");

        Button logInButton = new Button("Iniciar sesión");
        logInButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        logInButton.setOnAction(e -> logInLogic(user.getText(), pass.getText()));

        base.getChildren().addAll(welcomeMessage, user, pass, logInButton);
        StackPane root = new StackPane(base);
        logIn = new Scene(root, 300, 200);

    }
    public void logInLogic(String user, String password){
        try {
            Connection conn = ConectionDB.obtenerInstancia();
            String hashedPassword = hashPassword(password);

            String sql = "SELECT * FROM users WHERE credential = ? AND password = ?";
            try(PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, user);
                pstmt.setString(2, hashedPassword);

                try(ResultSet resultSet = pstmt.executeQuery()){
                    if(resultSet.next()){
                        String userType = resultSet.getString("type");
                        System.out.println("Login Successfully");

                        // Redirige a diferentes escenas según el tipo de usuario
                        if ("admin".equalsIgnoreCase(userType)) {
                            MenuInicial menuInicial = new MenuInicial(stage);
                            stage.setScene(menuInicial.getScene());
                        } else if ("casher".equalsIgnoreCase(userType)) {
                            CashierTables cashierTables = new CashierTables(stage);
                            stage.setScene(cashierTables.getScene());
                        }  else if ("waiter".equalsIgnoreCase(userType)) {
                            WaiterTables waiterTables = new WaiterTables(stage);
                            stage.setScene(waiterTables.getScene());
                        }else {
                            System.out.println("Tipo de usuario no reconocido");
                        }
                    }else{
                        AlertBox alertBox = new AlertBox();
                        alertBox.display("Datos incorrectos","Favor de ingresar los datos correctos");
                        System.out.println("Datos incorrectos");
                    }
                }
            }
        } catch (SQLException | NoSuchAlgorithmException ex){
            ex.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha256.digest(password.getBytes());
        return bytesToHex(hash);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public Scene getScene(){return logIn;}
}
