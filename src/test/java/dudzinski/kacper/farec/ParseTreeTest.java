package dudzinski.kacper.farec;

import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ParseTreeTest {
    @BeforeAll
    static void startJavaFX(){
        Platform.startup(() -> {});
    }

    @Nested
    @DisplayName("Preorder traversal returns")
    class PreorderTraversalTest {
        @Nested
        @DisplayName("the correct nodes when the regex string is")
        class PreorderTraversalPositiveTest {
            @Test
            @DisplayName("1")
            void test1() {
                RegularExpression regularExpression = Parser.parse("1");
                ParseTree parseTree = new ParseTree(regularExpression);
                ArrayList<ParseTreeNode> nodeList = ParseTree.preorderTraversal(parseTree.getRoot());
                assertEquals("1", nodeList.stream().map(ParseTreeNode::toString).collect(Collectors.joining(",")));
            }
            @Test
            @DisplayName("1*")
            void test2() {
                RegularExpression regularExpression = Parser.parse("1*");
                ParseTree parseTree = new ParseTree(regularExpression);
                ArrayList<ParseTreeNode> nodeList = ParseTree.preorderTraversal(parseTree.getRoot());
                assertEquals("1,*", nodeList.stream().map(ParseTreeNode::toString).collect(Collectors.joining(",")));
            }
            @Test
            @DisplayName("1+2")
            void test3() {
                RegularExpression regularExpression = Parser.parse("1+2");
                ParseTree parseTree = new ParseTree(regularExpression);
                ArrayList<ParseTreeNode> nodeList = ParseTree.preorderTraversal(parseTree.getRoot());
                assertEquals("1,2,+", nodeList.stream().map(ParseTreeNode::toString).collect(Collectors.joining(",")));
            }
            @Test
            @DisplayName("(1+2)|(3*+(4|5))")
            void test4() {
                RegularExpression regularExpression = Parser.parse("(1+2)|(3*+(4|5))");
                ParseTree parseTree = new ParseTree(regularExpression);
                ArrayList<ParseTreeNode> nodeList = ParseTree.preorderTraversal(parseTree.getRoot());
                assertEquals("1,2,+,3,*,4,5,|,+,|", nodeList.stream().map(ParseTreeNode::toString).collect(Collectors.joining(",")));
            }
        }
    }

}