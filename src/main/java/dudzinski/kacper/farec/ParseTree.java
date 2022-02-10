package dudzinski.kacper.farec;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.util.ArrayList;

/**
 * This class represents a parse tree for a regular expression. A parse tree is an unambiguous representation of a
 * regular expression. This class is responsible for building and displaying the parse tree for a given regular expression.
 */
public class ParseTree extends StackPane {

    private ParseTreeNode root;
    private int NODE_RADIUS = 20;   // The radius of a node.
    private int BASE_X_CHANGE = 50; // The minimum horizontal separation between two nodes.
    private int BASE_Y_CHANGE = 80; // The minimum vertical separation between two nodes.
    private double greatestX = 0;   // The greatest horizontal separation between two nodes.
    private double greatestY = 0;   // The greatest vertical separation between two nodes.

    public ParseTree(RegularExpression regularExpression){
        this.setAlignment(Pos.TOP_CENTER);
        root = buildTree(regularExpression, 0, 0);
        connectNodes(root);
        this.setMinSize(2 * greatestX + 4 * NODE_RADIUS, greatestY + 4 * NODE_RADIUS);
    }

    public ParseTreeNode getRoot(){
        return root;
    }

    /**
     * Given a regular expression, builds a parse tree representation of it.
     * @param regularExpression The regular expression for which to build a parse tree.
     * @param currentX The horizontal separation used for the previous depth. Initially 0.
     * @param currentY The vertical separation used for the previous depth. Initially 0.
     * @throws IllegalArgumentException
     */
    private ParseTreeNode buildTree(RegularExpression regularExpression, double currentX, double currentY) throws IllegalArgumentException{
        if (regularExpression instanceof SimpleRegularExpression){
            SimpleRegularExpression regex = (SimpleRegularExpression) regularExpression;
            StackPane leafNodePane = createNode(regex.getSymbol());
            leafNodePane.setTranslateX(currentX);
            leafNodePane.setTranslateY(currentY);
            this.getChildren().add(leafNodePane);
            ParseTreeNode leafNode = new ParseTreeNode(leafNodePane, null, null);
            return leafNode;
        }
        else if (regularExpression instanceof ComplexRegularExpression){
            ComplexRegularExpression regex = (ComplexRegularExpression) regularExpression;
            StackPane operatorNodePane = createNode(Parser.getOperatorChar(regex.getOperator()));
            operatorNodePane.setTranslateX(currentX);
            operatorNodePane.setTranslateY(currentY);
            this.getChildren().add(operatorNodePane);
            ParseTreeNode operatorNode = new ParseTreeNode(operatorNodePane);

            double maxDepth = regex.getDepth();
            double xChange = Math.pow(2, maxDepth - 1) * BASE_X_CHANGE;
            double yChange = BASE_Y_CHANGE;

            currentY += yChange;
            if (currentY > greatestY){
                greatestY = currentY;
            }

            if (regex.getOperator() == Parser.REOperators.STAR){
                operatorNode.setLeftChild(buildTree(regex.getLeftOperand(), currentX, currentY));
            }
            else {
                currentX -= xChange;
                operatorNode.setLeftChild(buildTree(regex.getLeftOperand(), currentX, currentY));

                currentX += xChange * 2;
                if (currentX > greatestX){
                    greatestX = currentX;
                }
                operatorNode.setRightChild(buildTree(regex.getRightOperand(), currentX, currentY));
            }
            return operatorNode;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Creates a labelled node.
     * @param title The node label.
     * @return Labelled node.
     */
    private StackPane createNode(char title){
        Circle node = new Circle();
        node.setRadius(NODE_RADIUS);
        node.setStyle("-fx-fill: white;-fx-stroke: black;-fx-stroke-width:2px");
        Label nodeTitle = new Label("" + title);
        StackPane nodePane = new StackPane();
        nodePane.setMaxSize(NODE_RADIUS * 2, NODE_RADIUS * 2);
        nodePane.getChildren().addAll(node, nodeTitle);
        return nodePane;
    }

    /**
     * Recursively connects nodes in a tree to their children.
     * @param parentNode The root node of the tree to be connected.
     */
    private void connectNodes(ParseTreeNode parentNode){
        ParseTreeNode leftChild = parentNode.getLeftChild();
        ParseTreeNode rightChild = parentNode.getRightChild();
        if (leftChild != null){
            createEdge(parentNode.getNodePane(), leftChild.getNodePane());
            connectNodes(leftChild);
        }
        if (rightChild != null){
            createEdge(parentNode.getNodePane(), rightChild.getNodePane());
            connectNodes(rightChild);
        }
    }

    /**
     * Connects a parent node to its child.
     * @param parent The parent node.
     * @param child The child node.
     */
    private void createEdge(StackPane parent, StackPane child){
        Line edge = new Line();
        edge.setStyle("-fx-stroke: black;-fx-stroke-width:2px");
        edge.setStartX(parent.getTranslateX());
        edge.setStartY(parent.getTranslateY());
        edge.setEndX(child.getTranslateX());
        edge.setEndY(child.getTranslateY());

        StackPane edgePane = new StackPane();
        edgePane.getChildren().add(edge);
        edgePane.setMaxWidth(edge.getEndX() - edge.getStartX());
        edgePane.setMaxHeight(edge.getEndY() - edge.getStartY());
        double xTranslate = parent.getTranslateX();
        double yTranslate = parent.getTranslateY();

        yTranslate += NODE_RADIUS;
        double xSeparation = Math.abs(parent.getTranslateX() - child.getTranslateX());
        // If the child is a left child, we need to move the edge to the left.
        if (child.getTranslateX() < parent.getTranslateX()){
            xTranslate += -1 * (xSeparation / 2);
        }
        // If the child is a right child, we need to move the edge to the right.
        else if (child.getTranslateX() > parent.getTranslateX()) {
            xTranslate += 1 * (xSeparation / 2);
        }
        edgePane.setTranslateX(xTranslate);
        edge.setTranslateY(yTranslate);

        this.getChildren().add(edgePane);
        edgePane.toBack();
    }

    /**
     * Performs a preorder traversal of the tree rooted at root.
     * @param root The root of the tree
     * @return A list of nodes in the tree, in preorder.
     */
    public ArrayList<ParseTreeNode> preorderTraversal(ParseTreeNode root){
        ArrayList<ParseTreeNode> currentList = new ArrayList<>();
        ParseTreeNode leftChild = root.getLeftChild();
        ParseTreeNode rightChild = root.getRightChild();
        if (leftChild != null){
            currentList.addAll(preorderTraversal(leftChild));
        }
        if (rightChild != null){
            currentList.addAll(preorderTraversal(rightChild));
        }
        currentList.add(root);
        return currentList;
    }
}

