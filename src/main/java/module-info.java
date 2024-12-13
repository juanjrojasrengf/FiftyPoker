module com.example.fiftypoker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.example.fiftypoker.controllers to javafx.fxml;

    exports com.example.fiftypoker;
}
