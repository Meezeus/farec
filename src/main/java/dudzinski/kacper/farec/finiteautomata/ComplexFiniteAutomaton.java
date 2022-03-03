package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.layout.StackPane;

import java.util.ArrayList;

/**
 * This class represents a complex finite automaton. A complex finite automaton is made up of up to two finite automata
 * that have been combined in different ways. These finite automata may themselves be complex.
 */
public class ComplexFiniteAutomaton extends FiniteAutomaton {

    private StackPane leftFiniteAutomaton;
    private StackPane rightFiniteAutomaton;

    public ComplexFiniteAutomaton(StackPane initialState, StackPane finalState,
                                  StackPane leftFiniteAutomaton, ArrayList<StackPane> transitions, StackPane rightFiniteAutomaton,
                                  double minWidth, double minHeight) {
        this.initialState = initialState;
        this.finalState = finalState;
        this.leftFiniteAutomaton = leftFiniteAutomaton;
        this.rightFiniteAutomaton = rightFiniteAutomaton;

        if (transitions != null) {
            finiteAutomatonPane.getChildren().addAll(transitions);
        }
        finiteAutomatonPane.getChildren().addAll(leftFiniteAutomaton, rightFiniteAutomaton, initialState, finalState);
        finiteAutomatonPane.setMinSize(minWidth, minHeight);
        enableInitialState(true);
        enableFinalState(true);
    }

}
