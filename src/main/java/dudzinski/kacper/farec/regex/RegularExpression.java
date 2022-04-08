package dudzinski.kacper.farec.regex;

import java.util.ArrayList;

/**
 * This abstract class represents a regular expression (regex). A regular
 * expression is a pattern consisting of regex operators applied to operands.
 * Operators are special symbols while operands symbols from the alphabet as
 * well as the empty string symbol and the empty set symbol.
 *
 * @see SimpleRegularExpression
 * @see ComplexRegularExpression
 * @see RegexOperator
 */
public abstract class RegularExpression {

    /**
     * Returns the depth of this regular expression. The depth of a regular
     * expression is equivalent to the depth of the parse tree representing the
     * regular expression.
     *
     * @return the depth of the regular expression
     */
    public abstract int getDepth();

    /**
     * Returns the postorder traversal of the subexpressions of this regular
     * expression.
     *
     * @return the postorder traversal of the subexpressions of this regular
     * expression
     */
    public ArrayList<RegularExpression> postorderTraversal() {
        // Create the list.
        ArrayList<RegularExpression> currentList = new ArrayList<>();

        // If this regular expression is complex, add the postorder traversal of
        // its left child followed by its right child to the list.
        if (this instanceof ComplexRegularExpression complexRegularExpression) {
            // Get the children.
            RegularExpression leftChild =
                    complexRegularExpression.getLeftOperand();
            RegularExpression rightChild =
                    complexRegularExpression.getRightOperand();

            // If they are not null, add their postorder traversal to the list.
            if (leftChild != null) {
                currentList.addAll(leftChild.postorderTraversal());
            }
            if (rightChild != null) {
                currentList.addAll(rightChild.postorderTraversal());
            }
        }

        // Add this regular expression to the list and return it.
        currentList.add(this);
        return currentList;
    }

}
