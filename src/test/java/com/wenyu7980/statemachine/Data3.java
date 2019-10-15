package com.wenyu7980.statemachine;

import java.math.BigDecimal;

public class Data3 {
    private State s;
    private State2 s2;
    private State3 s3;
    private Inner inner;
    private BigDecimal zero = BigDecimal.ZERO;
    private Boolean uBoolean = true;
    private boolean lBoolean = false;

    public Data3() {
    }

    public Data3(Inner inner) {
        this.inner = inner;
    }

    public State getS() {
        return s;
    }

    public void setS(State s) {
        this.s = s;
    }

    public State2 getS2() {
        return s2;
    }

    public void setS2(State2 s2) {
        this.s2 = s2;
    }

    public State3 getS3() {
        return s3;
    }

    public void setS3(State3 s3) {
        this.s3 = s3;
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
