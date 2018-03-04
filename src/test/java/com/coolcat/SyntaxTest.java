package com.coolcat;

import org.junit.Test;

import static com.coolcat.AppUtil.*;
import static com.coolcat.TreeUtil.parseNode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
 /*
        System input, should be regulated: input is required to be trimmed,lowercase,no whitespace within.
        Those has done in one line with lib's methods to update input before logic goes to syntax check in code.
        I do trust lib's methods so not put them in method to unit test against. But be aware when adding new cases
        make sure input string is regulated format.
     */

public class SyntaxTest {
    private static final String INPUT_WITH_MISMATCHED_BRACKETS = "add(2,4)))";
    private static final String INPUT_WITH_INVALID_CHAR = "add(2,*)";
    private static final String INPUT_WITH_INVALID_LEAF_VALUE = "div(3,a)";
    private static final String INPUT_WITH_INVALID_OPERATOR = "mul(3,1)";
    private static final String INPUT_WITH_INVALID_ARGUMENTS = "add(1)";

    private static final String INPUT_WITH_NON_OP_NOR_DIGIT = "ahha";
    private static final String INPUT_WITH_ONLY_OP = "sub";
    private static final String INPUT_WITH_UNBALANCED_BRACKETS = "add(1,2";
    private static final String INPUT_WITH_MORE_ARGUMENTS = "mult(3,1,4)";

    @Test
    public void testSyntaxCheckWithMissingBracket() {
        try {
            syntaxCheck(INPUT_WITH_MISMATCHED_BRACKETS);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo(UNBALANCED);
        }
    }


    @Test
    public void testSyntaxCheckWithInvalidChar() {
        try {
            syntaxCheck(INPUT_WITH_INVALID_CHAR);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo(UNSUPPORTED);
        }
    }

    @Test
    public void testParseNodeWithInvalidLeafValue() {
        try {
            parseNode(INPUT_WITH_INVALID_LEAF_VALUE, null);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).contains(INVALID_LEAF);
        }
    }

    @Test
    public void testParseNodeWithInvalidOperator() {
        try {
            parseNode(INPUT_WITH_INVALID_OPERATOR, null);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).contains(INVALID_OP);
        }
    }

    @Test
    public void testParseNodeWithInvalidArguments() {
        try {
            parseNode(INPUT_WITH_INVALID_ARGUMENTS, null);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).contains(INVALID_ARGUMENT);
        }
    }

    @Test
    public void testParseNodeWithMissingBrackets() {
        try {
            parseNode(INPUT_WITH_UNBALANCED_BRACKETS, null);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo(UNBALANCED);
        }
    }

    @Test
    public void testParseNodeWithMoreArguments() {
        try {
            parseNode(INPUT_WITH_MORE_ARGUMENTS, null);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).contains(INVALID_OP);
        }
    }

    @Test
    public void testParseNodeWithNonOpNorDigit() {
        try {
            parseNode(INPUT_WITH_NON_OP_NOR_DIGIT, null);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).contains(INVALID_OP);
        }
    }

    @Test
    public void testParseNodeWithOnlyOp() {
        try {
            parseNode(INPUT_WITH_ONLY_OP, null);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo(MISSING_ARGUMENTS + INPUT_WITH_ONLY_OP);
        }
    }
}
