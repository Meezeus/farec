package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.finiteautomata.graphical.GraphicalFiniteAutomatonBuilder;
import dudzinski.kacper.farec.regex.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static dudzinski.kacper.farec.regex.RegularExpressionSettings.*;

/**
 * This is the controller for the view used to convert regular expressions into
 * finite automata. The view is split into two sections: an area for displaying
 * finite automata on the left, and an area for displaying the parse tree on the
 * right.
 *
 * @see RegularExpressionSettings
 */
public final class ConvertREScreenController implements Initializable {

    @FXML
    private VBox leftVBox;
    @FXML
    private ScrollPane finiteAutomatonScrollPane;
    @FXML
    private StackPane blankPane;
    @FXML
    private Label explanationLabel;

    @FXML
    private VBox rightVBox;
    @FXML
    private ScrollPane parseTreeScrollPane;
    @FXML
    private Label regularExpressionLabel;

    @FXML
    private Label infoLabel;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;

    private int currentPreorderIndex;
    private int maxPreorderIndex;
    private ArrayList<ParseTreeNode> parseTreeNodesPreorder;
    private ArrayList<RegularExpression> regularExpressionsPreorder;

    /**
     * Makes the finite automaton area and the parse tree area grow by equal
     * amounts. Sets the background color of all the components.
     */
    public void initialize(URL location, ResourceBundle resources) {
        // Make the finite automaton area and the parse tree area grow by equal
        // amounts.
        HBox.setHgrow(leftVBox, Priority.ALWAYS);
        HBox.setHgrow(rightVBox, Priority.ALWAYS);

        // Set the background color of the parse tree scroll pane.
        finiteAutomatonScrollPane.setBackground(
                new Background(
                        new BackgroundFill(CONTAINER_COLOR,
                                           CornerRadii.EMPTY,
                                           Insets.EMPTY
                        )));

        // Set the background color of the blank pane.
        blankPane.setBackground(
                new Background(
                        new BackgroundFill(CONTAINER_COLOR,
                                           CornerRadii.EMPTY,
                                           Insets.EMPTY
                        )));

        // Set the background color of the explanation label.
        explanationLabel.setBackground(
                new Background(
                        new BackgroundFill(CONTAINER_COLOR,
                                           CornerRadii.EMPTY,
                                           Insets.EMPTY
                        )));

        // Set the background color of the parse tree scroll pane.
        parseTreeScrollPane.setBackground(
                new Background(
                        new BackgroundFill(CONTAINER_COLOR,
                                           CornerRadii.EMPTY,
                                           Insets.EMPTY
                        )));

        // Set the background color of the regular expression label.
        regularExpressionLabel.setBackground(
                new Background(
                        new BackgroundFill(CONTAINER_COLOR,
                                           CornerRadii.EMPTY,
                                           Insets.EMPTY
                        )));
    }

    /**
     * Starts off the process of converting the regular expression shown in the
     * parse tree into a finite automaton. Displays the parse tree and
     * highlights the first parse tree node in the preorder traversal. Displays
     * the finite automaton for the first regular expression in the preorder
     * traversal. Sets all the labels in the view. Disables the prev button (and
     * possibly the next button).
     *
     * @param parseTree the parse tree of the regular expression being
     *                  converted
     */
    public void setParseTree(ParseTree parseTree) {
        // Get the preorder list of parse tree nodes and the preorder list of
        // regular expressions, and set the indices accordingly.
        parseTreeNodesPreorder = parseTree.preorderTraversal();
        regularExpressionsPreorder = parseTree.getRegularExpression()
                                              .preorderTraversal();
        currentPreorderIndex = 0;
        maxPreorderIndex = parseTreeNodesPreorder.size() - 1;

        // Display the parse tree and update the regular expression label.
        parseTreeScrollPane.setContent(parseTree.getContainer());
        regularExpressionLabel.setText(
                "Regular Expression: "
                        + Parser.simplifyRegexString(
                        parseTree.getRegularExpression().toString()));

        // Disable the prev button (and possibly the next button).
        prevButton.setDisable(true);
        if (currentPreorderIndex == maxPreorderIndex) {
            nextButton.setDisable(true);
        }

        // Highlight the first node in the preorder traversal of the parse tree.
        ParseTreeNode currentParseTreeNode =
                parseTreeNodesPreorder.get(currentPreorderIndex);
        currentParseTreeNode.setStroke(NODE_STROKE_HIGHLIGHT_COLOR);

        // Set the info label with the first regular expression in the preorder
        // traversal.
        RegularExpression currentRegularExpression =
                regularExpressionsPreorder.get(currentPreorderIndex);
        infoLabel.setText(
                "Showing the finite automaton for "
                        + Parser.simplifyRegexString(
                        currentRegularExpression.toString())
                        + ".");

        // Set the explanation label.
        explanationLabel.setText(
                GraphicalFiniteAutomatonBuilder
                        .getExplanationText(currentRegularExpression));

        // Display the first finite automaton.
        blankPane.getChildren().clear();
        blankPane.getChildren().add(
                GraphicalFiniteAutomatonBuilder
                        .buildFiniteAutomaton(currentRegularExpression)
                        .getContainer());
    }

