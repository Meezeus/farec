package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.Group;
import javafx.scene.paint.Paint;

/**
 * This interface represents a finite automaton component, i.e. a state or an edge.
 */
public interface FiniteAutomatonComponent {

    /**
     * Set the stroke colour of the finite automaton component.
     *
     * @param paint The new stroke colour.
     */
    void setStroke(Paint paint);

    /**
     * Sets the text of the finite automaton component label.
     *
     * @param labelText The new text of the label.
     */
    void setLabel(String labelText);

    /**
     * @return The Group holding the finite automaton component.
     */
    Group getContainer();

}
