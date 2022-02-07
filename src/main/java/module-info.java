module dudzinski.kacper.farec {
    requires javafx.controls;
    requires javafx.fxml;

    opens dudzinski.kacper.farec to javafx.fxml;
    exports dudzinski.kacper.farec;
}