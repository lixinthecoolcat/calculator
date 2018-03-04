package com.coolcat;

/**
 * @author cool cat
 */
class Node {
    Node(Operator op) {
        this.data = 0.0;
        this.op = op;
    }

    Node(Double data) {
        this.data = data;
        this.op = Operator.nil;
    }

    final private Operator op;

    private Double data;

    Node left;

    Node right;

    public Operator getOp() {
        return op;
    }

    public void setData(Double data) {
        this.data = data;
    }

    public Double getData() {
        return data;
    }


    public Double calculate() {
        return op.operate(left.getData(), right.getData());
    }
}