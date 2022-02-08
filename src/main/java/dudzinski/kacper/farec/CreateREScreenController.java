package dudzinski.kacper.farec;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateREScreenController {

    public Parser parser = new Parser();
    public TextField reInputField;
    public Button parseButton;
    public Label infoLabel;

    public void parseRE(){
        String regexString = reInputField.getText().replaceAll("\\s+","").trim();
        System.out.println(regexString);
        try {
            RegularExpression regex = parser.parse(regexString);
            System.out.println(regex);
            infoLabel.setText("Regular expression is valid!");
        }
        catch (IllegalArgumentException e){
            infoLabel.setText("Regular expression is invalid!");
        }
    }

}
