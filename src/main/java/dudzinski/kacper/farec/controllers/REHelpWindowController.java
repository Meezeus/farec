package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.regex.RegexOperator;
import dudzinski.kacper.farec.regex.RegexOperatorChars;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller for the regular expression help window. The help
 * window is displayed when the help button is pressed in the view for creating
 * regular expressions. It contains information to help the user create valid
 * regular expressions.
 */
public class REHelpWindowController implements Initializable {

    public Label operatorsLabel;
    public Label helpLabel;

    /**
     * Sets the labels in the help window.
     */
    public void initialize(URL location, ResourceBundle resources) {
        // Set the label showing the different regex operators.
        StringBuilder operatorLabelText =
                new StringBuilder("Available regex operators:\n");
        for (RegexOperator operator : RegexOperator.values()) {
            operatorLabelText.append("  -").append(operator)
                             .append(", using the character '")
                             .append(RegexOperatorChars
                                             .getCharFromOperator(operator))
                             .append("'\n");
        }
        operatorsLabel.setText(operatorLabelText.toString());

        // Set the help label.
        helpLabel.setText(
                """
                A valid regular expression can only contain letters, numbers, \
                brackets and regex operators.
                    
                Valid regular expressions must have a root operator: an \
                operator that is not contained within any brackets (excluding \
                outer brackets surrounding the whole expression).
                    
                The STAR operator has the highest precedence, then \
                CONCATENATION, then UNION. This means that, for example, the \
                regular expression a|b+c|d will be parsed as (a|b)+(c|d), and \
                the regular expression a|b*+c will be parsed as (a|(b*))+c.
                    
                When looking for the root operator, the regular expression is \
                parsed from right to left. As a result, expressions such as \
                a+b+c are parsed as (a+b)+c.
                """
        );
    }

}
