module com.example.ac {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ac to javafx.fxml;
    exports com.example.ac;
}