package dudzinski.kacper.farec;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Nested
    @DisplayName("Setting an operator char returns")
    class SetOperatorCharTest {
        @Nested
        @DisplayName("true when setting")
        class SetOperatorCharPositiveTest {
            @Test
            @DisplayName("STAR as *")
            void test1(){
                boolean result = Parser.setOperatorChar(Parser.REOperators.STAR, '*');
                assertTrue(result);
            }
            @Test
            @DisplayName("CONCATENATION as |")
            void test2(){
                boolean result = Parser.setOperatorChar(Parser.REOperators.CONCATENATION, '|');
                assertTrue(result);
            }
            @Test
            @DisplayName("STAR as *")
            void test3(){
                boolean result = Parser.setOperatorChar(Parser.REOperators.UNION, '+');
                assertTrue(result);
            }
        }
        @Nested
        @DisplayName("false when setting")
        class SetOperatorCharNegativeTest {
            @Test
            @DisplayName("CONCATENATION as /")
            void test1(){
                boolean result = Parser.setOperatorChar(Parser.REOperators.CONCATENATION, '/');
                assertFalse(result);
            }
        }
    }

    @Nested
    @DisplayName("Getting an operator char returns")
    class getOperatorCharTest {
        @Nested
        @DisplayName("the corresponding char when the operator is")
        class getOperatorCharPositiveTest {
            @Test
            @DisplayName("STAR")
            void test1(){
                Parser.REOperators operator = Parser.REOperators.STAR;
                char operatorChar = Parser.getOperatorChar(operator);
                assertEquals('*', operatorChar);
            }
            @Test
            @DisplayName("CONCATENATION")
            void test2(){
                Parser.REOperators operator = Parser.REOperators.CONCATENATION;
                char operatorChar = Parser.getOperatorChar(operator);
                assertEquals('|', operatorChar);
            }
            @Test
            @DisplayName("UNION")
            void test3(){
                Parser.REOperators operator = Parser.REOperators.UNION;
                char operatorChar = Parser.getOperatorChar(operator);
                assertEquals('+', operatorChar);
            }
        }
    }

    @Nested
    @DisplayName("Getting an operator")
    class getOperatorTest {
        @Nested
        @DisplayName("returns the correct operator when the char is")
        class getOperatorPositiveTest {
            @Test
            @DisplayName("*")
            void test1(){
                char operatorChar = '*';
                Parser.REOperators operator = Parser.getOperator(operatorChar);
                assertEquals(Parser.REOperators.STAR, operator);
            }
            @Test
            @DisplayName("|")
            void test2(){
                char operatorChar = '|';
                Parser.REOperators operator = Parser.getOperator(operatorChar);
                assertEquals(Parser.REOperators.CONCATENATION, operator);
            }
            @Test
            @DisplayName("+")
            void test3(){
                char operatorChar = '+';
                Parser.REOperators operator = Parser.getOperator(operatorChar);
                assertEquals(Parser.REOperators.UNION, operator);
            }
        }
        @Nested
        @DisplayName("throws an error when the char is")
        class getOperatorNegativeTest {
            @Test
            @DisplayName("not linked to any operator")
            void test1(){
                char operatorChar = ',';
                assertThrows(IllegalArgumentException.class, () -> Parser.getOperator(operatorChar));
            }
        }
    }

    @Nested
    @DisplayName("Validating an expression returns")
    class IsValidTest {
        @Nested
        @DisplayName("true when the expression is")
        class IsValidPositiveTest {
            @Test
            @DisplayName("a")
            void test1(){
                String testString = "a";
                assertTrue(Parser.isValid(testString));
            }
            @Test
            @DisplayName("a*")
            void test2(){
                String testString = "a*";
                assertTrue(Parser.isValid(testString));
            }
            @Test
            @DisplayName("a+b")
            void test3(){
                String testString = "a+b";
                assertTrue(Parser.isValid(testString));
            }
            @Test
            @DisplayName("(a+b)|c*")
            void test4(){
                String testString = "(a+b)|c*";
                assertTrue(Parser.isValid(testString));
            }
        }
        @Nested
        @DisplayName("false when the expression is invalid because of")
        class IsValidNegativeTest {
            @Test
            @DisplayName("too many open brackets")
            void test1(){
                String testString = "((a+b)|c*";
                assertFalse(Parser.isValid(testString));
            }
            @Test
            @DisplayName("too few open brackets")
            void test2(){
                String testString = "a+b)|c*";
                assertFalse(Parser.isValid(testString));
            }
            @Test
            @DisplayName("brackets in the wrong order")
            void test3(){
                String testString = ")a+b(|c*";
                assertFalse(Parser.isValid(testString));
            }
            @Test
            @DisplayName("illegal characters")
            void test4(){
                String testString = "($+&)|!*";
                assertFalse(Parser.isValid(testString));
            }
        }
    }

    @Nested
    @DisplayName("Removing outer brackets")
    class RemoveOuterBracketsTest {
        @Nested
        @DisplayName("works correctly when the string is")
        class RemoveOuterBracketsPositiveTest {
            @Test
            @DisplayName("(a)")
            void test1() {
                String testString = "(a)";
                String resultString = Parser.removeOuterBrackets(testString);
                assertEquals("a", resultString);
            }
            @Test
            @DisplayName("(a+b)")
            void test2() {
                String testString = "(a+b)";
                String resultString = Parser.removeOuterBrackets(testString);
                assertEquals("a+b", resultString);
            }
            @Test
            @DisplayName("((a+b)+c)")
            void test3() {
                String testString = "((a+b)+c)";
                String resultString = Parser.removeOuterBrackets(testString);
                assertEquals("(a+b)+c", resultString);
            }
            @Test
            @DisplayName("(c+(a+b))")
            void test4() {
                String testString = "(c+(a+b))";
                String resultString = Parser.removeOuterBrackets(testString);
                assertEquals("c+(a+b)", resultString);
            }
            @Test
            @DisplayName("((a+b)+(c+d))")
            void test5() {
                String testString = "((a+b)+(c+d))";
                String resultString = Parser.removeOuterBrackets(testString);
                assertEquals("(a+b)+(c+d)", resultString);
            }
        }
        @Nested
        @DisplayName("has no effect when the string is")
        class RemoveOuterBracketsNegativeTest {
            @Test
            @DisplayName("a")
            void test1() {
                String testString = "a";
                String resultString = Parser.removeOuterBrackets(testString);
                assertEquals("a", resultString);
            }
            @Test
            @DisplayName("a+b")
            void test2() {
                String testString = "a+b";
                String resultString = Parser.removeOuterBrackets(testString);
                assertEquals("a+b", resultString);
            }
            @Test
            @DisplayName("(a+b)+c")
            void test3() {
                String testString = "(a+b)+c";
                String resultString = Parser.removeOuterBrackets(testString);
                assertEquals("(a+b)+c", resultString);
            }
            @Test
            @DisplayName("c+(a+b)")
            void test4() {
                String testString = "c+(a+b)";
                String resultString = Parser.removeOuterBrackets(testString);
                assertEquals("c+(a+b)", resultString);
            }
            @Test
            @DisplayName("(a+b)+(c+d)")
            void test5() {
                String testString = "(a+b)+(c+d)";
                String resultString = Parser.removeOuterBrackets(testString);
                assertEquals("(a+b)+(c+d)", resultString);
            }
        }
    }

    @Nested
    @DisplayName("The root index is")
    class FindRootIndexTest{
        @Nested
        @DisplayName("found correctly when the regex is")
        class FindRootIndexPositiveTest{
            @Test
            @DisplayName("a*")
            void test1(){
                String testString = "a*";
                int rootIndex = Parser.findRootIndex(testString);
                assertEquals(1, rootIndex);
            }
            @Test
            @DisplayName("a+b")
            void test2(){
                String testString = "a+b";
                int rootIndex = Parser.findRootIndex(testString);
                assertEquals(1, rootIndex);
            }
            @Test
            @DisplayName("(a+b)+c")
            void test3(){
                String testString = "(a+b)+c";
                int rootIndex = Parser.findRootIndex(testString);
                assertEquals(5, rootIndex);
            }
            @Test
            @DisplayName("c+(a+b)")
            void test4(){
                String testString = "c+(a+b)";
                int rootIndex = Parser.findRootIndex(testString);
                assertEquals(1, rootIndex);
            }
            @Test
            @DisplayName("(a+b)+(c+d)")
            void test5(){
                String testString = "(a+b)+(c+d)";
                int rootIndex = Parser.findRootIndex(testString);
                assertEquals(5, rootIndex);
            }
            @Test
            @DisplayName("(a+b)*")
            void test6(){
                String testString = "(a+b)*";
                int rootIndex = Parser.findRootIndex(testString);
                assertEquals(5, rootIndex);
            }
            @Test
            @DisplayName("((a+b)*)+(c*)")
            void test7(){
                String testString = "((a+b)*)+(c*)";
                int rootIndex = Parser.findRootIndex(testString);
                assertEquals(8, rootIndex);
            }
            @Test
            @DisplayName("a*+b")
            void test8(){
                String testString = "a*+b";
                int rootIndex = Parser.findRootIndex(testString);
                assertEquals(2, rootIndex);
            }
        }
        @Nested
        @DisplayName("equal to -1 when the regex is")
        class FindRootIndexNegativeTest{
            @Test
            @DisplayName("a")
            void test1(){
                String testString = "a";
                int rootIndex = Parser.findRootIndex(testString);
                assertEquals(-1, rootIndex);
            }
            @Test
            @DisplayName("aa")
            void test2(){
                String testString = "a";
                int rootIndex = Parser.findRootIndex(testString);
                assertEquals(-1, rootIndex);
            }
        }
    }

    @Nested
    @DisplayName("Parsing a regex string")
    class ParseRegexTest{
        @Nested
        @DisplayName("returns the correct regex when the regex string is")
        class ParseRegexPositiveTest{
            @Test
            @DisplayName("a")
            void test1(){
                String testString = "a";
                RegularExpression regex = Parser.parse(testString);
                assertEquals("a", regex.toString());
            }
            @Test
            @DisplayName("a*")
            void test2(){
                String testString = "a*";
                RegularExpression regex = Parser.parse(testString);
                assertEquals("(a)STAR(null)", regex.toString());
            }
            @Test
            @DisplayName("a+b")
            void test3(){
                String testString = "a+b";
                RegularExpression regex = Parser.parse(testString);
                assertEquals("(a)UNION(b)", regex.toString());
            }
            @Test
            @DisplayName("a*+b")
            void test4(){
                String testString = "a*+b";
                RegularExpression regex = Parser.parse(testString);
                assertEquals("((a)STAR(null))UNION(b)", regex.toString());
            }
            @Test
            @DisplayName("a+b*")
            void test5(){
                String testString = "a+b*";
                RegularExpression regex = Parser.parse(testString);
                assertEquals("(a)UNION((b)STAR(null))", regex.toString());
            }
            @Test
            @DisplayName("(a+b)+c")
            void test6(){
                String testString = "(a+b)+c";
                RegularExpression regex = Parser.parse(testString);
                assertEquals("((a)UNION(b))UNION(c)", regex.toString());
            }
            @Test
            @DisplayName("c+(a+b)")
            void test7(){
                String testString = "c+(a+b)";
                RegularExpression regex = Parser.parse(testString);
                assertEquals("(c)UNION((a)UNION(b))", regex.toString());
            }
            @Test
            @DisplayName("(a+b)|(c+d)")
            void test8(){
                String testString = "(a+b)|(c+d)";
                RegularExpression regex = Parser.parse(testString);
                assertEquals("((a)UNION(b))CONCATENATION((c)UNION(d))", regex.toString());
            }
            @Test
            @DisplayName("((a+b)|c)|(d*+a)")
            void test9(){
                String testString = "((a+b)|c)|(d*+a)";
                RegularExpression regex = Parser.parse(testString);
                assertEquals("(((a)UNION(b))CONCATENATION(c))CONCATENATION(((d)STAR(null))UNION(a))", regex.toString());
            }
            @Test
            @DisplayName("a+a*+a")
            void test10(){
                String testString = "a+a*+a";
                RegularExpression regex = Parser.parse(testString);
                assertEquals("(a)UNION(((a)STAR(null))UNION(a))", regex.toString());
            }
            @Test
            @DisplayName("(a*)*")
            void test11(){
                String testString = "(a*)*";
                RegularExpression regex = Parser.parse(testString);
                assertEquals("((a)STAR(null))STAR(null)", regex.toString());
            }
            @Test
            @DisplayName("a**")
            void test12(){
                String testString = "a**";
                RegularExpression regex = Parser.parse(testString);
                assertEquals("(a)STAR(null)", regex.toString());
            }
        }
        @Nested
        @DisplayName("throws an exception when the regex string is")
        class ParseRegexNegativeTest{
            @Test
            @DisplayName("empty")
            void test1(){
                String testString = "";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(testString));
            }
            @Test
            @DisplayName("+")
            void test2(){
                String testString = "+";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(testString));
            }
            @Test
            @DisplayName("*")
            void test3(){
                String testString = "*";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(testString));
            }
            @Test
            @DisplayName("*a")
            void test4(){
                String testString = "*a";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(testString));
            }
            @Test
            @DisplayName("a+")
            void test5(){
                String testString = "a+";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(testString));
            }
            @Test
            @DisplayName("+a")
            void test6(){
                String testString = "+a";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(testString));
            }
            @Test
            @DisplayName("a+*")
            void test7(){
                String testString = "a+*";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(testString));
            }
            @Test
            @DisplayName("a++")
            void test8(){
                String testString = "a++";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(testString));
            }
            @Test
            @DisplayName("++a")
            void test9(){
                String testString = "++a";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(testString));
            }
        }
    }

}