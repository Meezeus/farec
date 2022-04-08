package dudzinski.kacper.farec.regex;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;

import static dudzinski.kacper.farec.regex.RegularExpressionSettings.*;

/**
 * This class represents a parse tree for a regular expression. A parse tree is
 * an unambiguous representation of a regular expression. This class is
 * responsible for building the parse tree for a given regular expression.
 *
 * @see RegularExpressionSettings
 * @see ParseTreeNode
 */
public final class ParseTree {

    private static final double TOP_PADDING = 20;
    private final StackPane container = new StackPane();
    private final RegularExpression regularExpression;
    private final ParseTreeNode root;

    // The greatest horizontal separation between two nodes.
    private double greatestX = 0;
    // The greatest vertical separation between two nodes.
    private double greatestY = 0;

    /**
     * Creates a parse tree for the given regular expression.
     *
     * @param regularExpression the regular expression for which to create a
     *                          parse tree
     */
    public ParseTree(RegularExpression regularExpression) {
        // Set the regular expression
        this.regularExpression = regularExpression;

        // Create the parse tree and connect the nodes.
        root = buildParseTree(regularExpression, 0, 0);
        connectNodes(root);

        // Initialise the container.
        container.setAlignment(Pos.TOP_CENTER);
        container.setMinWidth((2 * greatestX) + (4 * NODE_RADIUS));
        container.setMinHeight(greatestY + (4 * NODE_RADIUS) + TOP_PADDING);
        container.setPadding(new Insets(TOP_PADDING, 0, 0, 0));
        container.setBackground(
                new Background(
                        new BackgroundFill(CONTAINER_COLOR,
                                           CornerRadii.EMPTY,
                                           Insets.EMPTY
                        )));
    }

    /**
     * Returns the container of this parse tree.
     *
     * @return the container of this parse tree
     */
    public StackPane getContainer() {
        return container;
    }

    /**
     * Returns the regular expression of this parse tree.
     *
     * @return the regular expression of this parse tree
     */
    public RegularExpression getRegularExpression() {
        return regularExpression;
    }

    /**
     * Returns the postorder traversal of the nodes in this parse tree.
     *
     * @return the postorder traversal of the nodes in this parse tree
     */
    public ArrayList<ParseTreeNode> postorderTraversal() {
        return postorderTraversal(root);
    }

    /**
     * Builds a parse tree for the given regular expression.
     *
     * @param regularExpression the regular expression for which to build a
     *                          parse tree
     * @param currentX          the horizontal separation used in the
     *                          construction of the previous depth (initially
     *                          0)
     * @param currentY          the vertical separation used in the construction
     *                          of the previous depth (initially 0)
     * @throws IllegalArgumentException if the regular expression is neither
     *                                  simple nor complex
     */
    private ParseTreeNode buildParseTree(RegularExpression regularExpression,
                                         double currentX, double currentY)
            throws IllegalArgumentException {
        // If the regular expression is simple, it's parse tree is just a single
        // node.
        if (regularExpression instanceof SimpleRegularExpression simpleRegex) {
            // Create the leaf node.
            StackPane leafNodePane = createNode(simpleRegex.getSymbol());

            // Move the leaf node into position.
            leafNodePane.setTranslateX(currentX);
            leafNodePane.setTranslateY(currentY);

            // Add the leaf node to the parse tree.
            container.getChildren().add(leafNodePane);
            return new ParseTreeNode(leafNodePane);
        }

        // If the regular expression is complex, it's parse tree will include an
        // operator node and one or two children.
        else if (regularExpression instanceof
                ComplexRegularExpression complexRegex) {
            // Create the operator node.
            char operatorChar =
                    RegularExpressionSettings.getCharFromOperator(
                            complexRegex.getOperator());
            StackPane operatorNodePane = createNode(operatorChar);

            // Move the operator node into position.
            operatorNodePane.setTranslateX(currentX);
            operatorNodePane.setTranslateY(currentY);

            // Add the operator node to the parse tree.
            container.getChildren().add(operatorNodePane);
            ParseTreeNode operatorNode = new ParseTreeNode(operatorNodePane);

            // Move downwards.
            currentY += MIN_Y_CHANGE;
            if (currentY > greatestY) {
                greatestY = currentY;
            }

            // If the operator is STAR then there is only one child, and we do
            // not need to move left/right.
            if (complexRegex.getOperator() == RegexOperator.STAR) {
                // Build the subtree rooted at the left child and add it to the
                // parse tree.
                operatorNode.setLeftChild(buildParseTree(
                        complexRegex.getLeftOperand(), currentX, currentY));
            }

            // If the operator is CONCATENATION or UNION, there are two
            // children, and we will need to move left/right.
            else if ((complexRegex.getOperator() == RegexOperator.CONCATENATION)
                    || (complexRegex.getOperator() == RegexOperator.UNION)) {
                // Calculate the horizontal distance between the operator node
                // and its children.
                double maxDepth = complexRegex.getDepth();
                double xChange = Math.pow(2, maxDepth - 1) * MIN_X_CHANGE;

                // Move to the left.
                currentX -= xChange;
                if (Math.abs(currentX) > greatestX) {
                    greatestX = Math.abs(currentX);
                }

                // Build the subtree rooted at the left child and add it to the
                // parse tree.
                operatorNode.setLeftChild(buildParseTree(
                        complexRegex.getLeftOperand(), currentX, currentY));

                // Move to the right.
                currentX += xChange * 2;
                if (currentX > greatestX) {
                    greatestX = currentX;
                }

                // Build the subtree rooted at the right child and add it to the
                // parse tree.
                operatorNode.setRightChild(buildParseTree(
                        complexRegex.getRightOperand(), currentX, currentY));
            }

            // Return the operator node.
            return operatorNode;
        }
        else {
            throw new IllegalArgumentException(
                    "Regular Expression is neither simple nor complex!");
        }
    }

