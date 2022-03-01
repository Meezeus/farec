package dudzinski.kacper.farec;

import java.util.ArrayList;

/**
 * This abstract class represents a regular expression. A regular expression may be simple or complex.
 */
public abstract class RegularExpression {

    public abstract int getDepth();

    /**
     * Performs a preorder traversal of the given regular expression, considering its component subexpressions.
     * @param regularExpression The regular expression to traverse.
     * @return A list of component regular expressions, in preorder.
     */
    public static ArrayList<RegularExpression> preorderTraversal(RegularExpression regularExpression) {
        ArrayList<RegularExpression> currentList = new ArrayList<>();
        if (regularExpression instanceof ComplexRegularExpression) {
            ComplexRegularExpression complexRegularExpression = (ComplexRegularExpression) regularExpression;
            RegularExpression leftChild = complexRegularExpression.getLeftOperand();
            RegularExpression rightChild = complexRegularExpression.getRightOperand();
            if (leftChild != null) {
                currentList.addAll(preorderTraversal(leftChild));
            }
            if (rightChild !=  null) {
                currentList.addAll(preorderTraversal(rightChild));
            }
        }
        currentList.add(regularExpression);
        return currentList;
    }

}
