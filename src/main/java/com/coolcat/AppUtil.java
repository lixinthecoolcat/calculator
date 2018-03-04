package com.coolcat;

import javafx.util.Pair;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.StringWriter;
import java.util.Arrays;

/**
 * Utility class for app
 *
 * @author cool cat
 */

class AppUtil {
    static final Character COMMA = ',';
    static final Character BRACKET_LEFT = '(';
    static final Character BRACKET_RIGHT = ')';
    static final String UNSUPPORTED = "Syntax error: contain unsupported characters.";
    static final String UNBALANCED = "Syntax error: missing brackets.";
    static final String INVALID_LET_VALUE_NAME = "Syntax error: value name in a LET expression should be a single letter.";
    static final String INVALID_LET_EXPR = "Syntax error: LET expression requires 3 arguments.";
    static final String INVALID_INPUT = "Syntax error: invalid input.";
    static final String INVALID_LEAF_NODE = " Syntax error: expression has invalid value. It should be an Integer value.";
    static final String INVALID_OP = "Syntax error: unrecognized operator:";
    static final String INVALID_ARGUMENT = "Syntax error: there should be two arguments following math operators, separated by comma.";
    static final String MISSING_ARGUMENTS = "Syntax error: Can't find any arguments for operator ";

    static final String OP_LET = Operator.let.name();
    static final int LET_LAYER_LENGTH = Operator.let.name().length() + 1; // "let("

