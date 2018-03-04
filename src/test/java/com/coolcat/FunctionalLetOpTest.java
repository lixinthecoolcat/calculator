package com.coolcat;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FunctionalLetOpTest {
    private static final String LET_MULT_LAYER_LET_AS_VALUE = "let(a,let(b,10,add(b,b)),let(b,20,add(a,b)))";
    private static final String LET_MULT_LAYER_LET_AS_VALUE_WITH_LET = "let(a,let(b,let(c,10,mult(c,2)),add(b,b)),let(b,20,add(a,b)))";
    private static final String LET_DOUBLE_LAYER = "let(a,5,let(b,mult(a,10),add(b,a)))";
    private static final String LET_SIMPLE = "let(a,5,add(a,a))";
    private static final String OP_DOUBLE_WITH_LET_AS_LEFT = "add(let(a,3,add(a,a)),2)";
    private static final String OP_DOUBLE_WITH_LET_AS_RIGHT = "add(2,let(a,3,add(a,a)))";
    private static final String OP_DOUBLE_WITH_LET_AS_BOTH = "div(let(a,3,add(a,a)),let(b,2,sub(b,3)))";


    @Test
    public void testLetWithMultiLayerLetAsValue() {
        Double result = Calculator.doCalculate(LET_MULT_LAYER_LET_AS_VALUE);
        assertThat(result).isEqualTo(40);
    }

    @Test
    public void testLetWithMultiLayerLetAsValueWithLetAreWeCrazyYet() {
        Double result = Calculator.doCalculate(LET_MULT_LAYER_LET_AS_VALUE_WITH_LET);
        assertThat(result).isEqualTo(60);
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
    public void testOperatorWithDoubleLayerWithLetAsRight() {

        Double result = Calculator.doCalculate(OP_DOUBLE_WITH_LET_AS_RIGHT);
        assertThat(result).isEqualTo(8);
    }

    @Test
    public void testOperatorWithDoubleLayerWithLetAsLeft() {

        Double result = Calculator.doCalculate(OP_DOUBLE_WITH_LET_AS_LEFT);
        assertThat(result).isEqualTo(8);
    }

    @Test
    public void testOperatorWithDoubleLayerWithLetASBoth() {

        Double result = Calculator.doCalculate(OP_DOUBLE_WITH_LET_AS_BOTH);
        assertThat(result).isEqualTo(-6);
    }

}
