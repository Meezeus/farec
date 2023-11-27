package dudzinski.kacper.farec.regex;

import java.util.ArrayList;

/**
 * This abstract class represents a regular expression (regex). A regular
 * expression is a pattern consisting of regex operators applied to operands.
 * Operators are special symbols while operands are symbols from the alphabet as
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
     * Returns the preorder traversal of the subexpressions of this regular
     * expression.
     *
     * @return the preorder traversal of the subexpressions of this regular
     * expression
     */
    public ArrayList<RegularExpression> preorderTraversal() {
        // Create the list.
        ArrayList<RegularExpression> currentList = new ArrayList<>();

        // If this regular expression is complex, add the preorder traversal of
        // its left child followed by its right child to the list.
        if (this instanceof ComplexRegularExpression complexRegularExpression) {
            // Get the children.
            RegularExpression leftChild =
                    complexRegularExpression.getLeftOperand();
            RegularExpression rightChild =
                    complexRegularExpression.getRightOperand();

            // If they are not null, add their preorder traversal to the list.
            if (leftChild != null) {
                currentList.addAll(leftChild.preorderTraversal());
            }
            if (rightChild != null) {
                currentList.addAll(rightChild.preorderTraversal());
            }
        }

        // Add this regular expression to the list and return it.
        currentList.add(this);
        return currentList;
    }

}
