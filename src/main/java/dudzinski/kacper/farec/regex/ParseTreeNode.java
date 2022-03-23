package dudzinski.kacper.farec.regex;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * This class represents a node in a regular expression parse tree. A node
 * consists of a container that holds the actual node itself as well as
 * references to the node's children.
 */
public class ParseTreeNode {

    private final StackPane container;
    private ParseTreeNode leftChild = null;
    private ParseTreeNode rightChild = null;

    /**
     * Creates a parse tree node with <code>null</code> children.
     *
     * @param container the container of the parse tree node
     */
    public ParseTreeNode(StackPane container) {
        this.container = container;
    }

    /**
     * Creates a parse tree node.
     *
     * @param container  the container of the parse tree node
     * @param leftChild  the left child of the parse tree node
     * @param rightChild the right child of the parse tree node
     */
    public ParseTreeNode(StackPane container, ParseTreeNode leftChild,
                         ParseTreeNode rightChild) {
        this.container = container;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    /**
     * Returns the container of this parse tree node.
     *
     * @return the container of this parse tree node
     */
    public StackPane getContainer() {
        return container;
    }

    /**
     * Returns the left child of this parse tree node.
     *
     * @return the left child of this parse tree node
     */
    public ParseTreeNode getLeftChild() {
        return leftChild;
    }

    /**
     * Returns the right child of this parse tree node.
     *
     * @return the right child of this parse tree node
     */
    public ParseTreeNode getRightChild() {
        return rightChild;
    }

    /**
     * Sets the left child of this parse tree node.
     *
     * @param leftChild the new left child
     */
    public void setLeftChild(ParseTreeNode leftChild) {
        this.leftChild = leftChild;
    }

    /**
     * Sets the right child of the parse tree node.
     *
     * @param rightChild the new right child
     */
    public void setRightChild(ParseTreeNode rightChild) {
        this.rightChild = rightChild;
    }

    /**
     * Returns the label of this parse tree node.
     *
     * @return the label of this parse tree node
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public String getLabelText() {
        Label label = (Label) container.getChildren().stream().filter(
                child -> child instanceof Label).findFirst().get();
        return label.getText();
    }

    /**
     * Sets the stroke colour of this parse tree node.
     *
     * @param color the new colour of this parse tree node
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void setStroke(Color color) {
        Circle circle = (Circle) container.getChildren().stream().filter(
                child -> child instanceof Circle).findFirst().get();
        circle.setStroke(color);
    }

}
