package com.coolcat;


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
    private int availableOps = 4; //add,sub,mult,div=> these 4 we can generate randomly. let needs a bit extra work but covered below as well.
    private String letTestValueName = "a";

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
        String op = opList.get(rand.nextInt(availableOps));

        int right = rand.nextInt();

        String exp = buildRandomExpression(op, letTestValueName, Integer.toString(right));

        int outer = rand.nextInt();

        String letExp = new StringBuilder().append("let(").append(letTestValueName).append(",").append(outer).append(",").append(exp).append(")").toString();

        System.out.println("Random test input Let Expression: " + letExp);

        Double result = Calculator.doCalculate(letExp);

        checkResults(op, outer, right, result);

    }

    @Test
    public void testRandomCombinationNormalOp() {
        String op = opList.get(rand.nextInt(availableOps));

        int left = rand.nextInt();
        int right = rand.nextInt();

        Double result = Calculator.doCalculate(buildRandomExpression(op, Integer.toString(left), Integer.toString(right)));

        checkResults(op, left, right, result);
    }

    private String buildRandomExpression(String op, String left, String right) {
        String exp = new StringJoiner(",", op + "(", ")").add(left)
                .add(right).toString();
        System.out.println("Random test input: " + exp);
        return exp;
    }

    private Double checkResults(String op, double left, double right, Double result) {
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
        return rand.nextInt();
    }

    private String getRandomOp() {
        return opList.get(rand.nextInt(availableOps));
    }
}
