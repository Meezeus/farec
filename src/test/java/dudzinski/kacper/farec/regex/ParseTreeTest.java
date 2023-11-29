package dudzinski.kacper.farec.regex;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the {@link ParseTree} class and its methods.
 */
class ParseTreeTest {

    /**
     * Because these tests involve JavaFX elements, the JavaFX runtime has to be
     * started first.
     */
    @BeforeAll
    static void startJavaFX() {
        StartJavaFX.startJavaFX();
    }

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
     * Test class for the {@link ParseTree#preorderTraversal()} method.
     */
    @Nested
    @DisplayName("Preorder traversal returns the correct nodes when the regex" +
                 " string is")
    class PreorderTraversalTest {
        @Test
        @DisplayName("1")
        void test1() {
            String regexString = "1";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            ParseTree parseTree = new ParseTree(regularExpression);
            ArrayList<ParseTreeNode> nodeList = parseTree.preorderTraversal();
            assertEquals("1", nodeList.stream().map(ParseTreeNode::getLabelText)
                    .collect(Collectors.joining(",")));
        }

        @Test
        @DisplayName("1*")
        void test2() {
            String regexString = "1*";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            ParseTree parseTree = new ParseTree(regularExpression);
            ArrayList<ParseTreeNode> nodeList = parseTree.preorderTraversal();
            assertEquals("1,*",
                         nodeList.stream().map(ParseTreeNode::getLabelText)
                                 .collect(Collectors.joining(",")));
        }

        @Test
        @DisplayName("1+2")
        void test3() {
            String regexString = "1+2";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            ParseTree parseTree = new ParseTree(regularExpression);
            ArrayList<ParseTreeNode> nodeList = parseTree.preorderTraversal();
            assertEquals("1,2,+",
                         nodeList.stream().map(ParseTreeNode::getLabelText)
                                 .collect(Collectors.joining(",")));
        }

        @Test
        @DisplayName("(1+2)|(3*+(4|5))")
        void test4() {
            String regexString = "(1+2)|(3*+(4|5))";
            RegularExpression regularExpression =
                    Parser.parseRegexString(regexString);
            ParseTree parseTree = new ParseTree(regularExpression);
            ArrayList<ParseTreeNode> nodeList = parseTree.preorderTraversal();
            assertEquals("1,2,+,3,*,4,5,|,+,|",
                         nodeList.stream().map(ParseTreeNode::getLabelText)
                                 .collect(Collectors.joining(",")));
        }
    }

}