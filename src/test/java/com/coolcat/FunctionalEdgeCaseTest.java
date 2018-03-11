package com.coolcat;

import org.junit.Test;

import java.util.StringJoiner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FunctionalEdgeCaseTest {
    @Test
    public void testEdgeDivByZero() {
        Double result = Calculator.doCalculate("div(3,0)");
        assertThat(result).isEqualTo(Double.POSITIVE_INFINITY);

        result = Calculator.doCalculate("div(-3,0)");
        assertThat(result).isEqualTo(Double.NEGATIVE_INFINITY);

    }

    @Test
    public void testEdgeAddZero() {
        String inputLeft = "0";
        String inputRight = "-2";

        Double result = Calculator.doCalculate(buildInput(Operator.add, inputLeft, inputRight));

        assertThat(result).isEqualTo(-2);
    }

    @Test
    public void testEdgeSubZero() {
        String arg1 = "0";
        String arg2 = "-2";
        Operator op = Operator.sub;

        Double result = Calculator.doCalculate(buildInput(op, arg1, arg2));

        assertThat(result).isEqualTo(2);
        result = Calculator.doCalculate(buildInput(op, arg2, arg1));

        assertThat(result).isEqualTo(-2);
    }

    @Test
    public void testEdgeMultZero() {
        String inputLeft = "0";
        String inputRight = "2";

        Double result = Calculator.doCalculate(buildInput(Operator.mult, inputLeft, inputRight));

        assertThat(result).isEqualTo(0.0);

    }

    @Test
    public void testEdgeMultZeroWithSign() {
        String inputLeft = "0";
        String inputRight = "-2";

        Double result = Calculator.doCalculate(buildInput(Operator.mult, inputLeft, inputRight));

        assertThat(result).isEqualTo(-0.0);

    }

    @Test
    public void testEdgeMultDoubleSign() {
        String inputLeft = "-2";
        String inputRight = "-5";

        Double result = Calculator.doCalculate(buildInput(Operator.mult, inputLeft, inputRight));

        assertThat(result).isEqualTo(10.0);

    }

    @Test
    public void testEdgeMAX() {
        String inputLeft = Integer.toString(Integer.MAX_VALUE);
        String inputRight = Integer.toString(Integer.MAX_VALUE);
        Double result = Calculator.doCalculate(buildInput(Operator.mult, inputLeft, inputRight));
        //we ues double as node's data type. so we can handle input type (Integer)'s max/min values.
        assertThat(result).isEqualTo((double) Integer.MAX_VALUE * (double) Integer.MAX_VALUE);
    }

    @Test
    public void testEdgeMIN() {
        String inputLeft = Integer.toString(Integer.MIN_VALUE);
        String inputRight = Integer.toString(Integer.MIN_VALUE);
        Double result = Calculator.doCalculate(buildInput(Operator.add, inputLeft, inputRight));
        assertThat(result).isEqualTo((double) Integer.MIN_VALUE + (double) Integer.MIN_VALUE);
    }

    private String buildInput(Operator op, String left, String right) {
        String prefix = op.name() + "(";
        return new StringJoiner(",", prefix, ")").add(left).add(right).toString();
    }
}
