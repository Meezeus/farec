package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.layout.StackPane;

import java.util.ArrayList;

/**
 * This class represents a complex finite automaton. A complex finite automaton is made up of up to two finite automata
 * that have been combined in different ways. These finite automata may themselves be complex.
 */
public class ComplexFiniteAutomaton extends FiniteAutomaton {

    /**
     * Create a complex finite automaton.
     *
     * @param initialState     The initial state of the complex finite automaton.
     * @param finalState       The final state of the complex finite automaton.
     * @param finiteAutomaton1 The finite automaton used in the construction of the complex finite automaton.
     * @param transitions      List of transitions used in the construction of the complex finite automaton.
     * @param finiteAutomaton2 The final automaton used in the construction of the complex finite automaton. May be null.
     * @param minWidth         The minimum width of the complex finite automaton.
     * @param minHeight        The minimum height of the complex finite automaton.
     */
    public ComplexFiniteAutomaton(State initialState, State finalState,
                                  StackPane finiteAutomaton1, ArrayList<Edge> transitions, StackPane finiteAutomaton2,
                                  double minWidth, double minHeight) {
        this.initialState = initialState;
        this.finalState = finalState;

        if (transitions != null) {
            transitions.forEach(transition -> finiteAutomatonPane.getChildren().add(transition.getPane()));
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
