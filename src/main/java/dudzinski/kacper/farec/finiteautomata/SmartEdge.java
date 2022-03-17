package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 * This class represents a smart edge in a finite automaton. A smart edge is an
 * edge that manages its own size, position, relationship to other components
 * etc. This makes it much more dynamic.
 * <p>
 * An edge is a directed, labelled line between two states. It represents a
 * transitions between the two states. An edge between two states in a
 * particular direction is unique: there cannot be any other edges between those
 * two states in the same direction. The edge is usually a straight line, but
 * may be a curve instead (see below).
 * <p>
 * If there are edges between two states in both directions, these edges are
 * called symmetric edges. To avoid them overlapping each other, the straight
 * lines are replaced by quad curves.
 * <p>
 * The start state and end state of the edge may be the same state. In this
 * case, the edge is called a loop. For a loop, the straight line is replaced by
 * a cubic curve. The cubic curve may go above or below the state.
 *
 * @see GraphicalEdge
 */
public class SmartEdge extends SmartComponent {

    protected final Group container = new Group();
    protected final Shape edgeShape;
    protected final Polygon arrowhead;
    protected final Label label;
    protected final SmartState startState;
    protected final SmartState endState;

    /**
     * Creates an edge between two (not necessarily unique) states.
     *
     * @param edgeShape  the shape of the edge (a line or a curve)
     * @param arrowhead  the arrowhead at the end of the edge
     * @param label      the label on the edge
     * @param startState the start state of the edge
     * @param endState   the end state of the edge
     */
    public SmartEdge(Shape edgeShape, Polygon arrowhead, Label label,
                     SmartState startState, SmartState endState) {
        this.edgeShape = edgeShape;
        this.arrowhead = arrowhead;
        this.label = label;
        this.startState = startState;
        this.endState = endState;
        container.getChildren().addAll(edgeShape, arrowhead, label);
        container.setId("selectable");
    }

    /**
     * Returns the container of this edge. The container contains the edge
     * shape, arrowhead and label.
     *
     * @return the container of this edge
     */
    public Group getContainer() {
        return container;
    }

    /**
     * Set the stroke colour of this edge. The stroke colour is applied to the
     * edge shape and the arrowhead.
     *
     * @param paint the new stroke colour
     */
    public void setStroke(Paint paint) {
        edgeShape.setStroke(paint);
        arrowhead.setStroke(paint);
    }

    /**
     * Sets the text of this edge's label.
     *
     * @param labelText the new text of the label
     */
    public void setLabelText(String labelText) {
        label.setText(labelText);
    }

    /**
     * Returns the text of this edge's label.
     *
     * @return the text of this edge's label.
     */
    public String getLabelText() {
        return label.getText();
    }

    /**
     * Returns the start state of this edge.
     *
     * @return the start state of this edge
     */
    public SmartState getStartState() {
        return startState;
    }

    /**
     * Returns the end state of this edge.
     *
     * @return the end state of this edge
     */
    public SmartState getEndState() {
        return endState;
    }

}
