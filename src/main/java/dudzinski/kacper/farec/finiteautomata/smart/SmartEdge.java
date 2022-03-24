package dudzinski.kacper.farec.finiteautomata.smart;

import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 * This class represents a smart edge in a finite automaton. A smart edge is an
 * edge that manages its own size, position, relationship to other components
 * etc. This makes it much more dynamic.
 * <p>
 * An edge is a directed, labelled line or curve that connects two different
 * states. An edge may be straight or curved. If there are edges between two
 * states in both directions, these edges are called symmetric edges. Symmetric
 * edges are curves. Otherwise, the edge is a straight line.
 *
 * @see SmartEdgeComponent
 * @see SmartLoopEdge
 */
public class SmartEdge extends SmartEdgeComponent {

    private boolean isCurved = false;

    /**
     * Creates an edge between two different states.
     *
     * @param edgeShape  the shape of the edge (a line or a curve)
     * @param arrowhead  the arrowhead at the end of the edge
     * @param label      the label on the edge
     * @param startState the start state of the edge
     * @param endState   the end state of the edge
     * @throws IllegalArgumentException if the two states are the same
     */
    public SmartEdge(Shape edgeShape, Polygon arrowhead, Label label,
                     SmartState startState, SmartState endState)
            throws IllegalArgumentException {
        super(edgeShape, arrowhead, label, startState, endState);

        if (startState == endState) {
            throw new IllegalArgumentException(
                    "The two states cannot be the same!");
        }
    }

    /**
     * Sets the edge as a curve or a straight line. If <code>setCurve</code> is
     * true, the edge will be a curve. If <code>setCurve</code> is false, the
     * edge will be a straight line.
     *
     * @param setCurve whether to set the edge as a curve (true), or a straight
     *                 line (false)
     */
    public void setCurved(boolean setCurve) {
        // Check if anything needs to be done.
        if ((setCurve && !isCurved) || (!setCurve && isCurved)) {
            isCurved = setCurve;

            // Create a new edge that has the correct line shape.
            SmartEdge newEdge;
            if (setCurve) {
                newEdge = SmartFiniteAutomatonBuilder.createCurvedEdge(
                        label.getText(), startState, endState);
            }
            else {
                newEdge = SmartFiniteAutomatonBuilder.createEdge(
                        label.getText(), startState, endState);
            }

            // Set the stroke of the new edge to the stroke of this edge.
            newEdge.setStroke(this.getStroke());

            // Steal the components of the new edge.
            edgeShape = newEdge.edgeShape;
            arrowhead = newEdge.arrowhead;
            label = newEdge.label;

            // Clear the container and add the new components.
            container.getChildren().clear();
            container.getChildren().addAll(edgeShape,
                                           arrowhead,
                                           label);
        }
    }

}
