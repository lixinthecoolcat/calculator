package com.coolcat;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FunctionalBigPictureTest {
    private static final String LET_MULT_LAYER = "let(a,let(b,10,add(b,b)),let(b,20,add(a,b)))";
    private static final String LET_DOUBLE_LAYER = "let(a,5,let(b,mult(a,10),add(b,a)))";
    private static final String LET_SIMPLE = "let(a,5,add(a,a))";
    private static final String OP_DOUBLE_WITH_LET = "add(2,let(a,3,add(a,a)))";
    private static final String OP_MULT_LAYER = "mult(add(2,2),div(9,3))";
    private static final String OP_DOUBLE_LAYER = "add(1,mult(2,3))";
    private static final String OP_SIMPLE = "add(1,2)";
    private static final String ONLY_INTEGER = "2.0";

    @Test
    public void testLetWithMultiLayer() {

        Double result = Calculator.doCalculate(LET_MULT_LAYER);
        assertThat(result).isEqualTo(40);
    }

    @Test
    public void testLetWithSingleLayer() {

        Double result = Calculator.doCalculate(LET_DOUBLE_LAYER);
        assertThat(result).isEqualTo(55);
    }

    @Test
    public void testSimpleLet() {

        Double result = Calculator.doCalculate(LET_SIMPLE);
        assertThat(result).isEqualTo(10);
    }

    @Test
    public void testOperatorWithDoubleLayerWithLet() {

        Double result = Calculator.doCalculate(OP_DOUBLE_WITH_LET);
        assertThat(result).isEqualTo(8);
    }

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
    public void testSimpleInteger(){
        Double result = Calculator.doCalculate(ONLY_INTEGER);
        assertThat(result).isEqualTo(NumberUtils.toDouble(ONLY_INTEGER));
    }
}
