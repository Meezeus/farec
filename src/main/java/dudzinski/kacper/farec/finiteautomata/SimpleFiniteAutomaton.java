package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.layout.StackPane;

/**
 * This class represents a simple finite automaton. A simple finite automaton is made up of only two states (an initial
 * state and a final state) with a labelled transition between them.
 */
public class SimpleFiniteAutomaton extends FiniteAutomaton {

    public SimpleFiniteAutomaton(State initialState, State finalState, StackPane transition, double minWidth, double minHeight) {
        this.initialState = initialState;
        this.finalState = finalState;
        finiteAutomatonPane.getChildren().addAll(transition, initialState.getStatePane(), finalState.getStatePane());
        finiteAutomatonPane.setMinSize(minWidth, minHeight);
        enableInitialState(true);
        enableFinalState(true);
    }

}
