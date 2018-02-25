package com.coolcat;

interface Operatable {
    default Double operate(Node node) {
        return node.getData();
    }
}
