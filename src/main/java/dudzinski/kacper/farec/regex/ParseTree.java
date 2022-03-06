package dudzinski.kacper.farec.regex;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;

/**
 * This class represents a parse tree for a regular expression. A parse tree is an unambiguous representation of a
 * regular expression. This class is responsible for building and displaying the parse tree for a given regular expression.
 */
public class ParseTree extends StackPane {

    private final RegularExpression regularExpression;
    private final ParseTreeNode root;
    private final int NODE_RADIUS = 20;   // The radius of a node.
    private final int BASE_X_CHANGE = 50; // The minimum horizontal separation between two nodes.
    private final int BASE_Y_CHANGE = 80; // The minimum vertical separation between two nodes.
    private double greatestX = 0;   // The greatest horizontal separation between two nodes.
    private double greatestY = 0;   // The greatest vertical separation between two nodes.

    /**
     * Creates and initialises the parse tree for the given regular expression.
     *
     * @param regularExpression The regular expression for the parse tree.
     */
    public ParseTree(RegularExpression regularExpression) {
        // Set regular expression
        this.regularExpression = regularExpression;
        // Create the parse tree and connect the nodes.
        root = createParseTree(regularExpression, 0, 0);
        connectNodes(root);
        // Initialise the container.
        this.setAlignment(Pos.TOP_CENTER);
        this.setMinSize((2 * greatestX) + (4 * NODE_RADIUS), greatestY + NODE_RADIUS);
    }

    /**
     * Given a regular expression, builds a parse tree representation of it.
     *
     * @param regularExpression The regular expression for which to build a parse tree.
     * @param currentX          The horizontal separation used for the previous depth. Initially 0.
     * @param currentY          The vertical separation used for the previous depth. Initially 0.
     */
    private ParseTreeNode createParseTree(RegularExpression regularExpression, double currentX, double currentY) throws IllegalArgumentException {
        // If the regular expression is simple, its parse tree is just a single node.
        if (regularExpression instanceof SimpleRegularExpression simpleRegex) {
            // Create the node.
            StackPane leafNodePane = createNode(simpleRegex.getSymbol());
            // Move the node into position and add it to the parse tree.
            leafNodePane.setTranslateX(currentX);
            leafNodePane.setTranslateY(currentY);
            this.getChildren().add(leafNodePane);
            return new ParseTreeNode(leafNodePane, null, null);
        }
        // If the regular is complex, its parse tree will include an operator node and one or two children.
        else if (regularExpression instanceof ComplexRegularExpression complexRegex) {
            // Create the operator node.
            StackPane operatorNodePane = createNode(RegexOperatorChars.getCharFromOperator(complexRegex.getOperator()));
            // Move the operator node into position and add it to the parse tree.
            operatorNodePane.setTranslateX(currentX);
            operatorNodePane.setTranslateY(currentY);
            this.getChildren().add(operatorNodePane);
            ParseTreeNode operatorNode = new ParseTreeNode(operatorNodePane);

            // Calculate the horizontal and vertical distance between the operator node and its children.
            double maxDepth = complexRegex.getDepth();
            double xChange = Math.pow(2, maxDepth - 1) * BASE_X_CHANGE;

            // Move downwards.
            currentY += BASE_Y_CHANGE;
            if (currentY > greatestY) {
                greatestY = currentY;
            }

            // If the operator is STAR, there is only one child, and we do not need to move left/right.
            if (complexRegex.getOperator() == RegexOperator.STAR) {
                operatorNode.setLeftChild(createParseTree(complexRegex.getLeftOperand(), currentX, currentY));
            }
            // If the operator is CONCATENATION or UNION, there are two children, and we will need to move left/right.
            else if (complexRegex.getOperator() == RegexOperator.CONCATENATION || complexRegex.getOperator() == RegexOperator.UNION) {
                // Move to the left.
                currentX -= xChange;
                if (Math.abs(currentX) > greatestX) {
                    greatestX = Math.abs(currentX);
                }

                // Build the subtree rooted at the left child and add it to the parse tree.
                operatorNode.setLeftChild(createParseTree(complexRegex.getLeftOperand(), currentX, currentY));

                // Move to the right.
                currentX += xChange * 2;
                if (currentX > greatestX) {
                    greatestX = currentX;
                }

                // Build the subtree rooted at the right child and add it to the parse tree.
                operatorNode.setRightChild(createParseTree(complexRegex.getRightOperand(), currentX, currentY));
            }
            return operatorNode;
        }
        else {
            throw new IllegalArgumentException("Regular Expression is neither Simple nor Complex!");
        }
    }

