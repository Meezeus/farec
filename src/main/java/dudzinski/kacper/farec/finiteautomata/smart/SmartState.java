package dudzinski.kacper.farec.finiteautomata.smart;

import dudzinski.kacper.farec.finiteautomata.graphical.GraphicalState;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

/**
 * This class represents a smart state in a finite automaton. A smart state is a
 * state that manages its own size, position, relationship to other components
 * etc. This makes it much more dynamic.
 * <p>
 * A state is a circular, labelled node. It is connected to other states by
 * directed edges (transitions). States keeps track of their incoming and
 * outgoing edges. A state may be marked as initial or final. An initial state
 * has a short, incoming edge that is not connected to any other state. A final
 * state has a second, inner circle.
 *
 * @see GraphicalState
 */
public class SmartState extends SmartComponent {

    private final Group container = new Group();
    private final Circle circle;
    private final Label label;
    private final ArrayList<SmartEdge> incomingEdges = new ArrayList<>();
    private final ArrayList<SmartEdge> outgoingEdges = new ArrayList<>();
    private boolean isInitial = false;
    private boolean isFinal = false;

    /**
     * Creates a smart state. The state is neither initial nor final.
     *
     * @param circle the state circle
     * @param label  the state label
     */
    public SmartState(Circle circle, Label label) {
        this.circle = circle;
        this.label = label;
        container.getChildren().addAll(circle, label);
        container.setId("selectable");
    }

    /**
     * Returns the container of this state. The container contains the state
     * circle and label, as well as any extra marking (if the state is an
     * initial or final state).
     *
     * @return the container of this state
     */
    @Override
    public Group getContainer() {
        return container;
    }

    /**
     * Sets the stroke colour of this state. The stroke colour is applied to the
     * state circle.
     *
     * @param paint the new stroke colour
     */
    @Override
    public void setStroke(Paint paint) {
        circle.setStroke(paint);
    }

    /**
     * Returns the stroke colour of this state.
     *
     * @return the stroke colour
     */
    @Override
    public Paint getStroke() {
        return circle.getStroke();
    }

    /**
     * Sets the text of this state's label.
     *
     * @param labelText the new text of the label
     */
    @Override
    public void setLabelText(String labelText) {
        label.setText(labelText);
    }

    /**
     * Sets the fill colour of this state. The fill colour is applied to the
     * state circle.
     *
     * @param paint the new fill colour
     */
    public void setFill(Paint paint) {
        circle.setFill(paint);
    }

    /**
     * Returns whether this state is marked as an initial state.
     *
     * @return true if this state is marked as an initial state, false otherwise
     */
    public boolean isInitial() {
        return isInitial;
    }

    /**
     * Returns whether this state is marked as a final state.
     *
     * @return true if this state is marked as a final state, false otherwise
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Enables and disables this state as an initial state. If
     * <code>setInitial</code> is set to true, this state will be marked as an
     * initial state (if it is not already). If <code>setInitial</code> is set
     * to false, the initial state marking will be removed from this state (if
     * it is present).
     * <p>
     * The marking for an initial state is a short, incoming edge that is not
     * connected to any other state.
     *
     * @param setInitial whether to mark the state as an initial state or not
     */
    public void setAsInitial(boolean setInitial) {
        if (!isInitial && setInitial) {
            isInitial = true;
            SmartFiniteAutomatonBuilder.setAsInitial(this);
        }
        else if (isInitial && !setInitial) {
            isInitial = false;
            SmartFiniteAutomatonBuilder.setAsNonInitial(this);
        }
    }

    /**
     * Enables and disables the state as a final state. If <code>setFinal</code>
     * is set to true, this state will be marked as a final state (if it is not
     * already). If <code>setFinal</code> is set to false, the final state
     * marking will be removed from this state (if it is present).
     * <p>
     * The marking for a final state is a second, inner circle.
     *
     * @param setFinal whether to mark the state as a final state or not
     */
    public void setAsFinal(boolean setFinal) {
        if (!isFinal && setFinal) {
            isFinal = true;
            SmartFiniteAutomatonBuilder.setAsFinal(this);
        }
        else if (isFinal && !setFinal) {
            isFinal = false;
            SmartFiniteAutomatonBuilder.setAsNonFinal(this);
        }
    }

    /**
     * Adds the given edge to the list of incoming edges.
     *
     * @param edge the incoming edge
     */
    public void addIncomingEdge(SmartEdge edge) {
        incomingEdges.add(edge);
    }

    /**
     * Adds the given edge to the list of outgoing edges.
     *
     * @param edge the outgoing edge
     */
    public void addOutgoingEdge(SmartEdge edge) {
        outgoingEdges.add(edge);
    }

    /**
     * Removes the edge from the list of incoming edges.
     *
     * @param edge the incoming edge
     */
    public void removeIncomingEdge(SmartEdge edge) {
        incomingEdges.remove(edge);
    }

    /**
     * Removes the edge from the list of outgoing edges.
     *
     * @param edge the outgoing edge
     */
    public void removeOutgoingEdge(SmartEdge edge) {
        outgoingEdges.remove(edge);
    }

    /**
     * Returns the list of all edges incoming to this state.
     *
     * @return the list of incoming edges
     */
    public ArrayList<SmartEdge> getIncomingEdges() {
        return incomingEdges;
    }

    /**
     * Returns the list of all outgoing edges from this state.
     *
     * @return the list of outgoing edges
     */
    public ArrayList<SmartEdge> getOutgoingEdges() {
        return outgoingEdges;
    }

}
