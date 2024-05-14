module com.example.gymmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires fontawesomefx;
    requires java.sql;
    requires mysql.connector.java;


    opens com.example.gymmanagementsystem to javafx.fxml;
    exports com.example.gymmanagementsystem;
}