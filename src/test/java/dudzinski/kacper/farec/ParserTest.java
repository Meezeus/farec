package dudzinski.kacper.farec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    Parser parser = new Parser();

    @Nested
    @DisplayName("Parser returns correct operator")
    class getOperatorTest {
        @Test
        @DisplayName("when the char is *")
        void test1(){
            char operatorChar = '*';
            Parser.REOperators operator = parser.getOperator(operatorChar);
            assertEquals(operator, Parser.REOperators.STAR);
        }
        @Test
        @DisplayName("when the char is |")
        void test2(){
            char operatorChar = '|';
            Parser.REOperators operator = parser.getOperator(operatorChar);
            assertEquals(operator, Parser.REOperators.CONCATENATION);
        }
        @Test
        @DisplayName("when the char is +")
        void test3(){
            char operatorChar = '+';
            Parser.REOperators operator = parser.getOperator(operatorChar);
            assertEquals(operator, Parser.REOperators.UNION);
        }
        @Test
        @DisplayName("when the char is not an operator")
        void test4(){
            char operatorChar = 'a';
            assertThrows(IllegalArgumentException.class, () -> parser.getOperator(operatorChar));
        }
    }

    @Nested
    @DisplayName("Parser validates strings correctly")
    class IsValidTest {
        @Test
        @DisplayName("when they are valid")
        void test1(){
            String testString = "(a+3)|c*";
            assertTrue(parser.isValid(testString));
        }
        @Test
        @DisplayName("when they are invalid because of too many open brackets")
        void test2(){
            String testString = "((a+b)|c*";
            assertFalse(parser.isValid(testString));
        }
        @Test
        @DisplayName("when they are invalid because of too few open brackets")
        void test3(){
            String testString = "a+b)|c*";
            assertFalse(parser.isValid(testString));
        }
        @Test
        @DisplayName("when they are invalid because of illegal characters")
        void test4(){
            String testString = "($+&)|!*";
            assertFalse(parser.isValid(testString));
        }
    }

    @Nested
    @DisplayName("Parser removes outer brackets correctly")
    class RemoveOuterBracketsTest {
        @Test
        @DisplayName("when the string is (a)")
        void test1() {
            String testString = "(a)";
            String resultString = parser.removeOuterBrackets(testString);
            assertEquals(resultString, "a");
        }
        @Test
        @DisplayName("when the string is (a+b)")
        void test2() {
            String testString = "(a+b)";
            String resultString = parser.removeOuterBrackets(testString);
            assertEquals(resultString, "a+b");
        }
        @Test
        @DisplayName("when the string is ((a+b)+c)")
        void test3() {
            String testString = "((a+b)+c)";
            String resultString = parser.removeOuterBrackets(testString);
            assertEquals(resultString, "(a+b)+c");
        }
        @Test
        @DisplayName("when the string is (c+(a+b))")
        void test4() {
            String testString = "(c+(a+b))";
            String resultString = parser.removeOuterBrackets(testString);
            assertEquals(resultString, "c+(a+b)");
        }
    }

    @Nested
    @DisplayName("Parser finds root index")
    class FindRootIndexTest{
        @Test
        @DisplayName("when the regex is a+b")
        void test1(){
            String testString = "a+b";
            int rootIndex = parser.findRootIndex(testString);
            assertEquals(rootIndex, 1);
        }
        @Test
        @DisplayName("when the regex is (a+b)+c")
        void test2(){
            String testString = "(a+b)+c";
            int rootIndex = parser.findRootIndex(testString);
            assertEquals(rootIndex, 5);
        }
        @Test
        @DisplayName("when the regex is (a+b)+(c+d)")
        void test3(){
            String testString = "(a+b)+(c+d)";
            int rootIndex = parser.findRootIndex(testString);
            assertEquals(rootIndex, 5);
        }
        @Test
        @DisplayName("when the regex is a*")
        void test4(){
            String testString = "a*";
            int rootIndex = parser.findRootIndex(testString);
            assertEquals(rootIndex, 1);
        }
        @Test
        @DisplayName("when the regex is (a+b)*")
        void test5(){
            String testString = "(a+b)*";
            int rootIndex = parser.findRootIndex(testString);
            assertEquals(rootIndex, 5);
        }
        @Test
        @DisplayName("when the regex is ((a+b)*)+(c*)")
        void test6(){
            String testString = "((a+b)*)+(c*)";
            int rootIndex = parser.findRootIndex(testString);
            assertEquals(rootIndex, 8);
        }
        @Test
        @DisplayName("when there is no root index")
        void test7(){
            String testString = "a";
            int rootIndex = parser.findRootIndex(testString);
            assertEquals(rootIndex, -1);
        }
    }

    @Nested
    @DisplayName("Parser can parse")
    class ParseRegexTest{
        @Test
        @DisplayName("the regex a")
        void test1(){
            String testString = "a";
            RegularExpression regex = parser.parse(testString);
            assertEquals(regex.toString(), "a");
        }
        @Test
        @DisplayName("the regex a*")
        void test2(){
            String testString = "a*";
            RegularExpression regex = parser.parse(testString);
            assertEquals(regex.toString(), "(a)STAR(null)");
        }
        @Test
        @DisplayName("the regex a+b")
        void test3(){
            String testString = "a+b";
            RegularExpression regex = parser.parse(testString);
            assertEquals(regex.toString(), "(a)UNION(b)");
        }
        @Test
        @DisplayName("the regex (a*)+b")
        void test4(){
            String testString = "(a*)+b";
            RegularExpression regex = parser.parse(testString);
            assertEquals(regex.toString(), "((a)STAR(null))UNION(b)");
        }
        @Test
        @DisplayName("the regex a+(b*)")
        void test5(){
            String testString = "a+(b*)";
            RegularExpression regex = parser.parse(testString);
            assertEquals(regex.toString(), "(a)UNION((b)STAR(null))");
        }
        @Test
        @DisplayName("the regex (a+b)+c")
        void test6(){
            String testString = "(a+b)+c";
            RegularExpression regex = parser.parse(testString);
            assertEquals(regex.toString(), "((a)UNION(b))UNION(c)");
        }
        @Test
        @DisplayName("the regex c+(a+b)")
        void test7(){
            String testString = "c+(a+b)";
            RegularExpression regex = parser.parse(testString);
            assertEquals(regex.toString(), "(c)UNION((a)UNION(b))");
        }
        @Test
        @DisplayName("the regex (a+b)|(c+d)")
        void test8(){
            String testString = "(a+b)|(c+d)";
            RegularExpression regex = parser.parse(testString);
            assertEquals(regex.toString(), "((a)UNION(b))CONCATENATION((c)UNION(d))");
        }
        @Test
        @DisplayName("the regex ((a+b)|c)|((d*)+a)")
        void test9(){
            String testString = "((a+b)|c)|((d*)+a)";
            RegularExpression regex = parser.parse(testString);
            assertEquals(regex.toString(), "(((a)UNION(b))CONCATENATION(c))CONCATENATION(((d)STAR(null))UNION(a))");
        }
    }

}