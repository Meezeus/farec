module dudzinski.kacper.farec {
    requires javafx.controls;
    requires javafx.fxml;

    opens dudzinski.kacper.farec to javafx.fxml;
    exports dudzinski.kacper.farec;
    exports dudzinski.kacper.farec.controllers;
    opens dudzinski.kacper.farec.controllers to javafx.fxml;
    exports dudzinski.kacper.farec.regex;
    opens dudzinski.kacper.farec.regex to javafx.fxml;
    exports dudzinski.kacper.farec.finiteautomata;
    opens dudzinski.kacper.farec.finiteautomata to javafx.fxml;
}