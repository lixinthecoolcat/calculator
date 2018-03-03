package com.coolcat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        FunctionalBigPictureTest.class,
        FunctionalEdgeCaseTest.class,
        FunctionalRandomTest.class,
        SyntaxTest.class
})

public class CalculatorTestSuite {
}
