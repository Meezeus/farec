package dudzinski.kacper.farec;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpScreenController implements Initializable {

    public Label operatorsLabel;
    public Label helpLabel;

    public void initialize(URL location, ResourceBundle resources) {
        String operatorLabelText = "Available regex operators:\n";
        for (Parser.REOperators operator: Parser.REOperators.values()){
            operatorLabelText += "  -" + operator + ", using the character \'" + Parser.getOperatorChar(operator) + "\'\n";
        }
        operatorsLabel.setText(operatorLabelText);

        helpLabel.setText("A valid regex expression can only contain letters, number, brackets and regex operators. " +
                "Valid expressions must have a root operator: an operator that is not contained within any brackets " +
                "(excluding outer brackets surrounding the whole expression). The regex expression is parsed from " +
                "left to right. As a result, expressions such as a+b+c are parsed as a+(b+c). When looking for a " +
                "root operator, the UNION and CONCATENATION operators are favoured over the STAR operator. As a " +
                "result, the expression a*+b is considered the same as (a*)+b.");
    }

}
