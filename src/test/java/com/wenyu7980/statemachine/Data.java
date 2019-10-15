package com.wenyu7980.statemachine;

import java.math.BigDecimal;

public class Data {
    private State s;
    private Inner inner;
    private BigDecimal zero = BigDecimal.ZERO;
    private Boolean uBoolean = true;
    private boolean lBoolean = false;

    public Data() {
    }

    public Data(Inner inner) {
        this.inner = inner;
    }

    public State getS() {
        return s;
    }

    public void setS(State s) {
        this.s = s;
    }

    public static class Inner {
        private BigDecimal data;

        public Inner(BigDecimal data) {
            this.data = data;
        }

        public void setData(BigDecimal data) {
            this.data = data;
        }

    }

}
