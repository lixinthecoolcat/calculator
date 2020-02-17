package com.coolcat;

import java.util.function.DoubleBinaryOperator;

public enum Operator implements Operatable {
    add ((x,y)->x+y),
    sub ((x,y)->x-y),
    mult ((x,y)->x*y),
    div ((x,y)->x/y),
    nil((x,y)->0.0),
    let((x,y)->0.0);

    private DoubleBinaryOperator op;
    Operator(DoubleBinaryOperator operator){
        op = operator;
    }
    public Double operate(Double arg1, Double arg2){
        return op.applyAsDouble(arg1,arg2);
    }
}


