module com.example.fiftypoker {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.fiftypoker.controllers to javafx.fxml;

    exports com.example.fiftypoker;
}
