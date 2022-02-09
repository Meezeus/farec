package dudzinski.kacper.farec;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateREScreenController {

    private FXMLLoader fxmlLoader;
    public TextField reInputField;
    public Button helpButton;
    public Button parseButton;
    public Label infoLabel;
    public StackPane parseTreeContainer;

    public void openHelpWindow() throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Help: Regular Expressions");
        fxmlLoader = new FXMLLoader(App.class.getResource("help_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        window.setScene(scene);
        window.showAndWait();
    }

    public void parseRE(){
        String regexString = reInputField.getText().replaceAll("\\s+","").trim();
        try {
            RegularExpression regex = Parser.parse(regexString);
            infoLabel.setText("Regular expression is valid!");
            ParseTree parseTree = new ParseTree(regex);
            parseTreeContainer.getChildren().clear();
            parseTreeContainer.getChildren().add(parseTree);
        }
        catch (IllegalArgumentException e){
            infoLabel.setText(e.getMessage());
            parseTreeContainer.getChildren().clear();
        }
    }

}
