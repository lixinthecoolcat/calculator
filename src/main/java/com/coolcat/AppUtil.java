package com.coolcat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.StringWriter;

/**
 * Utility class for app
 *
 * @author cool cat
 */

class AppUtil {
    static final Character COMMA = ',';
    static final Character BRACKET_LEFT = '(';
    static final Character BRACKET_RIGHT = ')';

    /**
     * When a expr stripped operator and out layer of brackets, the first comma
     * after balanced bracket pairs is the one separating this particular expr's arguments
     *
     * @param input input string
     * @return find pos for outer layer argument delimiter
     */
    public static int findCommaPosition(String input) {
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
            if ((i == input.length() - 1 || input.charAt(i) == COMMA) && layer == 0) {
                position = i;
                break;
            }
        }
        return position;
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
     * @param input input String
     * @return boolean
     */
    static String validateInput(String input) {
        int layer = 0;
        boolean balanced = false;
        boolean hasInvalidCh;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == BRACKET_LEFT) {
                layer++;

            }
            if (input.charAt(i) == BRACKET_RIGHT) {
                layer--;
            }
        }
        if (layer == 0) {
            balanced = true;
        }
        if (!balanced) {
            throw new IllegalArgumentException("Syntax error: missing brackets");
        }
        // In the cases when there are space inside expr, trim won't help. Using writer to remove space so we can compare later
        StringWriter stringWriterOriginal = new StringWriter();
        input.chars().mapToObj(i -> (char) i).filter(a -> !Character.isSpaceChar(a)).forEach(stringWriterOriginal::write);

        StringWriter stringWriterAfterFilter = new StringWriter();
        input.chars().mapToObj(i -> (char) i).filter(a -> Character.isAlphabetic(a) || Character.isDigit(a) ||
                (a == BRACKET_LEFT) || (a == BRACKET_RIGHT) || (a == COMMA)).forEach(stringWriterAfterFilter::write);


        hasInvalidCh = !stringWriterOriginal.toString().equals(stringWriterAfterFilter.toString());
        if (hasInvalidCh) {
            throw new IllegalArgumentException("Syntax error: contain unsupported characters");
        }
        return stringWriterOriginal.toString();

    }

    /**
     * Translate 'let' operator into normal operator. remove all the let phrases in expr
     *
     * @param input expression string with let
     * @return expression string without let
     */
    static String preProcess(String input) {
        int letLayerLength = "let(".length();
        //go from left to right, locate value exp and get the value, replace value name in exp by calculated
        //value, repeat, until no let and only normal operations
        String expr = input;
        while (input.contains(Operator.let.name())) {
            int letPos = input.indexOf(Operator.let.name());
            int letEnd = findCommaPosition(input.substring(letPos)) + letPos;
            int vNamePos = findCommaPosition(input.substring(letPos + letLayerLength));
            String vName = input.substring(letPos + letLayerLength, letPos + letLayerLength + vNamePos);//"let("
            int subStrBeginAt = letPos + letLayerLength + vName.length() + 1;
            int vValuePos = findCommaPosition(input.substring(subStrBeginAt));
            String vValue = input.substring(subStrBeginAt, subStrBeginAt + vValuePos);
            expr = input.substring(subStrBeginAt + vValuePos + 1, letEnd);
            if (vValue.contains(Operator.let.name())) {
                vValue = preProcess(vValue);
            }

            /*Since we don't know what embedded in let exp, f.g. let(a,10,add(a,a)), here is a bit tricky part.
            because we don't want to replace letter 'a' as part of "add", but do want to replace the 'a' as part of (a,a)
            I am sure there will be a way to solve it. But it will add code complicity without any gain.
            After all, the value name in let expr is just a symbol.It make no improvement for functional or user experience
            to use any length of string to be the value name.Simple way is to define a syntax requirement for let,
            only use single letter as variable name.
            So here is an assumption(#2): let first param must be single letter.
            */
            if (vName.length() > 1) {
                throw new IllegalArgumentException("let expr's value name should be single letter. ");
            }

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
                //looking for left argument
                if (tmp[i] == vName.charAt(0) && tmp[i + 1] == BRACKET_RIGHT && tmp[i - 1] == COMMA) {
                    replaceBegin = i;
                    break;
                }
            }

            if (replaceBegin != 0) {
                expr = expr.substring(0, replaceBegin) + vValue + expr.substring(replaceBegin + 1);
            }
            return expr;
        }
        return expr;
    }
}
