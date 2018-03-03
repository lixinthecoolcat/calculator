package com.coolcat;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;

import static com.coolcat.AppUtil.*;

/**
 * Utility class for operator/calculation tree
 *
 * @author cool cat
 */
class TreeUtil {
    public static final String INVALID_LEAF = " Syntax error: expression has invalid value. It should be an Integer value.";
    public static final String INVALID_OP = "Syntax error: unrecognized operator:";
    public static final String INVALID_ARGUMENT ="Syntax error: arguments in operator methods requires to be two, separated by comma.";
    private enum Direction {
        left,
        right
    }

    /**
     * @param input    expression string
     * @param rootNode operator tree node
     * @return operator tree node
     */
    static Node parseNode(String input, Node rootNode) {
        //have a non-null root first before recursive loop
        if (rootNode == null) {
            //if input is a digit: it is a leaf. return directly
            if(NumberUtils.isCreatable(input)){
                return new Node(NumberUtils.toDouble(input));
            }
            rootNode = new Node(findOperator(input));
            input = stripLayer(input, rootNode.getOp());
        }
        if (!input.isEmpty()) {

            if (!input.contains(Character.toString(COMMA))) {
                //after stripping a layer of op and brackets, we are expecting expr|digit,expr|digit format here
                //no comma means only one argument in this outermost layer of expr
                throw new IllegalArgumentException(INVALID_ARGUMENT);
            }
            int position = findCommaPosition(input).getKey();
            String left = input.substring(0, position);
            String right = input.substring(position + 1);

            parseBranch(left, rootNode, Direction.left);
            parseBranch(right, rootNode, Direction.right);
        }

        return rootNode;
    }

    private static void parseBranch(String str, Node rootNode, Direction dir) {
        Node current;
        // if contains comma, means it is still an expression
        if (str.contains(Character.toString(COMMA))) {
            Operator op = findOperator(str);
            current = new Node(op);
            str = stripLayer(str, op);
        } else {// data node
            if(!NumberUtils.isCreatable(str)){
                throw new IllegalArgumentException(str + " in " + rootNode.getOp()+INVALID_LEAF);
            }
            current = new Node(Double.parseDouble(str));
            str = "";
        }

        if (dir == Direction.left) {
            rootNode.left = current;
        } else {
            rootNode.right = current;
        }
        parseNode(str, current);
    }

    private static String stripLayer(String input, Operator op) {
        //at least following op, you need a "(", then you will see syntax error upon that missing bracket.
        if(input.length() <= op.name().length()){
            throw new IllegalArgumentException(INVALID_INPUT);
        }
        if(findCommaPosition(input).getValue()!=0){
            throw new IllegalArgumentException(UNBALANCED);
        }
        return input.substring(op.name().length() + 1, findCommaPosition(input).getKey());
    }

    private static Operator findOperator(String input) {
        String cmd;
        if(input.contains(Character.toString(BRACKET_LEFT))) {
            cmd = input.substring(0, input.indexOf(Character.toString(BRACKET_LEFT)));
        }else {
            cmd = input;
        }
        return Arrays.stream(Operator.values()).filter(o -> o.name().equalsIgnoreCase(cmd))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(INVALID_OP+cmd));
    }

}
