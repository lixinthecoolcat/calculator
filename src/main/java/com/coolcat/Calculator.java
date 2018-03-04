package com.coolcat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.coolcat.AppUtil.*;
import static com.coolcat.TreeUtil.parseNode;
import static java.lang.System.exit;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * A simple calculator. Use -h to see usage info.
 *
 * @author cool cat
 */
public class Calculator {
    private static final Logger logger = LogManager.getLogger(Calculator.class);

   /* parse input string, break down to sub operations, build a tree of it. Root is the outermost operator
                    for example: "add(add(add(1,2),2),add(3,4))"

                                |Add|
                                /   \
                             |Add|  |Add|
                             /  \    /  \
                          |Add|  2   3   4
                         /    \
                        1      2
            When tree is ready, we can traverse in post order to do calculate
   */


    public static void main(String[] args) {
        Level logLevel = null;

        //we can take an option, like -q to quit loop, but ctl-C will do the same.
        //I can make it exit for every run, but it is more user friendly to keep it running in loop. Easier to set log level and be used through the session.
        while (true) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Enter options or expressions: ");

                String input = reader.readLine();
                input = input.trim();

                if (input.isEmpty()) {
                    System.out.println("Please enter options or expression.");
                    continue;
                }
                if (NumberUtils.isCreatable(input)) {
                    doCalculate(input);
                    continue;
                }
                if (input.charAt(0) == '-') {
                    String[] params = input.split(SPACE);
                    if (params[0].length() != 2) {
                        throw new IllegalArgumentException("This system only supports short options.type -h for help info.");
                    }
                    char option = params[0].charAt(1);
                    switch (option) {
                        case 'h':
                            System.out.print(new StringBuilder().append("* ").append("Numbers: integers between Integer.MIN_VALUE and Integer.MAX_VALUE\n")
                                    .append("* ").append("Variables: strings of characters, where each character is one of a-z, A-Z\n")
                                    .append("* ").append("Arithmetic functions: add, sub, mult, div, each taking two arbitrary expressions as arguments\n")
                                    .append("* ").append("A let operator for assigning values to variables:\n" +
                                            "let(<variable name>, <value expression>, <expression where variable is used>)\n")
                                    .append("\n")
                                    .append("###   -h for help   -e set log level.\n\n").toString());
                            break;
                        case 'e': //not a good name I admit. Don't want to use 'l' since it is easy to confused with digit 1. can't come out a better name :)
                            if (params.length == 1) {
                                System.out.println("Please enter level value following -e ");
                                continue;
                            }

                            logLevel = Arrays.stream(Level.values()).filter(o -> o.name().equalsIgnoreCase(params[1]))
                                    .findFirst().orElseThrow(() -> new IllegalArgumentException("Syntax error in -e option: it can only be INFO,ERROR,TRACE or DEBUG"));

                            setLogLevel(logLevel);
                            System.out.println("Set logging level to : " + logLevel.name());

                            break;
                        default:
                            throw new IllegalArgumentException("Option syntax error. type -h for help info.");
                    }

                } else {
                    // here is the code path to do calculation
                    if(logLevel == null) {
                        System.out.println("Set default logging level : DEBUG");
                        logLevel = Level.DEBUG;
                    }
                    logger.info("Logging lever is: " + logLevel);
                    logger.info("User inputs are: " + input);
                    //regulate input ( trim, strip, case-insensitive) before parse. must have.
                    input = StringUtils.deleteWhitespace(input).toLowerCase();

                    doCalculate(syntaxCheck(input));
                }
            } catch (IllegalArgumentException e) {
                // syntax or other minor error do not exit, give user a chance to try again with correct typing.
                logger.error(e.getMessage());

            } catch (IOException e) {
                logger.error(e.getCause());
                exit(1);
            }
        }
    }

    /**
     * Calculator main logic work flow: translate let, build tree, traverse tree to do calculate, return result.
     *
     * @param input expression string
     * @return calculation result
     */
    //Ideally, we should use BigDecimal for best accuracy for decimal values generated by division operator.
    //Using Double is a simplify and knowing design choice with assumption (#1)of the users for this tool are not bankers
    public static Double doCalculate(String input) {
        Node rootNode;
        while (input.contains(OP_LET)) {
            input = preProcess(input);
        }

        if (NumberUtils.isCreatable(input)) {
            rootNode = parseNode(Integer.valueOf(input));
        } else {
            rootNode = parseNode(input, null);
        }

        calculate(rootNode);

        System.out.println("The calculated result is: " + rootNode.getData());

        //Technically, output is written to console or file by logger. We don't need return value. It is for being testing friendly.
        return rootNode.getData();
    }

    /**
     * @param root root node of operator tree
     */

    // Post order recursive to do calculate
    private static void calculate(Node root) {
        //root with nil op, is a leaf, no need to go further
        if (root.getOp() != Operator.nil) {
            calculate(root.left);
            calculate(root.right);

            //root.data is overwritten by each step, since we need final result, we don't need keep intermediate calculated data
            Double result = root.calculate();
            if (result.equals(Double.NEGATIVE_INFINITY) || result.equals(Double.POSITIVE_INFINITY)) {
                logger.warn("Divided by zero detected.");
            }
            root.setData(result);
        }

    }
}
