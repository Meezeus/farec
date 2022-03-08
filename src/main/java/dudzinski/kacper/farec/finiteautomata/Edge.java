package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * This class represents an edge in a finite automaton.
 */
public class Edge {

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
     * Sets the text of the state label.
     *
     * @param labelText The new text of the label.
     */
    public void setLabel(String labelText) {
        label.setText(labelText);
    }

}
