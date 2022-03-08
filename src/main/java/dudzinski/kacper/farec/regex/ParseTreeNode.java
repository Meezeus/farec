package dudzinski.kacper.farec.regex;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * This class represents a node in the parse tree. A node consists of a Pane object that holds the actual node itself
 * as well as references to the node's children.
 */
public class ParseTreeNode {

    private StackPane nodePane;
    private ParseTreeNode leftChild;
    private ParseTreeNode rightChild;

    /**
     * Create the parse tree node for a leaf node.
     *
     * @param nodePane The node pane containing the parse tree node.
     */
    public ParseTreeNode(StackPane nodePane) {
        this.nodePane = nodePane;
    }

    /**
     * Create the parse tree node for an internal node.
     *
     * @param nodePane   The node pane containing the parse tree node.
     * @param leftChild  The left child of the parse tree node.
     * @param rightChild The right child of the parse tree node.
     */
    public ParseTreeNode(StackPane nodePane, ParseTreeNode leftChild, ParseTreeNode rightChild) {
        this.nodePane = nodePane;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    /**
     * @return The node pane containing the parse tree node.
     */
    public StackPane getNodePane() {
        return nodePane;
    }

    /**
     * @return The left child of the parse tree node.
     */
    public ParseTreeNode getLeftChild() {
        return leftChild;
    }

    /**
     * @return The right child of the parse tree node.
     */
    public ParseTreeNode getRightChild() {
        return rightChild;
    }

    /**
     * Set the left child of the parse tree node.
     *
     * @param leftChild The new left child.
     */
    public void setLeftChild(ParseTreeNode leftChild) {
        this.leftChild = leftChild;
    }

    /**
     * Set the right child of the parse tree node.
     *
     * @param rightChild The new left child.
     */
    public void setRightChild(ParseTreeNode rightChild) {
        this.rightChild = rightChild;
    }

    /**
     * @return The node label.
     */
    public String getLabelText() {
        Label label = (Label) nodePane.getChildren().stream().filter(child -> child instanceof Label).findFirst().get();
        return label.getText();
    }

    /**
     * Sets the stroke colour of the node.
     *
     * @param colour The new colour.
     */
    public void setCircleStrokeColour(String colour) {
        Circle circle = (Circle) nodePane.getChildren().stream().filter(child -> child instanceof Circle).findFirst().get();
        circle.setStroke(Paint.valueOf(colour));
    }

}
