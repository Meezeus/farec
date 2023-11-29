package dudzinski.kacper.farec.regex;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the {@link RegularExpression} class and its methods.
 */
public class RegularExpressionTest {

    /**
     * Make sure the operators are set to their default symbols before each
     * test.
     */
    @BeforeEach
    void resetOperatorChars() {
        RegularExpressionSettings.setOperatorChar(RegexOperator.STAR, '*');
        RegularExpressionSettings.setOperatorChar(RegexOperator.UNION, '+');
        RegularExpressionSettings.setOperatorChar(RegexOperator.CONCATENATION,
                                                  '|');
    }

    /**
     * Test class for the {@link RegularExpression#getDepth()} method.
     */
    @Nested
    @DisplayName("The depth of a regular expression is returned correctly" +
                 " when the regex string is")
    class GetDepthTest {
        @Test
        @DisplayName("a")
        void test1() {
            RegularExpression regularExpression =
                    new SimpleRegularExpression('a');
            int depth = regularExpression.getDepth();
            assertEquals(0, depth);
        }

        @Test
        @DisplayName("a*")
        void test2() {
            String regexString = "a*";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            int depth = regularExpression.getDepth();
            assertEquals(1, depth);
        }

        @Test
        @DisplayName("a+b")
        void test3() {
            String regexString = "a+b";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            int depth = regularExpression.getDepth();
            assertEquals(1, depth);
        }

        @Test
        @DisplayName("a*+b")
        void test4() {
            String regexString = "a*+b";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            int depth = regularExpression.getDepth();
            assertEquals(2, depth);
        }

        @Test
        @DisplayName("a+b+c")
        void test5() {
            String regexString = "a+b+c";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            int depth = regularExpression.getDepth();
            assertEquals(2, depth);
        }

        @Test
        @DisplayName("(a+b)+(c+d)")
        void test6() {
            String regexString = "(a+b)+(c+d)";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            int depth = regularExpression.getDepth();
            assertEquals(2, depth);
        }

        @Test
        @DisplayName("(a+b)*+(c+d)")
        void test7() {
            String regexString = "(a+b)*+(c+d)";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            int depth = regularExpression.getDepth();
            assertEquals(3, depth);
        }

        @Test
        @DisplayName("((a*)*)*")
        void test8() {
            String regexString = "((a*)*)*";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            int depth = regularExpression.getDepth();
            assertEquals(3, depth);
        }
    }

    /**
     * Test class for the {@link RegularExpression#preorderTraversal()} method.
     */
    @Nested
    @DisplayName("Preorder traversal returns the correct regular expressions" +
                 " when the regex string is")
    class PreorderTraversalTest {
        @Test
        @DisplayName("1")
        void test1() {
            ArrayList<String> trueRegexStringList = new ArrayList<>();
            trueRegexStringList.add("1");

            String regexString = "1";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            ArrayList<RegularExpression> testRegexList =
                    regularExpression.preorderTraversal();

            assertEquals(trueRegexStringList.size(), testRegexList.size());
            for (int index = 0; index < trueRegexStringList.size(); index++) {
                assertEquals(trueRegexStringList.get(index),
                             testRegexList.get(index).toString());
            }
        }

        @Test
        @DisplayName("1*")
        void test2() {
            ArrayList<String> trueRegexStringList = new ArrayList<>();
            trueRegexStringList.add("1");
            trueRegexStringList.add("(1)*");

            String regexString = "1*";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            ArrayList<RegularExpression> testRegexList =
                    regularExpression.preorderTraversal();

            assertEquals(trueRegexStringList.size(), testRegexList.size());
            for (int index = 0; index < trueRegexStringList.size(); index++) {
                assertEquals(trueRegexStringList.get(index),
                             testRegexList.get(index).toString());
            }
        }

        @Test
        @DisplayName("1+2")
        void test3() {
            ArrayList<String> trueRegexStringList = new ArrayList<>();
            trueRegexStringList.add("1");
            trueRegexStringList.add("2");
            trueRegexStringList.add("(1)+(2)");

            String regexString = "1+2";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            ArrayList<RegularExpression> testRegexList =
                    regularExpression.preorderTraversal();

            assertEquals(trueRegexStringList.size(), testRegexList.size());
            for (int index = 0; index < trueRegexStringList.size(); index++) {
                assertEquals(trueRegexStringList.get(index),
                             testRegexList.get(index).toString());
            }
        }

        @Test
        @DisplayName("(1+2)|(3*+(4|5))")
        void test4() {
            ArrayList<String> trueRegexStringList = new ArrayList<>();
            trueRegexStringList.add("1");
            trueRegexStringList.add("2");
            trueRegexStringList.add("(1)+(2)");
            trueRegexStringList.add("3");
            trueRegexStringList.add("(3)*");
            trueRegexStringList.add("4");
            trueRegexStringList.add("5");
            trueRegexStringList.add("(4)|(5)");
            trueRegexStringList.add("((3)*)+((4)|(5))");
            trueRegexStringList.add("((1)+(2))|(((3)*)+((4)|(5)))");

            String regexString = "(1+2)|(3*+(4|5))";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            ArrayList<RegularExpression> testRegexList =
                    regularExpression.preorderTraversal();

            assertEquals(trueRegexStringList.size(), testRegexList.size());
            for (int index = 0; index < trueRegexStringList.size(); index++) {
                assertEquals(trueRegexStringList.get(index),
                             testRegexList.get(index).toString());
            }
        }
    }

}
