package com.coolcat;



import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.Test;
/*
This class tests method doCalculate in Calculator class. It requires checked valid input which
is the output of validateInput from AppUtil.
Be aware, when adding new test data, make sure there is no space within input string.
 */
public class CalculatorTest {
    public static final String LET_MULT_LAYER = "let(a,let(b,10,add(b,b)),let(b,20,add(a,b)))";
    public static final String LET_SIGNLE_LAYER = "let(a,5,let(b,mult(a,10),add(b,a)))";
    public static final String LET_SIMPLE = "let(a,5,add(a,a))";
    public static final String OP_MULT_LAYER = "mult(add(2,2),div(9,3))";
    public static final String OP_SINGLE_LAYER = "add(1,mult(2,3))";
    public static final String OP_SIMPLE = "add(1,2)";

    @Test
    public void testLetWithMultiLayer(){

        Integer result = Calculator.doCalculate(LET_MULT_LAYER);
        assertThat(result).isEqualTo(40);
    }
    @Test
    public void testLetWithSingleLayer(){

        Integer result = Calculator.doCalculate(LET_SIGNLE_LAYER);
        assertThat(result).isEqualTo(55);
    }
    @Test
    public void testSimpleLet(){

        Integer result = Calculator.doCalculate(LET_SIMPLE);
        assertThat(result).isEqualTo(10);
    }
    @Test
    public void testOperatorWithMultiLayer(){

        Integer result = Calculator.doCalculate(OP_MULT_LAYER);
        assertThat(result).isEqualTo(12);
    }
    @Test
    public void testOperatorWithSingleLayer(){

        Integer result = Calculator.doCalculate(OP_SINGLE_LAYER);
        assertThat(result).isEqualTo(7);
    }
    @Test
    public void testSimpleOperator(){

        Integer result = Calculator.doCalculate(OP_SIMPLE);
        assertThat(result).isEqualTo(3);
    }

}
