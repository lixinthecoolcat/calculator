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

//last not least, random bombardment.

public class FunctionalRandomTest {


    private static Random rand = new Random();
    private static List<String> opList;

    @BeforeClass
    public static void setup() {
        rand = new Random();
        opList = Arrays.stream(Operator.values()).filter(op -> !op.equals(Operator.nil) && !op.equals(Operator.let)).map(Operator::name)
                .sorted().collect(Collectors.toList());
    }
    @Test
    public void testRandomNormalDoubleLayer() {
        String op = getRandomOp();
        Integer left = getRandomInteger();
        Integer right = getRandomInteger();
        String leftEx = buildRandomExpression(op, Integer.toString(left), Integer.toString(right));
        Double resultLeftLayer = Calculator.doCalculate(leftEx);
        Double expectedLeftLayer = checkResults(op, left, right, resultLeftLayer);

        op = getRandomOp();
        left = getRandomInteger();
        right = getRandomInteger();
        String rightEx = buildRandomExpression(op, Integer.toString(left), Integer.toString(right));
        Double resultRightLayer = Calculator.doCalculate(rightEx);
        Double expectedRightLayer = checkResults(op, left, right, resultRightLayer);

        op = getRandomOp();
        Double result = Calculator.doCalculate(buildRandomExpression(op, leftEx, rightEx));
        checkResults(op, expectedLeftLayer, expectedRightLayer, result);
    }

    @Test
    public void testRandomCombinationWithLet() {
        String op = opList.get(rand.nextInt(4));

        int right = rand.nextInt();

        String exp = buildRandomExpression(op, "a", Double.toString(right));

        int outer = rand.nextInt();

        String letExp = new StringBuilder().append("let(a,").append(outer).append(",").append(exp).append(")").toString();

        System.out.println("Input Let Expression: " + letExp);

        Double result = Calculator.doCalculate("let(a," + outer + "," + exp + ")");

        checkResults(op, outer, right, result);

    }

    @Test
    public void testRandomCombinationNormalOp() {
        String op = opList.get(rand.nextInt(4));

        int left = rand.nextInt();
        int right = rand.nextInt();

        Double result = Calculator.doCalculate(buildRandomExpression(op, Double.toString(left), Double.toString(right)));

        checkResults(op, left, right, result);
    }

    private String buildRandomExpression(String op, String left, String right) {
        String exp = new StringJoiner(",", op + "(", ")").add(left)
                .add(right).toString();
        System.out.println("Input: " + exp);
        return exp;
    }

    private Double checkResults(String op, double left, double right, Double result) {
        Double expected = 0.0;
        switch (op) {
            case "add":
                expected = (double)left + right;
                break;
            case "sub":
                expected = (double)left - right;
                break;
            case "mult":
                expected = (double)left * right;
                break;
            case "div":
                expected = (double)left / right;
                break;
        }
        //checkResults can be called within sub-expr, therefore input can be double. With Double, comes concern of equality
        //so far this isEqualTo holds up well with double.
        assertThat(result).isEqualTo(expected);
        return expected;
    }

    private Integer getRandomInteger() {
        return rand.nextInt();
    }

    private String getRandomOp() {
        return opList.get(rand.nextInt(4));
    }
}