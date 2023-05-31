module com.example.login1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.login1 to javafx.fxml;
    exports com.example.login1;
}