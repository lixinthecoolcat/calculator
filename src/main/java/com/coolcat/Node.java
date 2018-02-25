package com.coolcat;

/**
 * @author cool cat
 */
public class Node {
    Node(Operator op) {
        this.data = 0.0;
        this.op = op;
    }

    public Operator getOp() {
        return op;
    }

    public void setData(Double data) {
        this.data = data;
    }

    public Double getData() {
        return data;
    }

    Node(Double data) {
        this.data = data;
        this.op = Operator.nil;
    }

    final private Operator op;

    private Double data;

    Node left;

    Node right;

}