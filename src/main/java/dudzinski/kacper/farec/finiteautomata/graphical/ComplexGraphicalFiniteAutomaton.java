package dudzinski.kacper.farec.finiteautomata.graphical;

import javafx.scene.layout.StackPane;

import java.util.ArrayList;

/**
 * This class represents a complex graphical finite automaton. A complex finite
 * automaton is a finite automaton that has been created from other finite
 * automata, using the construction rule of a regex operator. Up to two other
 * finite automata may be used to create the complex finite automaton. A complex
 * finite automaton is made up of multiple states and edges.
 *
 * @see GraphicalFiniteAutomaton
 * @see SimpleGraphicalFiniteAutomaton
 */
public class ComplexGraphicalFiniteAutomaton extends GraphicalFiniteAutomaton {

    /**
     * Create a complex finite automaton.
     *
     * @param initialState the initial state of this finite automaton
     * @param finalState   the final state of this finite automaton
     * @param faContainer1 the container of one of the finite automata used in
     *                     the construction of this finite automaton
     * @param edges        the list of edges used in the construction of this
     *                     finite automaton (might be empty)
     * @param faContainer2 the container of one of the final automata used in
     *                     the construction of this finite automaton (might be
     *                     <code>null</code>)
     * @param minWidth     the minimum width of this finite automaton
     * @param minHeight    the minimum height of this finite automaton
     */
    public ComplexGraphicalFiniteAutomaton(GraphicalState initialState,
                                           GraphicalState finalState,
                                           StackPane faContainer1,
                                           ArrayList<GraphicalEdge> edges,
                                           StackPane faContainer2,
                                           double minWidth, double minHeight) {
        // Set the initial state and the final state.
        this.initialState = initialState;
        this.finalState = finalState;

        // Add the transition containers to the container of this finite
        // automaton.
        edges.forEach(transition ->
                              container.getChildren().
                                       add(transition.getContainer()));
        // If the second finite automaton container is not null, add it to the
        // container of this finite automaton.
        if (faContainer2 != null) {
            container.getChildren().add(faContainer2);
        }
        // Add the first finite automaton container as well as the containers of
        // the initial state and final state to this finite automaton.
        container.getChildren().addAll(faContainer1,
                                       initialState.getContainer(),
                                       finalState.getContainer());

        // Set the minimum width and height of this finite automaton.
        container.setMinSize(minWidth, minHeight);

        /* These two methods must be called after the initial state and final
        state containers have been added to the container of the finite
        automaton. */
        enableInitialState(true);
        enableFinalState(true);
    }

}
