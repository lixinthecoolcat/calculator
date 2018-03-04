package com.coolcat;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FunctionalNormalOpTest {
    private static final String OP_MULT_LAYER = "mult(add(2,2),div(9,3))";
    private static final String OP_DOUBLE_LAYER = "add(1,mult(2,3))";
    private static final String OP_SIMPLE = "add(1,2)";
    private static final String ONLY_INTEGER = "2";
    private static final String ONLY_DECIMAL = "2.0";
    private static final String ONLY_INTEGER_V1 = "-1";

    @Test
    public void testOperatorWithMultiLayer() {

        Double result = Calculator.doCalculate(OP_MULT_LAYER);
        assertThat(result).isEqualTo(12);
    }

    @Test
    public void testOperatorWithDoubleLayer() {

        Double result = Calculator.doCalculate(OP_DOUBLE_LAYER);
        assertThat(result).isEqualTo(7);
    }

    @Test
    public void testSimpleOperator() {

        Double result = Calculator.doCalculate(OP_SIMPLE);
        assertThat(result).isEqualTo(3);
    }

    @Test
    public void testSimpleInteger() {
        Double result = Calculator.doCalculate(ONLY_INTEGER);
        assertThat(result).isEqualTo(NumberUtils.toDouble(ONLY_INTEGER));
    }

    @Test
    public void testSimpleIntegerV1() {
        Double result = Calculator.doCalculate(ONLY_INTEGER_V1);
        assertThat(result).isEqualTo(NumberUtils.toDouble(ONLY_INTEGER_V1));
    }

    @Test(expected = NumberFormatException.class)
    public void testSimpleDecimal() {
        //Throw ex due to we only support Integer from inputs.
        Calculator.doCalculate(ONLY_DECIMAL);
    }
}
