package dudzinski.kacper.farec;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the screen used to create regular expressions.
 */
public class CreateREScreenController implements Initializable {

    private FXMLLoader fxmlLoader;
    public TextField reInputField;
    public Button helpButton;
    public Button parseButton;
    public Label infoLabel;
    public StackPane parseTreeContainer;

    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> reInputField.requestFocus());   // Make sure the text field starts off focused.
    }

    /**
     * This method is called when the Help button is pressed. It opens a small window with help information.
     * @throws IOException
     */
    public void openHelpWindow() throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Help: Regular Expressions");
        fxmlLoader = new FXMLLoader(App.class.getResource("help_window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * This method is called when the Parse button is pressed. It will get the parser to parse the regular expression
     * entered in the text field. If the expression is valid, a parse tree is built and displayed and the info label is
     * updated accordingly. If the expression is invalid, an error message is shown and any currently displayed parse
     * tree is removed.
     */
    public void parseRE(){
        parseTreeContainer.getChildren().clear();
        String regexString = reInputField.getText().replaceAll("\\s+","").trim();
        try {
            RegularExpression regex = Parser.parse(regexString);
            infoLabel.setText("Regular expression is valid!");
            ParseTree parseTree = new ParseTree(regex);
            parseTreeContainer.getChildren().add(parseTree);
        }
        catch (IllegalArgumentException e){
            infoLabel.setText(e.getMessage());
        }
    }

}
