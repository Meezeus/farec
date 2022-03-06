package dudzinski.kacper.farec.controllers;

import dudzinski.kacper.farec.finiteautomata.FiniteAutomatonBuilder;
import dudzinski.kacper.farec.regex.ParseTree;
import dudzinski.kacper.farec.regex.ParseTreeNode;
import dudzinski.kacper.farec.regex.RegularExpression;
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

public class ConvertREScreenController implements Initializable {

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

    private int currentPreorderIndex;   // The index of the current preorder element.
    private int maxPreorderIndex;   // The max value of the current preorder index.
    private ArrayList<ParseTreeNode> parseTreeNodesPreorder;    // The parse tree nodes, in preorder.
    private ArrayList<RegularExpression> regularExpressionsPreorder;  // The regular expression components, in preorder.

    /**
     * This method is called at the start of the scene, and makes both VBoxes grow by equal amounts.
     */
    public void initialize(URL location, ResourceBundle resources) {
        HBox.setHgrow(leftVBox, Priority.ALWAYS);
        HBox.setHgrow(rightVBox, Priority.ALWAYS);
    }

    /**
     * This method is used to pass the parse tree to this controller. It sets all the data for the controller, sets the
     * scene elements and starts off the conversion process.
     *
     * @param parseTree The parse tree created in the previous scene.
     */
    public void setParseTree(ParseTree parseTree) {
        // Set the data.
        RegularExpression regularExpression = parseTree.getRegularExpression();
        parseTreeNodesPreorder = ParseTree.preorderTraversal(parseTree.getRoot());
        regularExpressionsPreorder = RegularExpression.preorderTraversal(regularExpression);
        currentPreorderIndex = 0;
        maxPreorderIndex = parseTreeNodesPreorder.size() - 1;

        // Set the scene elements.
        parseTreeContainer.setContent(parseTree);
        regularExpressionLabel.setText(regularExpression.toString());
        prevButton.setDisable(true);
        if (currentPreorderIndex == maxPreorderIndex) {
            nextButton.setDisable(true);
        }

        // Highlight the first node in the preorder traversal of the parse tree.
        ParseTreeNode currentParseTreeNode = parseTreeNodesPreorder.get(currentPreorderIndex);
        currentParseTreeNode.setCircleStrokeColour("red");

        // Set the info label with the first regular expression in the preorder traversal.
        RegularExpression currentRegularExpression = regularExpressionsPreorder.get(currentPreorderIndex);
        infoLabel.setText("Showing the finite automaton for " + currentRegularExpression.toString() + ".");

        // Set the explanation label.
        explanationLabel.setText(FiniteAutomatonBuilder.getExplanationText(currentRegularExpression));

        // Display the first finite automaton.
        finiteAutomataContainer.setContent(FiniteAutomatonBuilder.buildFiniteAutomaton(currentRegularExpression).getFiniteAutomatonPane());
    }

    /**
     * This method is called when the Next button is pressed. It moves onto the next step of the conversion process.
     */
    public void next() {
        // Remove highlighting from current node.
        ParseTreeNode currentParseTreeNode = parseTreeNodesPreorder.get(currentPreorderIndex);
        currentParseTreeNode.setCircleStrokeColour("black");

        // Increase the index and enable/disable the buttons accordingly.
        currentPreorderIndex += 1;
        if (currentPreorderIndex > 0) {
            prevButton.setDisable(false);
        }
        if (currentPreorderIndex == maxPreorderIndex) {
            nextButton.setDisable(true);
        }

        // Highlight the next node.
        ParseTreeNode nextParseTreeNode = parseTreeNodesPreorder.get(currentPreorderIndex);
        nextParseTreeNode.setCircleStrokeColour("red");

        // Update the info label.
        RegularExpression nextRegularExpression = regularExpressionsPreorder.get(currentPreorderIndex);
        infoLabel.setText("Showing the finite automaton for " + nextRegularExpression.toString() + ".");

        // Update the explanation label.
        explanationLabel.setText(FiniteAutomatonBuilder.getExplanationText(nextRegularExpression));

        // Display the next finite automaton.
        finiteAutomataContainer.setContent(FiniteAutomatonBuilder.buildFiniteAutomaton(nextRegularExpression).getFiniteAutomatonPane());
    }

    /**
     * This method is called when the Previous button is pressed. It moves back to the previous step of the conversion process.
     */
    public void prev() {
        // Remove highlighting from current node.
        ParseTreeNode currentParseTreeNode = parseTreeNodesPreorder.get(currentPreorderIndex);
        currentParseTreeNode.setCircleStrokeColour("black");

        // Decrease the index and enable/disable the buttons accordingly.
        currentPreorderIndex -= 1;
        if (currentPreorderIndex == 0) {
            prevButton.setDisable(true);
        }
        if (currentPreorderIndex < maxPreorderIndex) {
            nextButton.setDisable(false);
        }

        // Highlight the previous node.
        ParseTreeNode previousParseTreeNode = parseTreeNodesPreorder.get(currentPreorderIndex);
        previousParseTreeNode.setCircleStrokeColour("red");

        // Update the info label.
        RegularExpression previousRegularExpression = regularExpressionsPreorder.get(currentPreorderIndex);
        infoLabel.setText("Showing the finite automaton for " + previousRegularExpression.toString() + ".");

        // Update the explanation label.
        explanationLabel.setText(FiniteAutomatonBuilder.getExplanationText(previousRegularExpression));

        // Display the previous finite automaton.
        finiteAutomataContainer.setContent(FiniteAutomatonBuilder.buildFiniteAutomaton(previousRegularExpression).getFiniteAutomatonPane());
    }

}
