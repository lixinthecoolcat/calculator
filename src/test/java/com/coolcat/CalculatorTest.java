package com.coolcat;



import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.Test;
/*
This class tests method doCalculate in Calculator class. It requires checked valid input which
is the output of validateInput from AppUtil.
Be aware, when adding new test data, make sure there is no space within input string.
 */
public class CalculatorTest {
    private static final String LET_MULT_LAYER = "let(a,let(b,10,add(b,b)),let(b,20,add(a,b)))";
    private static final String LET_SIGNLE_LAYER = "let(a,5,let(b,mult(a,10),add(b,a)))";
    private static final String LET_SIMPLE = "let(a,5,add(a,a))";
    private static final String OP_MULT_LAYER = "mult(add(2,2),div(9,3))";
    private static final String OP_SINGLE_LAYER = "add(1,mult(2,3))";
    private static final String OP_SIMPLE = "add(1,2)";

    @Test
    public void testLetWithMultiLayer(){

        Double result = Calculator.doCalculate(LET_MULT_LAYER);
        assertThat(result).isEqualTo(40);
    }
    @Test
    public void testLetWithSingleLayer(){

        Double result = Calculator.doCalculate(LET_SIGNLE_LAYER);
        assertThat(result).isEqualTo(55);
    }
    @Test
    public void testSimpleLet(){

        Double result = Calculator.doCalculate(LET_SIMPLE);
        assertThat(result).isEqualTo(10);
    }
    @Test
    public void testOperatorWithMultiLayer(){

        Double result = Calculator.doCalculate(OP_MULT_LAYER);
        assertThat(result).isEqualTo(12);
    }
    @Test
    public void testOperatorWithSingleLayer(){

        Double result = Calculator.doCalculate(OP_SINGLE_LAYER);
        assertThat(result).isEqualTo(7);
    }
    @Test
    public void testSimpleOperator(){

        Double result = Calculator.doCalculate(OP_SIMPLE);
        assertThat(result).isEqualTo(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivByZero(){
        Calculator.doCalculate("div(3,0)");
    }
}
