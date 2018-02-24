package com.coolcat;

import org.junit.Test;

import static com.coolcat.AppUtil.validateInput;
import static junit.framework.TestCase.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AppUtilTest {
    public static final String INPUT_WITH_SPACE = "let(  a,let(b,10,add(b,b)),let(b,20,add(a,b)))";
    public static final String INPUT_WITH_MISMATCHED_BRACKETS = "add(2,4)))";
    public static final String INPUT_WITH_INVALID_CHAR ="add(2,*)";
    @Test
    public void testValidateInputWithInputWithSpacce(){
        String output = validateInput(INPUT_WITH_SPACE);
        assertThat(output).isEqualToIgnoringWhitespace(INPUT_WITH_SPACE);
        assertThat(output).isNotEqualTo(INPUT_WITH_SPACE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateInputWithMissingBracket(){
        validateInput(INPUT_WITH_MISMATCHED_BRACKETS);
    }

    @Test
    public void testValidateInputWithMissingBracketWithDetails() {
        try {
            validateInput(INPUT_WITH_MISMATCHED_BRACKETS);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Syntax error: missing brackets");
        }
    }
    @Test(expected = IllegalArgumentException.class)
    public void testValidateInputWithInvalidChar(){
        validateInput(INPUT_WITH_INVALID_CHAR);
    }

    @Test
    public void testValidateInputWithInvalidCharWithDetails() {
        try {
            validateInput(INPUT_WITH_INVALID_CHAR);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Syntax error: contain unsupported characters");
        }
    }
}
