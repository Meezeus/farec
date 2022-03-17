package dudzinski.kacper.farec.finiteautomata;

import javafx.scene.Group;
import javafx.scene.paint.Paint;

/**
 * This abstract class represents a smart finite automaton component, i.e. a
 * smart state or a smart edge. Smart components manage their own state size,
 * position, relationship to other components etc. This makes them much more
 * dynamic.
 */

public abstract class SmartComponent {

    /**
     * Returns the container of this smart component. The container holds all
     * the subcomponents that make up the smart component.
     *
     * @return the container of this smart component
     */
    public abstract Group getContainer();

    /**
     * Sets the stroke colour of this smart component.
     *
     * @param paint the new stroke colour
     */
    public abstract void setStroke(Paint paint);

    /**
     * Sets the text of this smart component's label.
     *
     * @param labelText the new text of the label
     */
    public abstract void setLabelText(String labelText);

}
