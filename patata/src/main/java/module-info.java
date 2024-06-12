module com.example.patata {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.pdfbox;
    requires com.jfoenix;


    opens com.example.patata to javafx.fxml;
    exports com.example.patata;
}