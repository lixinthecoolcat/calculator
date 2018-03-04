package com.coolcat;

public enum Operator implements Operatable {
    add {
        @Override
        public Double operate(Double arg1, Double arg2) {
            return arg1 + arg2;
        }

    }, sub {
        @Override
        public Double operate(Double arg1, Double arg2) {
            return arg1 - arg2;
        }

    }, mult {
        @Override
        public Double operate(Double arg1, Double arg2) {
            return arg1 * arg2;
        }

    }, div {
        @Override
        public Double operate(Double arg1, Double arg2) {
            return arg1 / arg2;
        }
    }, nil, let

}