    /**
     * Moves to the previous step of the conversion process. Removes the
     * highlighting from the current node and highlights the previous node,
     * updating the prev and next buttons accordingly. Updates all the labels in
     * the view and displays the previous finite automaton. This method is
     * called when the prev button is pressed.
     */
    public void prev() {
        // Remove highlighting from current node.
        ParseTreeNode currentParseTreeNode =
                parseTreeNodesPreorder.get(currentPreorderIndex);
        currentParseTreeNode.setStroke(NODE_STROKE_COLOR);

        // Decrease the index and enable/disable the buttons accordingly.
        currentPreorderIndex -= 1;
        if (currentPreorderIndex == 0) {
            prevButton.setDisable(true);
        }
        if (currentPreorderIndex < maxPreorderIndex) {
            nextButton.setDisable(false);
        }

        // Highlight the previous node.
        ParseTreeNode previousParseTreeNode =
                parseTreeNodesPreorder.get(currentPreorderIndex);
        previousParseTreeNode.setStroke(NODE_STROKE_HIGHLIGHT_COLOR);

        // Update the info label.
        RegularExpression previousRegularExpression =
                regularExpressionsPreorder.get(currentPreorderIndex);
        infoLabel.setText(
                "Showing the finite automaton for "
                        + Parser.simplifyRegexString(
                        previousRegularExpression.toString())
                        + ".");

        // Update the explanation label.
        explanationLabel.setText(
                GraphicalFiniteAutomatonBuilder
                        .getExplanationText(previousRegularExpression));

        // Display the previous finite automaton.
        blankPane.getChildren().clear();
        blankPane.getChildren().add(
                GraphicalFiniteAutomatonBuilder
                        .buildFiniteAutomaton(previousRegularExpression)
                        .getContainer());
    }

    /**
     * Moves to the next step of the conversion process. Removes the
     * highlighting from the current node and highlights the next node, updating
     * the prev and next buttons accordingly. Updates all the labels in the view
     * and displays the next finite automaton. This method is called when the
     * next button is pressed.
     */
    public void next() {
        // Remove the highlighting from current node.
        ParseTreeNode currentParseTreeNode =
                parseTreeNodesPreorder.get(currentPreorderIndex);
        currentParseTreeNode.setStroke(NODE_STROKE_COLOR);

        // Increase the index and enable/disable the buttons accordingly.
        currentPreorderIndex += 1;
        if (currentPreorderIndex > 0) {
            prevButton.setDisable(false);
        }
        if (currentPreorderIndex == maxPreorderIndex) {
            nextButton.setDisable(true);
        }

        // Highlight the next node.
        ParseTreeNode nextParseTreeNode =
                parseTreeNodesPreorder.get(currentPreorderIndex);
        nextParseTreeNode.setStroke(NODE_STROKE_HIGHLIGHT_COLOR);

        // Update the info label.
        RegularExpression nextRegularExpression =
                regularExpressionsPreorder.get(currentPreorderIndex);
        infoLabel.setText(
                "Showing the finite automaton for "
                        + Parser.simplifyRegexString(
                        nextRegularExpression.toString())
                        + ".");

        // Update the explanation label.
        explanationLabel.setText(
                GraphicalFiniteAutomatonBuilder
                        .getExplanationText(nextRegularExpression));

        // Display the next finite automaton.
        blankPane.getChildren().clear();
        blankPane.getChildren().add(
                GraphicalFiniteAutomatonBuilder
                        .buildFiniteAutomaton(nextRegularExpression)
                        .getContainer());
    }

}
