package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.layout.StackPane;

import java.util.ArrayList;

/**
 * This class represents a complex finite automaton. A complex finite automaton is made up of up to two finite automata
 * that have been combined in different ways. These finite automata may themselves be complex.
 */
public class ComplexFiniteAutomaton extends FiniteAutomaton {

    public ComplexFiniteAutomaton(State initialState, State finalState,
                                  StackPane finiteAutomaton1, ArrayList<StackPane> transitions, StackPane finiteAutomaton2,
                                  double minWidth, double minHeight) {
        this.initialState = initialState;
        this.finalState = finalState;

        if (transitions != null) {
            finiteAutomatonPane.getChildren().addAll(transitions);
        }
        if (finiteAutomaton2 != null) {
            finiteAutomatonPane.getChildren().add(finiteAutomaton2);
        }
        finiteAutomatonPane.getChildren().addAll(finiteAutomaton1, initialState.getPane(), finalState.getPane());
        finiteAutomatonPane.setMinSize(minWidth, minHeight);
        enableInitialState(true);
        enableFinalState(true);
    }

}
