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

    public ComplexFiniteAutomaton(StackPane initialState, StackPane finalState, ArrayList<StackPane> transitions, StackPane leftFiniteAutomaton, StackPane rightFiniteAutomaton) {
        this.initialState = initialState;
        this.finalState = finalState;
        this.leftFiniteAutomaton = leftFiniteAutomaton;
        this.rightFiniteAutomaton = rightFiniteAutomaton;
        finiteAutomatonContainer.getChildren().addAll(transitions);     // Has to be first so edges are hidden behind nodes.
        finiteAutomatonContainer.getChildren().addAll(initialState, finalState, leftFiniteAutomaton, rightFiniteAutomaton);
    }

}
