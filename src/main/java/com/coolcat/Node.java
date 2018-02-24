package com.coolcat;

/**
 * @author cool cat
 */
public class Node {
    Node(Operator op) {
        this.data = 0;
        this.op = op;
    }

    Node(Integer data) {
        this.data = data;
        this.op = Operator.nil;
    }

    Operator op;

    Integer data;

    Node left;

    Node right;

}