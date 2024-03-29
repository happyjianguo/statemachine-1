package com.wenyu7980.statemachine;

import com.wenyu7980.statemachine.exception.StatemachineExceptionSupplier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class StateMachineTest3dNull {

    private StateMachine3d<Data3, State, State2, State3, Event, Review> machine;
    private StatemachineExceptionSupplier<Data3, StateMachine3d.StateTriple<State, State2, State3>, Event, NoStateTransformException> supplier = (Data3 d, StateMachine3d.StateTriple<State, State2, State3> s, Event e) -> new NoStateTransformException(
            "没有状态转换{0},{1},{2},{3}", s.getS1().toString(), s.getS2().toString(),
            s.getS3().toString(), e.toString());
    private static final Review reviewPass = new Review(ReviewStatus.PASS);
    private static final Review reviewReject = new Review(ReviewStatus.REJECT);

    @Before
    public void Before() {
        machine = new StateMachine3d<>(
                (t) -> new StateMachine3d.StateTriple<>(t.getS(), t.getS2(),
                        t.getS3()), (t, s) -> {
            t.setS(s.getS1());
            t.setS2(s.getS2());
            t.setS3(s.getS3());
        }, this.supplier);
        // S1--E1-->S2
        machine.addState(
                new StateMachine3d.StateTriple<>(null, null, State3.S1),
                Event.E1,
                new StateMachine3d.StateTriple<>(null, null, State3.S2));
        // S2--E2+false-->S3
        machine.addState(
                new StateMachine3d.StateTriple<>(State.S2, null, State3.S2),
                Event.E2,
                new StateMachine3d.StateTriple<>(State.S3, null, State3.S3));
    }

    @Test
    public void TestStateMachine() {
        Data3 data3 = new Data3(new Data3.Inner(new BigDecimal(1)));
        Assert.assertEquals("null+null+S1--E1-->null+null+S2",
                new StateMachine3d.StateTriple<>(null, null, State3.S2),
                machine.sendEvent(data3,
                        new StateMachine3d.StateTriple<>(null, null, State3.S1),
                        Event.E1));

        Assert.assertEquals("null+null+S1--E1-->null+null+S2",
                new StateMachine3d.StateTriple<>(State.S3, null, State3.S3),
                machine.sendEvent(data3,
                        new StateMachine3d.StateTriple<>(State.S2, null,
                                State3.S2), Event.E2));
    }

}
