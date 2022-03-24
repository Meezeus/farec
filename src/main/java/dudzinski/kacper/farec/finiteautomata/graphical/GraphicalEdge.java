package dudzinski.kacper.farec.finiteautomata.graphical;

import dudzinski.kacper.farec.finiteautomata.smart.SmartEdgeComponent;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * This class represents a graphical representation of a finite automaton edge.
 * This class concerns itself with just the graphical aspect of an edge. As
 * such, the edge does not keep track of what states it is associated with. The
 * edge should be treated as an isolated graphical component.
 * <p>
 * An edge is a (directed), labelled line between two states. It represents a
 * transitions between the two states.
 * <p>
 * The transition between two states may be represented by several edges
 * assembled together (for example in a U shape). In such cases, only the final
 * edge is directed. The other edges are assumed to be part of the final edge.
 *
 * @see SmartEdgeComponent
 */
public class GraphicalEdge {

    private final StackPane container = new StackPane();

    /**
     * Creates a graphical edge.
     *
     * @param edgeGroup the group containing the line (and arrowhead)
     * @param label     the label of the edge
     */
    public GraphicalEdge(Group edgeGroup, Label label) {
        container.getChildren().addAll(edgeGroup, label);
    }

    /**
     * Returns the container of this edge. The container contains the group
     * holding the line (and arrowhead), as well as the label.
     *
     * @return the container of this edge
     */
    public StackPane getContainer() {
        return container;
    }

}
