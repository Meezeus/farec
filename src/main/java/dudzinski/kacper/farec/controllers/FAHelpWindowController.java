package dudzinski.kacper.farec.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller for the finite automata help window. The help window
 * is displayed when the help button is pressed in the view for creating finite
 * automata. It contains information to help the user create valid finite
 * automata.
 */
public class FAHelpWindowController implements Initializable {

    public Label helpLabel;

    /**
     * Sets the labels in the help window.
     */
    public void initialize(URL location, ResourceBundle resources) {
        // Set the help label.
        helpLabel.setText(
                """
                A finite automaton is made up of states with edges between \
                them.
                 
                All finite automata must contain an initial state and a final \
                state.
                                
                All states in the finite automaton must be reachable from the \
                initial state.
                                
                The label on an edge may only be an alphanumeric character, \
                the special empty string character, or a combination of these \
                using the UNION operator.
                """
        );
    }

}