    /**
     * Creates a labelled node.
     *
     * @param title The node label.
     * @return Labelled node.
     */
    private StackPane createNode(char title) {
        // Create the node
        Circle node = new Circle();
        node.setRadius(NODE_RADIUS);
        node.setFill(Color.WHITE);
        node.setStrokeWidth(2.0);
        node.setStroke(Color.BLACK);

        // Create the node label.
        Label nodeTitle = new Label("" + title);

        // Create the node container and add the node and the label.
        StackPane nodePane = new StackPane();
        nodePane.setMaxSize(2 * NODE_RADIUS, 2 * NODE_RADIUS);
        nodePane.getChildren().addAll(node, nodeTitle);
        return nodePane;
    }

    /**
     * Recursively connects nodes in a tree to their children.
     *
     * @param parentNode The root node of the tree to be connected.
     */
    private void connectNodes(ParseTreeNode parentNode) {
        // Get the children of the parent node.
        ParseTreeNode leftChild = parentNode.getLeftChild();
        ParseTreeNode rightChild = parentNode.getRightChild();

        // Connect the parent node to its children. Connect the nodes of the subtrees rooted at the children.
        if (leftChild != null) {
            connectParentToChild(parentNode.getNodePane(), leftChild.getNodePane());
            connectNodes(leftChild);
        }
        if (rightChild != null) {
            connectParentToChild(parentNode.getNodePane(), rightChild.getNodePane());
            connectNodes(rightChild);
        }
    }

    /**
     * Connects a parent node to its child.
     *
     * @param parent The parent node.
     * @param child  The child node.
     */
    private void connectParentToChild(StackPane parent, StackPane child) {
        // Create the line.
        Line line = new Line();
        line.setStrokeWidth(2.0);
        line.setStroke(Color.BLACK);
        line.setStartX(parent.getTranslateX());
        line.setStartY(parent.getTranslateY());
        line.setEndX(child.getTranslateX());
        line.setEndY(child.getTranslateY());

        // Create the line container and add the line.
        StackPane linePane = new StackPane();
        linePane.setMaxWidth(line.getEndX() - line.getStartX());
        linePane.setMaxHeight(line.getEndY() - line.getStartY());
        linePane.getChildren().add(line);

        // Move the line to the parent node.
        double xTranslate = parent.getTranslateX();
        double yTranslate = parent.getTranslateY();

        // Move the line down to the centre of the parent node.
        yTranslate += NODE_RADIUS;

        // Move the line to the left/right so that it connects the parent and child nodes.
        double xSeparation = Math.abs(parent.getTranslateX() - child.getTranslateX());
        // If the child is a left child, we need to move the edge to the left.
        if (child.getTranslateX() < parent.getTranslateX()) {
            xTranslate += -1 * (xSeparation / 2);
        }
        // If the child is a right child, we need to move the edge to the right.
        else if (child.getTranslateX() > parent.getTranslateX()) {
            xTranslate += 1 * (xSeparation / 2);
        }

        // Commit the move.
        linePane.setTranslateX(xTranslate);
        line.setTranslateY(yTranslate);

        // Add the line to the parse tree and send it to the back.
        this.getChildren().add(linePane);
        linePane.toBack();
    }

    /**
     * Performs a preorder traversal of the tree rooted at root.
     *
     * @param root The root of the tree
     * @return A list of nodes in the tree, in preorder.
     */
    public static ArrayList<ParseTreeNode> preorderTraversal(ParseTreeNode root) {
        ArrayList<ParseTreeNode> currentList = new ArrayList<>();
        ParseTreeNode leftChild = root.getLeftChild();
        ParseTreeNode rightChild = root.getRightChild();
        if (leftChild != null) {
            currentList.addAll(preorderTraversal(leftChild));
        }
        if (rightChild != null) {
            currentList.addAll(preorderTraversal(rightChild));
        }
        currentList.add(root);
        return currentList;
    }

    /**
     * @return The root of the parse tree.
     */
    public ParseTreeNode getRoot() {
        return root;
    }

    /**
     * @return The regular expression of the parse tree.
     */
    public RegularExpression getRegularExpression() {
        return regularExpression;
    }
}
