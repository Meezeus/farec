package dudzinski.kacper.farec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegularExpressionTest {
    @Nested
    @DisplayName("The depth of a regular expression is returned correctly when the regex string is")
    class GetDepthTest {
        @Test
        @DisplayName("a")
        void test1(){
            RegularExpression regularExpression = new SimpleRegularExpression('a');
            int depth = regularExpression.getDepth();
            assertEquals(0, depth);
        }
        @Test
        @DisplayName("a*")
        void test2(){
            RegularExpression regularExpression = Parser.parse("a*");
            int depth = regularExpression.getDepth();
            assertEquals(1, depth);
        }
        @Test
        @DisplayName("a+b")
        void test3(){
            RegularExpression regularExpression = Parser.parse("a+b");
            int depth = regularExpression.getDepth();
            assertEquals(1, depth);
        }
        @Test
        @DisplayName("a*+b")
        void test4(){
            RegularExpression regularExpression = Parser.parse("a*+b");
            int depth = regularExpression.getDepth();
            assertEquals(2, depth);
        }
        @Test
        @DisplayName("a+b+c")
        void test5(){
            RegularExpression regularExpression = Parser.parse("a+b+c");
            int depth = regularExpression.getDepth();
            assertEquals(2, depth);
        }
        @Test
        @DisplayName("(a+b)+(c+d)")
        void test6(){
            RegularExpression regularExpression = Parser.parse("(a+b)+(c+d)");
            int depth = regularExpression.getDepth();
            assertEquals(2, depth);
        }
        @Test
        @DisplayName("(a+b)*+(c+d)")
        void test7(){
            RegularExpression regularExpression = Parser.parse("(a+b)*+(c+d)");
            int depth = regularExpression.getDepth();
            assertEquals(3, depth);
        }
        @Test
        @DisplayName("((a*)*)*")
        void test8(){
            RegularExpression regularExpression = Parser.parse("(a+b)*+(c+d)");
            int depth = regularExpression.getDepth();
            assertEquals(3, depth);
        }
    }

    @Nested
    @DisplayName("Preorder traversal returns")
    class PreorderTraversalTest {
        @Nested
        @DisplayName("the correct regular expressions when the regex string is")
        class PreorderTraversalPositiveTest {
            @Test
            @DisplayName("1")
            void test1() {
                ArrayList<String> trueRegexStringList = new ArrayList<>();
                trueRegexStringList.add("1");

                RegularExpression regularExpression = Parser.parse("1");
                ArrayList<RegularExpression> testRegexList = RegularExpression.preorderTraversal(regularExpression);

                assertEquals(trueRegexStringList.size(), testRegexList.size());
                for (int index = 0; index < trueRegexStringList.size(); index++) {
                    assertEquals(trueRegexStringList.get(index), testRegexList.get(index).toString());
                }
            }
            @Test
            @DisplayName("1*")
            void test2() {
                ArrayList<String> trueRegexStringList = new ArrayList<>();
                trueRegexStringList.add("1");
                trueRegexStringList.add("(1)*");

                RegularExpression regularExpression = Parser.parse("1*");
                ArrayList<RegularExpression> testRegexList = RegularExpression.preorderTraversal(regularExpression);

                assertEquals(trueRegexStringList.size(), testRegexList.size());
                for (int index = 0; index < trueRegexStringList.size(); index++) {
                    assertEquals(trueRegexStringList.get(index), testRegexList.get(index).toString());
                }
            }
            @Test
            @DisplayName("1+2")
            void test3() {
                ArrayList<String> trueRegexStringList = new ArrayList<>();
                trueRegexStringList.add("1");
                trueRegexStringList.add("2");
                trueRegexStringList.add("(1)+(2)");

                RegularExpression regularExpression = Parser.parse("1+2");
                ArrayList<RegularExpression> testRegexList = RegularExpression.preorderTraversal(regularExpression);

                assertEquals(trueRegexStringList.size(), testRegexList.size());
                for (int index = 0; index < trueRegexStringList.size(); index++) {
                    assertEquals(trueRegexStringList.get(index), testRegexList.get(index).toString());
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

                RegularExpression regularExpression = Parser.parse("(1+2)|(3*+(4|5))");
                ArrayList<RegularExpression> testRegexList = RegularExpression.preorderTraversal(regularExpression);

                assertEquals(trueRegexStringList.size(), testRegexList.size());
                for (int index = 0; index < trueRegexStringList.size(); index++) {
                    assertEquals(trueRegexStringList.get(index), testRegexList.get(index).toString());
                }
            }
        }
    }

}
