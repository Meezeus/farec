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
    exports dudzinski.kacper.farec.finiteautomata.smart;
    opens dudzinski.kacper.farec.finiteautomata.smart to javafx.fxml;
    exports dudzinski.kacper.farec.finiteautomata.graphical;
    opens dudzinski.kacper.farec.finiteautomata.graphical to javafx.fxml;
}