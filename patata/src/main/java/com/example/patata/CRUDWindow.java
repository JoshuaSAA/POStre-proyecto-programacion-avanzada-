package com.example.patata;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class CRUDWindow {
    Scene CRUDscene;
    Stage stage;
    private TableView<Product> table = new TableView<>();
    private ObservableList<Product> data;
    private String imagePath;
    public CRUDWindow(Stage stage){
        this.stage=stage;
        initialize();
    }
    public void initialize(){

        BorderPane base = new BorderPane();
        Pane center = new Pane();
        VBox rightComponents = new VBox();
        HBox bottom = new HBox();
        HBox topBar = new HBox();


        rightComponents.setPadding(new Insets(15));
        // Labels and TextFields
        Label nameLabel = new Label("Product Name:");
        TextField nameInput = new TextField();

        Label priceLabel = new Label("Price:");
        TextField priceInput = new TextField();

        Label descriptionLabel = new Label("Description:");
        TextField descriptionInput = new TextField();

        Label pictureLabel = new Label("Picture:");
        Button pictureButton = new Button("Choose Image");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        pictureButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                imagePath = selectedFile.toURI().toString();
                Image image = new Image(imagePath);
                imageView.setImage(image);
            }
        });

        Label categoryLabel = new Label("Category:");
        TextField categoryInput = new TextField();

        // Buttons
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            try {
                Product product = new Product(0, nameInput.getText(), Float.parseFloat(priceInput.getText()), descriptionInput.getText(), imagePath, Integer.parseInt(categoryInput.getText()));
                new ProductDAO().addProduct(product);
                loadProducts();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            try {
                Product selectedProduct = table.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    selectedProduct.setProductName(nameInput.getText());
                    selectedProduct.setPrice(Float.parseFloat(priceInput.getText()));
                    selectedProduct.setDescription(descriptionInput.getText());
                    selectedProduct.setPicture(imagePath);
                    selectedProduct.setCategory(Integer.parseInt(categoryInput.getText()));
                    new ProductDAO().updateProduct(selectedProduct);
                    loadProducts();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            try {
                Product selectedProduct = table.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    new ProductDAO().deleteProduct(selectedProduct.getId());
                    loadProducts();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Product selectedProduct = table.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    nameInput.setText(selectedProduct.getProductName());
                    priceInput.setText(String.valueOf(selectedProduct.getPrice()));
                    descriptionInput.setText(selectedProduct.getDescription());
                    imagePath = selectedProduct.getPicture();
                    if (imagePath != null && !imagePath.isEmpty()) {
                        Image image = new Image(imagePath);
                        imageView.setImage(image);
                    } else {
                        imageView.setImage(null);
                    }
                    categoryInput.setText(String.valueOf(selectedProduct.getCategory()));
                }
            }
        });

        // Table columns
        TableColumn<Product, String> nameColumn = new TableColumn<>("Product Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));

        TableColumn<Product, Float> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getPrice()).asObject());

        TableColumn<Product, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        TableColumn<Product, String> pictureColumn = new TableColumn<>("Picture");
        pictureColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPicture()));

        TableColumn<Product, Integer> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCategory()).asObject());

        table.getColumns().addAll(nameColumn, priceColumn, descriptionColumn, pictureColumn, categoryColumn);


        // cosntruction

        //topbar
        Button backButton = new Button("<-");
        topBar.getChildren().addAll(backButton);

        backButton.setOnAction(e -> {
            MenuInicial menuInicial = new MenuInicial(stage);
            stage.setScene(menuInicial.getScene());
        });

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
            nameInput.clear();
            priceInput.clear();
            descriptionInput.clear();
            categoryInput.clear();
            imageView.setImage(null);
            imagePath = null;
        });

        table.setMinWidth(1000);
        table.setMinHeight(600);
        rightComponents.setMinWidth(200);
        bottom.setAlignment(Pos.CENTER);


        center.getChildren().addAll(table);
        bottom.getChildren().addAll(addButton,updateButton,deleteButton,clearButton);
        rightComponents.getChildren().addAll(nameLabel, nameInput, priceLabel, priceInput, descriptionLabel, descriptionInput, pictureLabel, pictureButton, imageView, categoryLabel, categoryInput);
        base.setTop(topBar);
        base.setCenter(center);
        base.setRight(rightComponents);
        base.setBottom(bottom);
        CRUDscene = new Scene(base, 1280, 720);

        loadProducts();
    }
    private void loadProducts() {
        try {
            List<Product> products = new ProductDAO().getAllProducts();
            data = FXCollections.observableArrayList(products);
            table.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Scene getScene(){
        return CRUDscene;
    }
}
