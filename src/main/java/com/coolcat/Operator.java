package com.coolcat;

public enum Operator implements Operatable {
    add {
        @Override
        public Double operate(Node node) {
            return node.left.getData() + node.right.getData();
        }

    }, sub {
        @Override
        public Double operate(Node node) {
            return node.left.getData() - node.right.getData();
        }

    }, mult {
        @Override
        public Double operate(Node node) {
            return node.left.getData() * node.right.getData();
        }

    }, div {
        @Override
        public Double operate(Node node) {
            if (node.right.getData() != 0) {
                return node.left.getData() / node.right.getData();
            } else {
                throw new IllegalArgumentException("div's second argument can't be zero.");
            }
        }

    },
    nil, let
}


