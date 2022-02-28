package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.RegexOperator;
import dudzinski.kacper.farec.RegexOperatorChars;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller for the help window.
 */
public class HelpWindowController implements Initializable {

    public Label operatorsLabel;
    public Label helpLabel;

    /**
     * Sets the labels in the help window.
     */
    public void initialize(URL location, ResourceBundle resources) {
        String operatorLabelText = "Available regex operators:\n";
        for (RegexOperator operator: RegexOperator.values()){
            operatorLabelText += "  -" + operator + ", using the character \'" + RegexOperatorChars.getCharFromOperator(operator) + "\'\n";
        }
        operatorsLabel.setText(operatorLabelText);

        helpLabel.setText("A valid regular expression can only contain letters, number, brackets and regex operators. " +
                "Valid regular expressions must have a root operator: an operator that is not contained within any " +
                "brackets (excluding outer brackets surrounding the whole expression). The regular expression is parsed " +
                "from left to right. As a result, expressions such as a+b+c are parsed as a+(b+c). When looking for a " +
                "root operator, the UNION and CONCATENATION operators are favoured over the STAR operator. As a result, " +
                "the expression a*+b is considered the same as (a*)+b.");
    }

}
