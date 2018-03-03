package com.coolcat;


import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/*
This class tests method doCalculate in Calculator class. It requires checked valid input which
is the output of validateInput from AppUtil.
Be aware, when adding new test data, make sure there is no space within input string.
 */
public class CalculatorTest {
    private static final String LET_MULT_LAYER = "let(a,let(b,10,add(b,b)),let(b,20,add(a,b)))";
    private static final String LET_SIGNLE_LAYER = "let(a,5,let(b,mult(a,10),add(b,a)))";
    private static final String LET_SIMPLE = "let(a,5,add(a,a))";
    private static final String OP_MULT_LAYER = "mult(add(2,2),div(9,3))";
    private static final String OP_SINGLE_LAYER = "add(1,mult(2,3))";
    private static final String OP_SIMPLE = "add(1,2)";
    Random rand = new Random();
    List<String> opList = Arrays.stream(Operator.values()).filter(op -> !op.equals(Operator.nil) && !op.equals(Operator.let)).map(op -> op.name())
            .sorted().collect(Collectors.toList());


    @Test
    public void testLetWithMultiLayer() {

        Double result = Calculator.doCalculate(LET_MULT_LAYER);
        assertThat(result).isEqualTo(40);
    }

    @Test
    public void testLetWithSingleLayer() {

        Double result = Calculator.doCalculate(LET_SIGNLE_LAYER);
        assertThat(result).isEqualTo(55);
    }

    @Test
    public void testSimpleLet() {

        Double result = Calculator.doCalculate(LET_SIMPLE);
        assertThat(result).isEqualTo(10);
    }

    @Test
    public void testOperatorWithMultiLayer() {

        Double result = Calculator.doCalculate(OP_MULT_LAYER);
        assertThat(result).isEqualTo(12);
    }

    @Test
    public void testOperatorWithSingleLayer() {

        Double result = Calculator.doCalculate(OP_SINGLE_LAYER);
        assertThat(result).isEqualTo(7);
    }

    @Test
    public void testSimpleOperator() {

        Double result = Calculator.doCalculate(OP_SIMPLE);
        assertThat(result).isEqualTo(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEdgeDivByZero() {
        Calculator.doCalculate("div(3,0)");
    }

    @Test
    public void testEdgeAddZero() {
        String inputLeft = "0";
        String inputRight = "-2";

        Double result = Calculator.doCalculate(
                new StringJoiner(",", "add(", ")").add(inputLeft).add(inputRight).toString());
        assertThat(result).isEqualTo(NumberUtils.toDouble(inputLeft) + NumberUtils.toDouble(inputRight));
    }

    @Test
    public void testEdgeMultZero() {

    }

    @Test
    public void testEdgeNoOp() {
        String input = "-1.1";
        Double result = Calculator.doCalculate(input);
        assertThat(result).isEqualTo(NumberUtils.toDouble(input));
    }

    @Test
    public void testRandomCombinationWithLet() {
        String op = opList.get(rand.nextInt(4));

        Double left = new Double(rand.nextInt());
        Double right = new Double(rand.nextInt());

        String exp = buildRandomExpression(op, "a", Double.toString(right));

        Double outter = new Double(rand.nextInt());

        String letExp = new StringBuilder().append("let(a,").append(outter).append(",").append(exp).append(")").toString();

        System.out.println("Input Let Expression: " + letExp);

        Double result = Calculator.doCalculate("let(a," + outter + "," + exp + ")");

        checkResults(op, outter, right, result);

    }

    @Test
    public void testRandomCombinationNormalOp() {
        String op = opList.get(rand.nextInt(4));

        Double left = new Double(rand.nextInt());
        Double right = new Double(rand.nextInt());

        Double result = Calculator.doCalculate(buildRandomExpression(op, Double.toString(left), Double.toString(right)));

        checkResults(op, left, right, result);
    }

    private String buildRandomExpression(String op, String left, String right) {
        String exp = new StringJoiner(",", op + "(", ")").add(left)
                .add(right).toString();
        System.out.println("Input: " + exp);
        return exp;
    }

    private void checkResults(String op, Double left, Double right, Double result) {
        switch (op) {
            case "add":
                assertThat(result).isEqualTo(left + right);
                break;
            case "sub":
                assertThat(result).isEqualTo(left - right);
                break;
            case "mult":
                assertThat(result).isEqualTo(left * right);
                break;
            case "div":
                try {
                    assertThat(result).isEqualTo(left / right);
                } catch (IllegalArgumentException e) {
                    // random right exactly equal to zero, the odds is extremely low. we have test case to
                    //test divByzero, so we just ignore exception here in this case.
                }
                break;
        }
    }
}
