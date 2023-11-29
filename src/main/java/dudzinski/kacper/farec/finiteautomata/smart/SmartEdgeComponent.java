package dudzinski.kacper.farec.finiteautomata.smart;

import dudzinski.kacper.farec.finiteautomata.graphical.GraphicalEdge;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 * This class represents a smart edge component in a finite automaton. A smart
 * edge component is an edge that manages its own size, position, relationship
 * to other components etc. This makes it much more dynamic.
 * <p>
 * An edge is a directed, labelled line between two (not necessarily different)
 * states. It represents a transition between the two states. An edge between
 * two states in a particular direction is unique: there cannot be any other
 * edges between those two states in the same direction.
 * <p>
 * If the two states connected by an edge are different states, the edge is a
 * {@link SmartEdge}. If the two states connected by an edge are the same state,
 * the edge is a {@link SmartLoopEdge}.
 *
 * @see SmartEdge
 * @see SmartLoopEdge
 * @see GraphicalEdge
 */
public abstract class SmartEdgeComponent extends SmartComponent {

    protected final Group container = new Group();
    protected final SmartState startState;
    protected final SmartState endState;
    protected Shape edgeShape;
    protected Polygon arrowhead;
    protected Label label;

    /**
     * Creates an edge between two (not necessarily unique) states.
     *
     * @param edgeShape  the shape of the edge (a line or a curve)
     * @param arrowhead  the arrowhead at the end of the edge
     * @param label      the label on the edge
     * @param startState the start state of the edge
     * @param endState   the end state of the edge
     */
    public SmartEdgeComponent(Shape edgeShape, Polygon arrowhead, Label label,
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
    @Override
    public Group getContainer() {
        return container;
    }

    /**
     * Returns the stroke colour of this edge.
     *
     * @return the stroke colour
     */
    @Override
    public Paint getStroke() {
        return edgeShape.getStroke();
    }

    /**
     * Set the stroke colour of this edge. The stroke colour is applied to the
     * edge shape and the arrowhead.
     *
     * @param paint the new stroke colour
     */
    @Override
    public void setStroke(Paint paint) {
        edgeShape.setStroke(paint);
        arrowhead.setStroke(paint);
    }

    /**
     * Returns the text of this edge's label.
     *
     * @return the text of this edge's label
     */
    @Override
    public String getLabelText() {
        return label.getText();
    }

    /**
     * Sets the text of this edge's label.
     *
     * @param labelText the new text of the label
     */
    @Override
    public void setLabelText(String labelText) {
        label.setText(labelText);
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
