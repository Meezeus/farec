package dudzinski.kacper.farec;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * This class represents a node in the parse tree. A node consists of a Pane object that holds the actual node itself
 * as well as references to the node's children.
 */
public class ParseTreeNode {

    private StackPane nodePane;
    private ParseTreeNode leftChild;
    private ParseTreeNode rightChild;

    public ParseTreeNode(StackPane nodePane){
        this.nodePane = nodePane;
    }

    public ParseTreeNode(StackPane nodePane, ParseTreeNode leftChild, ParseTreeNode rightChild){
        this.nodePane = nodePane;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public StackPane getNodePane() {
        return nodePane;
    }

    public ParseTreeNode getLeftChild() {
        return leftChild;
    }

    public ParseTreeNode getRightChild() {
        return rightChild;
    }

    public void setLeftChild(ParseTreeNode leftChild){
        this.leftChild = leftChild;
    }

    public void setRightChild(ParseTreeNode rightChild){
        this.rightChild = rightChild;
    }

    @Override
    public String toString(){
        Label label = (Label) nodePane.getChildren().stream().filter(child -> child instanceof Label).findFirst().get();
        return label.getText();
    }

}
