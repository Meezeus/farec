package dudzinski.kacper.farec.controllers;

import javafx.fxml.FXML;
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
public final class FAHelpWindowController implements Initializable {

    @FXML
    private Label helpLabel;

    /**
     * Sets the labels in the help window.
     */
    public void initialize(URL location, ResourceBundle resources) {
        // Set the help label.
        helpLabel.setText("""
            A finite automaton is made up of states with edges between \
            them.
                        
            To create a state, left-click on the workspace while in \
            STATE mode.
                      
            All finite automata must contain an initial state and a final \
            state.
                        
            All states in the finite automaton must be reachable from the \
            initial state.
                        
            To create an edge between two states, while in EDGE mode, hold \
            down left-click while on top of the start state and then \
            release while on top of the end state.
                        
            The label on an edge must be an alphanumeric character, the \
            empty string symbol, or a comma-separated list of the two.
            """);
    }

}
