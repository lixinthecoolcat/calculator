package com.coolcat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.StringWriter;

import static com.coolcat.TreeUtil.*;

public class AppUtil {
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
    public static String validateInput(String input) {
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

    public static String preprocess(String input) {

        //go from left to right, locate value exp and get the value, replace value name in exp by calculated
        //value, repeat, until no let and only normal operations
        String expr = input;
        while (input.contains(Operator.let.name())) {
            int letPos = input.indexOf(Operator.let.name());
            int letEnd = findCommaPosition(input.substring(letPos)) + letPos;
            int vNamePos = findCommaPosition(input.substring(letPos + 4));
            String vName = input.substring(letPos + 4, letPos + 4 + vNamePos);//"let("
            int subStrBeginAt = letPos + 4 + vName.length() + 1;
            int vValuePos = findCommaPosition(input.substring(subStrBeginAt));
            String vValue = input.substring(subStrBeginAt, subStrBeginAt + vValuePos);
            expr = input.substring(subStrBeginAt + vValuePos + 1, letEnd);
            if (vValue.contains(Operator.let.name())) {
                vValue = preprocess(vValue);
            }

            //Since we don't know what embedded in let exp, f.g. let(b,20,add(a,b)) as outer layer, the "a" within add(a,b) should be replaced by an evaluated
            //inner layer let to add(10,10). Therefore we don't want to replace letter a as part of "add", but do want to replace the a as part of (a,b)
            //Simple way is to define a syntax requirement for let, only use single letter as variable name. Otherwise "add" can be variable name which would be really
            //complex things. So here is an assumption: let first param must be single letter.
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
