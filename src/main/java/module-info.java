module com.example.ac {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;


    opens com.example.ac to javafx.fxml;
    exports com.example.ac;
}