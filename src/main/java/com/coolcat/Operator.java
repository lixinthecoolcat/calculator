package com.coolcat;

public enum Operator implements Operatable {
    add {
        @Override
        public Integer operate(Node node) {

            return node.left.data + node.right.data;
        }

    }, sub {
        @Override
        public Integer operate(Node node) {
            return node.left.data - node.right.data;
        }
    }, mult {
        @Override
        public Integer operate(Node node) {
            return node.left.data * node.right.data;
        }
    }, div {
        @Override
        public Integer operate(Node node) {
            if (node.right.data != 0) {
                return node.left.data / node.right.data;
            } else {
                throw new IllegalArgumentException("div second argument can't be zero.");
            }
        }
    }, nil {
        @Override
        public Integer operate(Node node) {
            return node.data;
        }


    }, let {
        @Override
        public Integer operate(Node node) {
            return node.data;
        }


    }
}


