package dudzinski.kacper.farec;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * This class represents a parse tree for a regular expression. A parse tree is an unambiguous representation of a
 * regular expression. This class is responsible for building and displaying the parse tree for a given regular expression.
 */
public class ParseTree extends StackPane {

    private ParseTreeNode root;
    private int BASE_X_CHANGE = 50;
    private int BASE_Y_CHANGE = 80;
    private int NODE_RADIUS = 20;
    private double currentY = 0;
    private double currentX = 0;

    public ParseTree(RegularExpression regularExpression){
        this.setAlignment(Pos.TOP_CENTER);
        root = buildTree(regularExpression);
        connectNodes(root);
    }

    /**
     * Given a regular expression, builds a parse tree representation of it.
     * @param regularExpression The regular expression for which to build a parse tree.
     * @throws IllegalArgumentException
     */
    private ParseTreeNode buildTree(RegularExpression regularExpression) throws IllegalArgumentException{
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

            double currentDepth = (double) regex.getDepth();
            double xChange = Math.pow(2, currentDepth - 1) * BASE_X_CHANGE;
            double yChange = BASE_Y_CHANGE;

            currentY += yChange;
            if (regex.getOperator() == Parser.REOperators.STAR){
                operatorNode.setLeftChild(buildTree(regex.getLeftOperand()));
            }
            else {
                currentX -= xChange;
                operatorNode.setLeftChild(buildTree(regex.getLeftOperand()));
                currentX += xChange * 2;
                operatorNode.setRightChild(buildTree(regex.getRightOperand()));
                currentX -= xChange;
            }
            currentY -= yChange;
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

}

