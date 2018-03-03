package com.coolcat;


import org.apache.commons.lang3.math.NumberUtils;
import org.junit.BeforeClass;
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
    private static final String LET_DOUBLE_LAYER = "let(a,5,let(b,mult(a,10),add(b,a)))";
    private static final String LET_SIMPLE = "let(a,5,add(a,a))";
    private static final String OP_DOUBLE_WITH_LET = "add(2,let(a,3,add(a,a)))";
    private static final String OP_MULT_LAYER = "mult(add(2,2),div(9,3))";
    private static final String OP_DOUBLE_LAYER = "add(1,mult(2,3))";
    private static final String OP_SIMPLE = "add(1,2)";
    private static Random rand = new Random();
    private static List<String> opList;

    @BeforeClass
    public static void setup() {
        rand = new Random();
        opList = Arrays.stream(Operator.values()).filter(op -> !op.equals(Operator.nil) && !op.equals(Operator.let)).map(op -> op.name())
                .sorted().collect(Collectors.toList());
    }

    // test big picture
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

    // test edge conditions
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

    //last not least, random bombardment.
    @Test
    public void testRandomNormalDoubleLayer() {
        String op = getRandomOp();
        Integer left = getRandomInteger();
        Integer right = getRandomInteger();
        String leftEx = buildRandomExpression(op, Integer.toString(left), Integer.toString(right));
        Double resultLeftLayer = Calculator.doCalculate(leftEx);
        Double expectedLeftLayer = checkResults(op, left.doubleValue(), right.doubleValue(), resultLeftLayer);

        op = getRandomOp();
        left = getRandomInteger();
        right = getRandomInteger();
        String rightEx = buildRandomExpression(op, Integer.toString(left), Integer.toString(right));
        Double resultRightLayer = Calculator.doCalculate(rightEx);
        Double expectedRightLayer = checkResults(op, left.doubleValue(), right.doubleValue(), resultRightLayer);

        op = getRandomOp();
        Double result = Calculator.doCalculate(buildRandomExpression(op, leftEx, rightEx));
        checkResults(op, expectedLeftLayer, expectedRightLayer, result);
    }

    @Test
    public void testRandomCombinationWithLet() {
        String op = opList.get(rand.nextInt(4));

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

    private Double checkResults(String op, Double left, Double right, Double result) {
        Double expected = 0.0;
        switch (op) {
            case "add":
                expected = left + right;
                break;
            case "sub":
                expected = left - right;
                break;
            case "mult":
                expected = left * right;
                break;
            case "div":
                expected = left / right;
                break;
        }
        //checkResults can be called within sub-expr, therefore input can be double. With Double, comes concern of equality
        //so far this isEqualTo holds up well with double.
        assertThat(result).isEqualTo(expected);
        return expected;
    }

    private Integer getRandomInteger() {
        return new Integer(rand.nextInt());
    }

    private String getRandomOp() {
        return opList.get(rand.nextInt(4));
    }
}