    /**
     * When a expr stripped operator and out layer of brackets, the first comma
     * after balanced bracket pairs is the one separating this particular expr's arguments
     *
     * @param input input string
     * @return find pos for outer layer argument delimiter
     */
    //Usually we aim one-method-do-one-thing, but here layer and position are tightly intertwined.
    //breaking them into two, will likely bring in duplicated code and hard-to-get logic since they are so low level.
    //therefore we use one method to get two return values as a Pair, key is the position, value is an indication of whether
    //input is bracket balanced.
    public static Pair<Integer, Integer> findCommaPosition(String input) {
        // layer of expressions
        int layer = 0;
        // the comma position for outermost layer argument
        int position = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == BRACKET_LEFT) {
                layer++;

            }
            if (input.charAt(i) == BRACKET_RIGHT) {
                layer--;
            }
            if (input.charAt(i) == COMMA && layer == 0) {
                position = i;
                break;
            }
            //or it is the last sub-exp, find comma will return the length of the input.
            if (i == input.length() - 1) {
                position = i;
            }
        }
        return new Pair<>(position, layer);
    }

    /**
     * Allow change level at runtime
     *
     * @param level logging level
     */
    static void setLogLevel(Level level) {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        org.apache.logging.log4j.core.config.Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(level);
        ctx.updateLoggers();
    }

    /**
     * A simple syntax check for input before process it.
     *
     * @param input input String regulated ( no space in it)
     * @return boolean
     */
    static String syntaxCheck(String input) {
        //input should be either a) Numbers b) Expressions which should have a valid operator in the input.
        if (!NumberUtils.isCreatable(input) && !containExpression(input)) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
        //ok, pass first guard, if you are an expr, do you have any brackets?
        if (containExpression(input) && (!input.contains(Character.toString(BRACKET_LEFT)) || !input.contains(Character.toString(COMMA)))) {
            throw new IllegalArgumentException(MISSING_ARGUMENTS);
        }
        //check whether brackets are in pairs
        if (findCommaPosition(input).getValue() != 0) {
            throw new IllegalArgumentException(UNBALANCED);
        }
        //check whether there are any char not in the supported list ( 0-9,a-z,A-Z,'(',')','-',',')
        StringWriter stringWriterAfterFilter = new StringWriter();
        input.chars().mapToObj(i -> (char) i).filter(a -> Character.isAlphabetic(a) || Character.isDigit(a) ||
                (a == BRACKET_LEFT) || (a == BRACKET_RIGHT) || (a == COMMA) || (a == '-')).forEach(stringWriterAfterFilter::write);

        if (!input.equals(stringWriterAfterFilter.toString())) {
            throw new IllegalArgumentException(UNSUPPORTED);
        }
        return input;

    }

    /**
     * Translate 'let' operator into normal operator. remove all the let phrases in expr
     *
     * @param input expression string with let
     * @return expression string without let
     */
    static String preProcess(String input) {
        //go from left to right, locate value exp and get the value, replace value name in exp by calculated value
        int letPos = input.indexOf(OP_LET);
        int letEnd = findCommaPosition(input.substring(letPos)).getKey() + letPos;

        int vNamePos = findCommaPosition(input.substring(letPos + LET_LAYER_LENGTH)).getKey();
        String vName = input.substring(letPos + LET_LAYER_LENGTH, letPos + LET_LAYER_LENGTH + vNamePos);

        if (NumberUtils.isCreatable(vName)) {
            throw new IllegalArgumentException(INVALID_LET_VALUE_NAME);
        }
        /*Since we don't know what embedded in let exp, f.g. let(a,10,add(a,a)), here is a bit tricky part.
            because we don't want to replace letter 'a' as part of "add", but do want to replace the 'a' as part of (a,a)
            I am sure there will be a way to solve it. But it will add code complicity without any gain.
            After all, the value name in let expr is just a symbol.It makes no improvement for functional nor user experience
            to use any length of string to be the value name.Simple way is to define a syntax requirement for let,
            only use single letter as variable name.
            So here is an assumption(#2): let first param must be single letter.
        */
        if (vName.length() > 1) {
            throw new IllegalArgumentException(INVALID_LET_VALUE_NAME);
        }

        int subStrBeginAt = letPos + LET_LAYER_LENGTH + vName.length() + 1;

        int vValuePos = findCommaPosition(input.substring(subStrBeginAt)).getKey();
        String vValue = input.substring(subStrBeginAt, subStrBeginAt + vValuePos);

        if (letEnd < subStrBeginAt + vValuePos + 1) {
            throw new IllegalArgumentException(INVALID_LET_EXPR);
        }

        if (input.charAt(letEnd) == COMMA) {
            letEnd = letEnd - 1;
        }

        String expr = input.substring(subStrBeginAt + vValuePos + 1, input.charAt(letEnd) == COMMA ? letEnd - 1 : letEnd);

        if (vValue.contains(OP_LET)) {
            vValue = preProcess(vValue);
        }

        //replacing
        char[] tmp = expr.toCharArray();
        int replaceBegin = 0;
        for (int i = 1; i < tmp.length - 1; i++) {
            //looking for left argument
            if (tmp[i] == vName.charAt(0) && tmp[i - 1] == BRACKET_LEFT && tmp[i + 1] == COMMA) {
                replaceBegin = i;
                break;
            }
        }

        if (replaceBegin != 0) {
            expr = expr.substring(0, replaceBegin) + vValue + expr.substring(replaceBegin + 1);
        }
        //then need check on right argument as well
        replaceBegin = 0;
        tmp = expr.toCharArray();
        for (int i = 1; i < tmp.length - 1; i++) {
            //looking for right argument
            if (tmp[i] == vName.charAt(0) && tmp[i + 1] == BRACKET_RIGHT && tmp[i - 1] == COMMA) {
                replaceBegin = i;
                break;
            }
        }

        if (replaceBegin != 0) {
            expr = expr.substring(0, replaceBegin) + vValue + expr.substring(replaceBegin + 1);
        }
        //before going out, put whole string together. let can be in the middle, so we still need the first part and last part.
        return input.substring(0, letPos) + expr + input.substring(letEnd + 1);
    }

    private static boolean containExpression(String input) {
        return Arrays.stream(Operator.values()).map(Operator::name).anyMatch(s -> input.contains(s));
    }
}
