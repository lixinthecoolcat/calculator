package com.coolcat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        FunctionalLetOpTest.class,
        FunctionalEdgeCaseTest.class,
        FunctionalRandomTest.class,
        FunctionalNormalOpTest.class,
        SyntaxTest.class
})

public class CalculatorTestSuite {
}
