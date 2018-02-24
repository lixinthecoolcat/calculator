package com.coolcat;

public class TreeUtil {
    static final Character COMMA = ',';
    static final Character BRACKET_LEFT = '(';
    static final Character BRACKET_RIGHT = ')';
    private enum Direction {
        left,
        right
    }

    /**
     * @param input expression string
     * @param rootNode operator tree node
     * @return operator tree node
     */
    public static Node parseNode(String input, Node rootNode) {
        //have a non-null root first before recursive loop
        if (rootNode == null) {
            rootNode = new Node(findOperator(input));
            input = input.substring(rootNode.op.name().length() + 1, input.length() - 1);
        }
        if (!input.isEmpty()) {

            if (!input.contains(Character.toString(COMMA))) {
                // no comma means down to digit, set input empty to get out.
                input = "";
            }
            int position = AppUtil.findCommaPosition(input);
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
            str = str.substring(op.name().length() + 1, str.length() - 1);
        } else {// data node
            current = new Node(Integer.parseInt(str));
            str = "";
        }

        if (dir == Direction.left) {
            rootNode.left = current;
        } else {
            rootNode.right = current;
        }
        parseNode(str, current);
    }

    private static Operator findOperator(String input) {
        String cmd = input.substring(0, input.indexOf(Character.toString(BRACKET_LEFT)));
        for (Operator op : Operator.values()) {
            if (op.name().toLowerCase().equals(cmd.toLowerCase())) {
                return op;
            }
        }
        return Operator.nil;
    }

}
