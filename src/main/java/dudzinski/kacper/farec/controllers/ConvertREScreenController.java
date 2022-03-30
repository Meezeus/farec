package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.finiteautomata.graphical.GraphicalFiniteAutomatonBuilder;
import dudzinski.kacper.farec.regex.*;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

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

    public HBox centralContainer;

    public VBox leftVBox;
    public ScrollPane finiteAutomataContainer;
    public Label explanationLabel;

    public VBox rightVBox;
    public ScrollPane parseTreeContainer;
    public Label regularExpressionLabel;

    public Label infoLabel;
    public Button prevButton;
    public Button nextButton;

    private int currentPreorderIndex;
    private int maxPreorderIndex;
    private ArrayList<ParseTreeNode> parseTreeNodesPreorder;
    private ArrayList<RegularExpression> regularExpressionsPreorder;

    /**
     * Makes the finite automaton area and the parse tree area grow by equal
     * amounts.
     */
    public void initialize(URL location, ResourceBundle resources) {
        HBox.setHgrow(leftVBox, Priority.ALWAYS);
        HBox.setHgrow(rightVBox, Priority.ALWAYS);
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
        parseTreeContainer.setContent(parseTree.getContainer());
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
        finiteAutomataContainer.setContent(
                GraphicalFiniteAutomatonBuilder
                        .buildFiniteAutomaton(currentRegularExpression)
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
        finiteAutomataContainer.setContent(
                GraphicalFiniteAutomatonBuilder
                        .buildFiniteAutomaton(nextRegularExpression)
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
        finiteAutomataContainer.setContent(
                GraphicalFiniteAutomatonBuilder
                        .buildFiniteAutomaton(previousRegularExpression)
                        .getContainer());
    }

}
