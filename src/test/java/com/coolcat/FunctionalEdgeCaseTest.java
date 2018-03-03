package com.coolcat;

import org.apache.commons.lang3.math.NumberUtils;
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

        Double result = Calculator.doCalculate(
                new StringJoiner(",", "add(", ")").add(inputLeft).add(inputRight).toString());
        assertThat(result).isEqualTo(-2);
    }

    @Test
    public void testEdgeMultZero() {
        String inputLeft = "0";
        String inputRight = "-2";

        Double result = Calculator.doCalculate(
                new StringJoiner(",", "mult(", ")").add(inputLeft).add(inputRight).toString());
        assertThat(result).isEqualTo(-0.0);

    }

    @Test
    public void testEdgeNoOp() {
        String input = "-2";
        Double result = Calculator.doCalculate(input);
        assertThat(result).isEqualTo(NumberUtils.toDouble(input));
    }
}
