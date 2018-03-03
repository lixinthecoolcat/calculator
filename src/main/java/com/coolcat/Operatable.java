package com.coolcat;


interface Operatable {
    default Double operate(Double arg1, Double arg2){
        return 0.0;
    }
}