    /**
     * Creates a labelled node. The label on the node is a symbol: either a
     * regex operator or a regex operand.
     *
     * @param symbol the node symbol
     * @return a labelled node
     */
    private StackPane createNode(char symbol) {
        // Create the circle.
        Circle circle = new Circle();
        circle.setRadius(NODE_RADIUS);
        circle.setFill(NODE_FILL);
        circle.setStrokeWidth(2 * NODE_STROKE_RADIUS);
        circle.setStroke(NODE_STROKE_COLOR);

        // Create the circle label.
        Label label = new Label(String.valueOf(symbol));

        // Create the circle container and add the circle and the label.
        StackPane nodePane = new StackPane();
        nodePane.setMaxSize(2 * NODE_RADIUS, 2 * NODE_RADIUS);
        nodePane.getChildren().addAll(circle, label);
        return nodePane;
    }

    /**
     * Recursively connects nodes in a parse tree to their children.
     *
     * @param parentNode the root node of the parse tree to be connected
     */
    private void connectNodes(ParseTreeNode parentNode) {
        // Get the children of the parent node.
        ParseTreeNode leftChild = parentNode.getLeftChild();
        ParseTreeNode rightChild = parentNode.getRightChild();

        // Connect the parent node to its children. Then connect the nodes of
        // the subtrees rooted at the children.
        if (leftChild != null) {
            connectParentToChild(parentNode.getContainer(),
                                 leftChild.getContainer());
            connectNodes(leftChild);
        }
        if (rightChild != null) {
            connectParentToChild(parentNode.getContainer(),
                                 rightChild.getContainer());
            connectNodes(rightChild);
        }
    }

    /**
     * Connects a parent node to its child.
     *
     * @param parent the parent node
     * @param child  the child node
     */
    @SuppressWarnings("GrazieInspection")
    private void connectParentToChild(StackPane parent, StackPane child) {
        // Create the line.
        Line line = new Line();
        line.setStrokeWidth(2 * EDGE_STROKE_RADIUS);
        line.setStroke(EDGE_STROKE_COLOR);
        line.setStartX(parent.getTranslateX());
        line.setStartY(parent.getTranslateY());
        line.setEndX(child.getTranslateX());
        line.setEndY(child.getTranslateY());

        // Create the line container and add the line.
        StackPane linePane = new StackPane();
        linePane.getChildren().add(line);
        linePane.setMaxWidth(line.getEndX() - line.getStartX());
        linePane.setMaxHeight(line.getEndY() - line.getStartY());

        // Get the translate of the parent node.
        double xTranslate = parent.getTranslateX();
        double yTranslate = parent.getTranslateY();

        // Add the offset to move the line down to the centre of the parent
        // node.
        yTranslate += NODE_RADIUS;

        // Add the offset to move the line to the left/right so that it connects
        // the parent and child nodes.
        double xSeparation = Math.abs(parent.getTranslateX()
                                              - child.getTranslateX());
        // If the child is a left child, we need to move the edge to the left.
        if (child.getTranslateX() < parent.getTranslateX()) {
            xTranslate -= 0.5 * xSeparation;
        }
        // If the child is a right child, we need to move the edge to the right.
        else if (child.getTranslateX() > parent.getTranslateX()) {
            xTranslate += 0.5 * xSeparation;
        }

        // Move the edge.
        linePane.setTranslateX(xTranslate);
        line.setTranslateY(yTranslate);

        // Add the edge to the back of the parse tree.
        container.getChildren().add(0, linePane);
    }

    /**
     * Returns the postorder traversal of the parse tree rooted at the given
     * parse tree node.
     *
     * @param rootNode the root parse tree node of the parse tree
     * @return the postorder traversal of the nodes in the parse tree
     */
    private ArrayList<ParseTreeNode> postorderTraversal(ParseTreeNode rootNode) {
        // Create the list.
        ArrayList<ParseTreeNode> currentList = new ArrayList<>();

        // Get the children.
        ParseTreeNode leftChild = rootNode.getLeftChild();
        ParseTreeNode rightChild = rootNode.getRightChild();

        // Add the postorder traversal of the left child, then the right child.
        if (leftChild != null) {
            currentList.addAll(postorderTraversal(leftChild));
        }
        if (rightChild != null) {
            currentList.addAll(postorderTraversal(rightChild));
        }

        // Add the root and return the list.
        currentList.add(rootNode);
        return currentList;
    }
}

