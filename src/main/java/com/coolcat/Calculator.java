package com.coolcat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.coolcat.AppUtil.setLogLevel;
import static com.coolcat.AppUtil.validateInput;
import static java.lang.System.exit;

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
        String input;
        Level logLevel = null;

        //we can take an option, like -q to quit loop, but ctl-C will do the same.
        //I can make it exit for every run, but it is more user friendly to keep it running in loop. Easier to set log level and be used through the session.
        while (true) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Enter options or expressions: ");

                input = reader.readLine();
                input = input.trim();
                if (input.isEmpty()) {
                    System.out.println("Please enter expression or options.");
                    continue;
                }
                if (input.charAt(0) == '-') {
                    String[] params = input.split(" ");
                    if (params[0].length() != 2) {
                        throw new IllegalArgumentException("This system only support short options.");
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
                        case 'e':
                            if (params.length == 1) {
                                System.out.println("Please enter level value following -e ");
                                continue;
                            }
                            for (Level level : Level.values()) {
                                if (params[1].trim().equalsIgnoreCase(level.name())) {
                                    logLevel = level;
                                    setLogLevel(logLevel);
                                }
                            }
                            if (logLevel == null) {
                                System.out.print("Syntax error in -l option: it can only be INFO,ERROR,TRACE or DEBUG");
                                logLevel = Level.DEBUG;
                            }
                            break;
                        default:
                            throw new IllegalArgumentException("Option syntax error. use -h to read help info.");
                    }

                } else {
                    // here is the code path to do calculation
                    logger.info("Debugger lever is: " + logLevel);
                    logger.info("User inputs are: " + input);

                    //take input, sanity check. A simple check. Won't prevent thorough abuse, like add(1,i) will end up exceptions.
                    doCalculate(validateInput(input));
                }
            } catch (IllegalArgumentException e) {
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

    //Technically, output is written to console or file by logger. We don't need return value. It is for testing.
    public static Double doCalculate(String input) {
        while (input.contains(Operator.let.name())) {
            input = AppUtil.preprocess(input);
        }
        Node rootNode = TreeUtil.parseNode(input, null);
        calculate(rootNode);
        System.out.println("The calculated result is: " + rootNode.getData());
        return rootNode.getData();
    }

    /**
     * @param root root node of operator tree
     */
    private static void calculate(Node root) {
        // Post order recursive to do calculate
        if (root != null) {
            calculate(root.left);
            calculate(root.right);

            if (root.getOp() != Operator.nil) {
                //root.data is overwritten by each step, since we need final result, we don't need keep intermediate calculated data
                root.setData(root.getOp().operate(root));
            }
        }

    }
}
