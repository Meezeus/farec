package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.layout.StackPane;

/**
 * This class represents a simple finite automaton. A simple finite automaton is made up of only two states (an initial
 * state and a final state) with a labelled transition between them.
 */
public class SimpleFiniteAutomaton extends FiniteAutomaton {

    private StackPane transition;

    public SimpleFiniteAutomaton(StackPane initialState, StackPane finalState, StackPane transition, double minWidth, double minHeight) {
        this.initialState = initialState;
        this.finalState = finalState;
        this.transition = transition;
        finiteAutomatonPane.getChildren().addAll(transition, initialState, finalState);
        finiteAutomatonPane.setMinSize(minWidth, minHeight);
        enableInitialState(true);
        enableFinalState(true);
    }

}
