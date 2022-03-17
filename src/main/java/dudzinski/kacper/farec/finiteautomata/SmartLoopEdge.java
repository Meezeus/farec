package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.control.Label;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;

/**
 * This class represents a smart loop edge in a finite automaton. A smart edge
 * is an edge that manages its own size, position, relationship to other
 * components etc. This makes it much more dynamic.
 * <p>
 * A loop edge is a directed, labelled curve that connect a state to itself. It
 * represents a transition between the state and itself. The loop for a
 * particular state is unique: there cannot be any other loops for that state.
 * The curve may go above or below the state.
 */
public class SmartLoopEdge extends SmartEdge {

    private boolean topside = true;

    /**
     * Creates a loop edge for the given state.
     *
     * @param edgeShape the curve of this loop edge
     * @param arrowhead the arrowhead at the end of this loop edge
     * @param label     the label on this loop edge
     * @param state     the state this loop edge is connected to
     */
    public SmartLoopEdge(CubicCurve edgeShape, Polygon arrowhead, Label label,
                         SmartState state) {
        super(edgeShape, arrowhead, label, state, state);
    }

    /**
     * Flips the position of this loop edge from the top of the state to the
     * bottom, and vice versa.
     */
    public void flip() {
        topside = !topside;
        SmartFiniteAutomatonBuilder.setLoopEdgeBindings(
                (CubicCurve) edgeShape, arrowhead, label, startState, topside);
    }

}
