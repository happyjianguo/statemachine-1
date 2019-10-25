package com.wenyu7980.statemachine;

import com.wenyu7980.statemachine.exception.StatemachineExceptionSupplier;
import com.wenyu7980.statemachine.guard.StateMachineTextGuard;
import com.wenyu7980.statemachine.listener.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StateMachineTest3d {

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
        machine.addState(new StateMachine3d.StateTriple<>(State.S1, State2.S1,
                        State3.S1), Event.E1,
                new StateMachine3d.StateTriple<>(State.S2, State2.S2,
                        State3.S2));
        // S2--E2+false-->S3
        machine.addState(new StateMachine3d.StateTriple<>(State.S2, State2.S2,
                        State3.S2), Event.E2,
                new StateMachine3d.StateTriple<>(State.S3, State2.S3,
                        State3.S3), (Data3, state, event, value) -> {
                    return false;
                });
        // S2--E2+true-->S4
        machine.addState(new StateMachine3d.StateTriple<>(State.S2, State2.S2,
                        State3.S2), Event.E2,
                new StateMachine3d.StateTriple<>(State.S4, State2.S4,
                        State3.S4), (Data3, state, event, value) -> {
                    return true;
                });
        // S4--E2+true-->S5
        machine.addState(new StateMachine3d.StateTriple<>(State.S4, State2.S4,
                        State3.S4), Event.E3,
                new StateMachine3d.StateTriple<>(State.S5, State2.S5,
                        State3.S5), new StateMachineTextGuard<>("1==1;"));
        // S5--E4+<>1-->S5
        machine.addState(new StateMachine3d.StateTriple<>(State.S5, State2.S5,
                        State3.S5), Event.E4,
                new StateMachine3d.StateTriple<>(State.S5, State2.S5,
                        State3.S5),
                new StateMachineTextGuard<>("#.inner.data<>1;"));
        // S5--E4+==1-->S6
        machine.addState(new StateMachine3d.StateTriple<>(State.S5, State2.S5,
                        State3.S5), Event.E4,
                new StateMachine3d.StateTriple<>(State.S6, State2.S6,
                        State3.S6),
                new StateMachineTextGuard<>("#.inner.data==1;"));
        // S6--E5+PASS-->S7
        machine.addState(new StateMachine3d.StateTriple<>(State.S6, State2.S6,
                        State3.S6), Event.E5,
                new StateMachine3d.StateTriple<>(State.S7, State2.S7,
                        State3.S7),
                new StateMachineTextGuard<>("$.status==\"PASS\";"));
        // S6--E5+<>PASS-->S8
        machine.addState(new StateMachine3d.StateTriple<>(State.S6, State2.S6,
                        State3.S6), Event.E5,
                new StateMachine3d.StateTriple<>(State.S8, State2.S8,
                        State3.S8),
                new StateMachineTextGuard<>("$.status<>\"PASS\";"));
        // S8--E6+true-->S9
        machine.addState(new StateMachine3d.StateTriple<>(State.S8, State2.S8,
                        State3.S8), Event.E6,
                new StateMachine3d.StateTriple<>(State.S9, State2.S9,
                        State3.S9), new StateMachineTextGuard<>(
                        "$.status==\"PASS\" || $.status==\"Other\" ;"));
    }

    @Test
    public void TestStateMachineTextGuard() {
        Data3 Data3 = new Data3(new Data3.Inner(new BigDecimal(1)));
        Assert.assertEquals("S4--E3-->S5",
                new StateMachine3d.StateTriple<>(State.S5, State2.S5,
                        State3.S5), machine.sendEvent(Data3,
                        new StateMachine3d.StateTriple<>(State.S4, State2.S4,
                                State3.S4), Event.E3));
        Assert.assertEquals("S5-S5--E4-->S6-S6",
                new StateMachine3d.StateTriple<>(State.S6, State2.S6,
                        State3.S6), machine.sendEvent(Data3,
                        new StateMachine3d.StateTriple<>(State.S5, State2.S5,
                                State3.S5), Event.E4));
    }

    /**
     * 测试无状态转移异常
     */
    @Test(expected = NoStateTransformException.class)
    public void testNoStateTransformException() {
        Data3 Data3 = new Data3();
        machine.sendEvent(Data3,
                new StateMachine3d.StateTriple<>(State.S1, State2.S1,
                        State3.S1), Event.E3);
    }

    /**
     * 测试是否正常调用listener
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testListener() {
        Data3 Data3 = new Data3();
        StateMachine3d<Data3, State, State2, State3, Event, Review> machine = new StateMachine3d<>(
                (t) -> new StateMachine3d.StateTriple<>(t.getS(), t.getS2(),
                        t.getS3()), (t, s) -> {
            t.setS(s.getS1());
            t.setS2(s.getS2());
            t.setS3(s.getS3());
        }, this.supplier);
        machine.addState(new StateMachine3d.StateTriple<>(State.S1, State2.S2,
                        State3.S3), Event.E1,
                new StateMachine3d.StateTriple<>(State.S2, State2.S2,
                        State3.S3));
        machine.addState(new StateMachine3d.StateTriple<>(State.S2, State2.S2,
                        State3.S3), Event.E2,
                new StateMachine3d.StateTriple<>(State.S3, State2.S2,
                        State3.S3));
        // preListener
        StateMachineListener3d<Data3, State, State2, State3, Event> preListener = Mockito
                .mock(StateMachineListener3d.class);
        when(preListener.isPost()).thenReturn(false);
        machine.addListener(preListener);
        // preEventListener
        StateMachineEventListener3d<Data3, State, State2, State3, Event, Review> preEventListener = Mockito
                .mock(StateMachineEventListener3d.class);
        when(preEventListener.isPost()).thenReturn(false);
        when(preEventListener.event()).thenReturn(Event.E1);
        machine.addEventListener(preEventListener);
        // exitStateListener
        StateMachineStateListener3d<Data3, State, State2, State3, Event> exitStateListener = Mockito
                .mock(StateMachineStateListener3d.class);
        when(exitStateListener.isEnter()).thenReturn(false);
        when(exitStateListener.state()).thenReturn(
                new StateMachine3d.StateTriple<>(State.S1, State2.S2,
                        State3.S3));
        machine.addStateListener(exitStateListener);
        // exitStateListener2
        StateMachineStateListener3d<Data3, State, State2, State3, Event> exitStateListener2 = Mockito
                .mock(StateMachineStateListener3d.class);
        when(exitStateListener2.isEnter()).thenReturn(false);
        when(exitStateListener2.state()).thenReturn(
                new StateMachine3d.StateTriple<>(null, State2.S2, State3.S3));
        machine.addStateListener(exitStateListener2);
        // transformListener
        StateMachineTransformListener3d<Data3, State, State2, State3, Event> transformListener = Mockito
                .mock(StateMachineTransformListener3d.class);
        when(transformListener.source()).thenReturn(
                new StateMachine3d.StateTriple<>(State.S1, State2.S2,
                        State3.S3));
        when(transformListener.target()).thenReturn(
                new StateMachine3d.StateTriple<>(State.S2, State2.S2,
                        State3.S3));
        machine.addTransformListener(transformListener);
        // transformListener2
        StateMachineTransformListener3d<Data3, State, State2, State3, Event> transformListener2 = Mockito
                .mock(StateMachineTransformListener3d.class);
        when(transformListener2.source()).thenReturn(
                new StateMachine3d.StateTriple<>(State.S1, null, State3.S3));
        when(transformListener2.target()).thenReturn(
                new StateMachine3d.StateTriple<>(State.S2, null, State3.S3));
        machine.addTransformListener(transformListener2);
        // actionListener
        StateMachineActionListener3d<Data3, State, State2, State3, Event, Review> actionListener = Mockito
                .mock(StateMachineActionListener3d.class);
        machine.addAction(actionListener);
        // enterStateListener
        StateMachineStateListener3d<Data3, State, State2, State3, Event> enterStateListener = Mockito
                .mock(StateMachineStateListener3d.class);
        when(enterStateListener.isEnter()).thenReturn(true);
        when(enterStateListener.state()).thenReturn(
                new StateMachine3d.StateTriple<>(State.S2, State2.S2,
                        State3.S3));
        machine.addStateListener(enterStateListener);
        // enterStateListener2
        StateMachineStateListener3d<Data3, State, State2, State3, Event> enterStateListener2 = Mockito
                .mock(StateMachineStateListener3d.class);
        when(enterStateListener2.isEnter()).thenReturn(true);
        when(enterStateListener2.state()).thenReturn(
                new StateMachine3d.StateTriple<>(State.S2, State2.S2, null));
        machine.addStateListener(enterStateListener2);
        // preEventListener
        StateMachineEventListener3d<Data3, State, State2, State3, Event, Review> postEventListener = Mockito
                .mock(StateMachineEventListener3d.class);
        when(postEventListener.isPost()).thenReturn(true);
        when(postEventListener.event()).thenReturn(Event.E1);
        machine.addEventListener(postEventListener);
        // postListener
        StateMachineListener3d<Data3, State, State2, State3, Event> postListener = Mockito
                .mock(StateMachineListener3d.class);
        when(postListener.isPost()).thenReturn(true);
        machine.addListener(postListener);
        machine.sendEvent(Data3,
                new StateMachine3d.StateTriple<>(State.S1, State2.S2,
                        State3.S3), Event.E1);
        // 确认调用before
        verify(preListener).listener(Data3,
                new StateMachine3d.StateTriple<>(State.S1, State2.S2,
                        State3.S3), Event.E1);
        verify(preEventListener).listener(Data3,
                new StateMachine3d.StateTriple<>(State.S1, State2.S2,
                        State3.S3), null, null);
        verify(exitStateListener).listener(Data3, Event.E1);
        verify(exitStateListener2).listener(Data3, Event.E1);
        verify(transformListener).listener(Data3, Event.E1);
        verify(transformListener2).listener(Data3, Event.E1);
        verify(enterStateListener).listener(Data3, Event.E1);
        verify(enterStateListener2).listener(Data3, Event.E1);
        verify(postEventListener).listener(Data3,
                new StateMachine3d.StateTriple<>(State.S1, State2.S2,
                        State3.S3),
                new StateMachine3d.StateTriple<>(State.S2, State2.S2,
                        State3.S3), null);
        verify(actionListener).listener(Data3,
                new StateMachine3d.StateTriple<>(State.S1, State2.S2,
                        State3.S3), Event.E1,
                new StateMachine3d.StateTriple<>(State.S2, State2.S2,
                        State3.S3), null);
        verify(postListener).listener(Data3,
                new StateMachine3d.StateTriple<>(State.S2, State2.S2,
                        State3.S3), Event.E1);
        Assert.assertEquals(State.S2, Data3.getS());
    }

}
