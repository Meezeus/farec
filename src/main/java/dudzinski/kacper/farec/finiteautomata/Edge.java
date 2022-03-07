package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

/**
 * This class represents an edge in a finite automaton.
 */
public class Edge extends Control implements FiniteAutomatonComponent {

    private StackPane edgePane = new StackPane();
    private Group edgeGroup;
    private Label label;

    /**
     * Creates an edge using the given components.
     *
     * @param edgeGroup A group containing the edge line and arrowhead.
     * @param label     The label of the state.
     */
    public Edge(Group edgeGroup, Label label) {
        this.edgeGroup = edgeGroup;
        this.label = label;
        edgePane.getChildren().addAll(edgeGroup, label);
    }

    /**
     * @return The StackPane holding the edge.
     */
    public StackPane getPane() {
        return edgePane;
    }

    /**
     * Set the stroke colour of the edge.
     *
     * @param paint The new stroke colour.
     */
    public void setStroke(Paint paint) {
        for (Node node : edgeGroup.getChildren()) {
            Shape shape = (Shape) node;
            shape.setStroke(paint);
        }
    }

    /**
     * Sets the text of the state label.
     *
     * @param labelText The new text of the label.
     */
    public void setLabel(String labelText) {
        label.setText(labelText);
    }

}
