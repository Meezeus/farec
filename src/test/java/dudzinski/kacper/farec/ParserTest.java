package dudzinski.kacper.farec;

import javafx.util.Pair;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Nested
    @DisplayName("Validating a regex string returns")
    class IsValidTest {
        @Nested
        @DisplayName("true when the regex string is")
        class IsValidPositiveTest {
            @Test
            @DisplayName("a")
            void test1(){
                String regexString = "a";
                Pair<Boolean, String> isValid = Parser.isValid(regexString);
                assertTrue(isValid.getKey());
            }
            @Test
            @DisplayName("a*")
            void test2(){
                String regexString = "a*";
                Pair<Boolean, String> isValid = Parser.isValid(regexString);
                assertTrue(isValid.getKey());
            }
            @Test
            @DisplayName("a+b")
            void test3(){
                String regexString = "a+b";
                Pair<Boolean, String> isValid = Parser.isValid(regexString);
                assertTrue(isValid.getKey());
            }
            @Test
            @DisplayName("(a+b)|c*")
            void test4(){
                String regexString = "(a+b)|c*";
                Pair<Boolean, String> isValid = Parser.isValid(regexString);
                assertTrue(isValid.getKey());
            }
        }
        @Nested
        @DisplayName("false when the regex string is invalid because of")
        class IsValidNegativeTest {
            @Test
            @DisplayName("illegal characters")
            void test1(){
                String regexString = "($+&)|!*";
                Pair<Boolean, String> isValid = Parser.isValid(regexString);
                assertFalse(isValid.getKey());
                assertEquals("Regular expressions can only contain alphanumeric characters and operators!", isValid.getValue());
            }
            @Test
            @DisplayName("brackets in the wrong order")
            void test2(){
                String regexString = ")a+b(|c*";
                Pair<Boolean, String> isValid = Parser.isValid(regexString);
                assertFalse(isValid.getKey());
                assertEquals("The regular expression has a closing bracket without an opening bracket!", isValid.getValue());
            }
            @Test
            @DisplayName("different numbers of opening and closing brackets")
            void test3(){
                String regexString = "(a+b|c*";
                Pair<Boolean, String> isValid = Parser.isValid(regexString);
                assertFalse(isValid.getKey());
                assertEquals("The regular expression has different numbers of opening and closing brackets!", isValid.getValue());
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
        @DisplayName("found correctly when the regex string is")
        class FindRootIndexPositiveTest{
            @Test
            @DisplayName("a*")
            void test1(){
                String regexString = "a*";
                int rootIndex = Parser.findRootIndex(regexString);
                assertEquals(1, rootIndex);
            }
            @Test
            @DisplayName("a+b")
            void test2(){
                String regexString = "a+b";
                int rootIndex = Parser.findRootIndex(regexString);
                assertEquals(1, rootIndex);
            }
            @Test
            @DisplayName("(a+b)+c")
            void test3(){
                String regexString = "(a+b)+c";
                int rootIndex = Parser.findRootIndex(regexString);
                assertEquals(5, rootIndex);
            }
            @Test
            @DisplayName("c+(a+b)")
            void test4(){
                String regexString = "c+(a+b)";
                int rootIndex = Parser.findRootIndex(regexString);
                assertEquals(1, rootIndex);
            }
            @Test
            @DisplayName("(a+b)+(c+d)")
            void test5(){
                String regexString = "(a+b)+(c+d)";
                int rootIndex = Parser.findRootIndex(regexString);
                assertEquals(5, rootIndex);
            }
            @Test
            @DisplayName("(a+b)*")
            void test6(){
                String regexString = "(a+b)*";
                int rootIndex = Parser.findRootIndex(regexString);
                assertEquals(5, rootIndex);
            }
            @Test
            @DisplayName("((a+b)*)+(c*)")
            void test7(){
                String regexString = "((a+b)*)+(c*)";
                int rootIndex = Parser.findRootIndex(regexString);
                assertEquals(8, rootIndex);
            }
            @Test
            @DisplayName("a*+b")
            void test8(){
                String regexString = "a*+b";
                int rootIndex = Parser.findRootIndex(regexString);
                assertEquals(2, rootIndex);
            }
        }
        @Nested
        @DisplayName("equal to -1 when the regex string is")
        class FindRootIndexNegativeTest{
            @Test
            @DisplayName("a")
            void test1(){
                String regexString = "a";
                int rootIndex = Parser.findRootIndex(regexString);
                assertEquals(-1, rootIndex);
            }
            @Test
            @DisplayName("aa")
            void test2(){
                String regexString = "a";
                int rootIndex = Parser.findRootIndex(regexString);
                assertEquals(-1, rootIndex);
            }
        }
    }

    @Nested
    @DisplayName("Parsing a regex string")
    class ParseRegexStringTest {
        @Nested
        @DisplayName("returns the correct regular expression when the regex string is")
        class ParseRegexStringPositiveTest{
            @Test
            @DisplayName("a")
            void test1(){
                String regexString = "a";
                RegularExpression regularExpression = Parser.parse(regexString);
                assertEquals("a", regularExpression.toString());
            }
            @Test
            @DisplayName("a*")
            void test2(){
                String regexString = "a*";
                RegularExpression regularExpression = Parser.parse(regexString);
                assertEquals("(a)*(null)", regularExpression.toString());
            }
            @Test
            @DisplayName("a+b")
            void test3(){
                String regexString = "a+b";
                RegularExpression regularExpression = Parser.parse(regexString);
                assertEquals("(a)+(b)", regularExpression.toString());
            }
            @Test
            @DisplayName("a*+b")
            void test4(){
                String regexString = "a*+b";
                RegularExpression regularExpression = Parser.parse(regexString);
                assertEquals("((a)*(null))+(b)", regularExpression.toString());
            }
            @Test
            @DisplayName("a+b*")
            void test5(){
                String regexString = "a+b*";
                RegularExpression regularExpression = Parser.parse(regexString);
                assertEquals("(a)+((b)*(null))", regularExpression.toString());
            }
            @Test
            @DisplayName("(a+b)+c")
            void test6(){
                String regexString = "(a+b)+c";
                RegularExpression regularExpression = Parser.parse(regexString);
                assertEquals("((a)+(b))+(c)", regularExpression.toString());
            }
            @Test
            @DisplayName("c+(a+b)")
            void test7(){
                String regexString = "c+(a+b)";
                RegularExpression regularExpression = Parser.parse(regexString);
                assertEquals("(c)+((a)+(b))", regularExpression.toString());
            }
            @Test
            @DisplayName("(a+b)|(c+d)")
            void test8(){
                String regexString = "(a+b)|(c+d)";
                RegularExpression regularExpression = Parser.parse(regexString);
                assertEquals("((a)+(b))|((c)+(d))", regularExpression.toString());
            }
            @Test
            @DisplayName("((a+b)|c)|(d*+a)")
            void test9(){
                String regexString = "((a+b)|c)|(d*+a)";
                RegularExpression regularExpression = Parser.parse(regexString);
                assertEquals("(((a)+(b))|(c))|(((d)*(null))+(a))", regularExpression.toString());
            }
            @Test
            @DisplayName("a+a*+a")
            void test10(){
                String regexString = "a+a*+a";
                RegularExpression regularExpression = Parser.parse(regexString);
                assertEquals("(a)+(((a)*(null))+(a))", regularExpression.toString());
            }
            @Test
            @DisplayName("(a*)*")
            void test11(){
                String regexString = "(a*)*";
                RegularExpression regularExpression = Parser.parse(regexString);
                assertEquals("((a)*(null))*(null)", regularExpression.toString());
            }
        }
        @Nested
        @DisplayName("throws an exception when the regex string is")
        class ParseRegexStringNegativeTest{
            @Test
            @DisplayName("empty")
            void test1(){
                String regexString = "";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(regexString));
            }
            @Test
            @DisplayName("+")
            void test2(){
                String regexString = "+";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(regexString));
            }
            @Test
            @DisplayName("*")
            void test3(){
                String regexString = "*";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(regexString));
            }
            @Test
            @DisplayName("*a")
            void test4(){
                String regexString = "*a";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(regexString));
            }
            @Test
            @DisplayName("a+")
            void test5(){
                String regexString = "a+";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(regexString));
            }
            @Test
            @DisplayName("+a")
            void test6(){
                String regexString = "+a";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(regexString));
            }
            @Test
            @DisplayName("a+*")
            void test7(){
                String regexString = "a+*";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(regexString));
            }
            @Test
            @DisplayName("a++")
            void test8(){
                String regexString = "a++";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(regexString));
            }
            @Test
            @DisplayName("++a")
            void test9(){
                String regexString = "++a";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(regexString));
            }
            @Test
            @DisplayName("a*a")
            void tes10(){
                String regexString = "a*a";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(regexString));
            }
            @Test
            @DisplayName("(a*)a")
            void tes11(){
                String regexString = "(a*)a";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(regexString));
            }
            @Test
            @DisplayName("a**")
            void tes12(){
                String regexString = "a**";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(regexString));
            }
            @Test
            @DisplayName("a*+")
            void tes13(){
                String regexString = "a*+";
                assertThrows(IllegalArgumentException.class, () -> Parser.parse(regexString));
            }
        }
    }

}