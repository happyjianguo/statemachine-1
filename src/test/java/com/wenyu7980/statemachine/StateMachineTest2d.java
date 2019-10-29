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

public class StateMachineTest2d {

    private StateMachine2d<Data2, State, State2, Event, Review> machine;
    private StatemachineExceptionSupplier<Data2, StateMachine2d.StatePair<State, State2>, Event, NoStateTransformException> supplier = (Data2 d, StateMachine2d.StatePair<State, State2> s, Event e) -> new NoStateTransformException(
            "没有状态转换{0},{1},{2}", s.getS1().toString(), s.getS2().toString(),
            e.toString());
    private static final Review reviewPass = new Review(ReviewStatus.PASS);
    private static final Review reviewReject = new Review(ReviewStatus.REJECT);

    @Before
    public void Before() {
        machine = new StateMachine2d<>(
                (t) -> new StateMachine2d.StatePair<>(t.getS(), t.getS2()),
                (t, s) -> {
                    t.setS(s.getS1());
                    t.setS2(s.getS2());
                }, this.supplier);
        // S1--E1-->S2
        machine.addState(new StateMachine2d.StatePair<>(State.S1, State2.S1),
                Event.E1, new StateMachine2d.StatePair<>(State.S2, State2.S2));
        // S2--E2+false-->S3
        machine.addState(new StateMachine2d.StatePair<>(State.S2, State2.S2),
                Event.E2, new StateMachine2d.StatePair<>(State.S3, State2.S3),
                (Data2, state, event, value) -> {
                    return false;
                });
        // S2--E2+true-->S4
        machine.addState(new StateMachine2d.StatePair<>(State.S2, State2.S2),
                Event.E2, new StateMachine2d.StatePair<>(State.S4, State2.S4),
                (Data2, state, event, value) -> {
                    return true;
                });
        // S4--E2+true-->S5
        machine.addState(new StateMachine2d.StatePair<>(State.S4, State2.S4),
                Event.E3, new StateMachine2d.StatePair<>(State.S5, State2.S5),
                new StateMachineTextGuard<>("1==1;"));
        // S5--E4+<>1-->S5
        machine.addState(new StateMachine2d.StatePair<>(State.S5, State2.S5),
                Event.E4, new StateMachine2d.StatePair<>(State.S5, State2.S5),
                new StateMachineTextGuard<>("#.inner.data<>1;"));
        // S5--E4+==1-->S6
        machine.addState(new StateMachine2d.StatePair<>(State.S5, State2.S5),
                Event.E4, new StateMachine2d.StatePair<>(State.S6, State2.S6),
                new StateMachineTextGuard<>("#.inner.data==1;"));
        // S6--E5+PASS-->S7
        machine.addState(new StateMachine2d.StatePair<>(State.S6, State2.S6),
                Event.E5, new StateMachine2d.StatePair<>(State.S7, State2.S7),
                new StateMachineTextGuard<>("$.status==\"PASS\";"));
        // S6--E5+<>PASS-->S8
        machine.addState(new StateMachine2d.StatePair<>(State.S6, State2.S6),
                Event.E5, new StateMachine2d.StatePair<>(State.S8, State2.S8),
                new StateMachineTextGuard<>("$.status<>\"PASS\";"));
        // S8--E6+true-->S9
        machine.addState(new StateMachine2d.StatePair<>(State.S8, State2.S8),
                Event.E6, new StateMachine2d.StatePair<>(State.S9, State2.S9),
                new StateMachineTextGuard<>(
                        "$.status==\"PASS\" || $.status==\"Other\" ;"));
    }

    @Test
    public void test() {
        Data2 Data2 = new Data2();
        Assert.assertEquals("S1-S1--E1-->S2-S2",
                new StateMachine2d.StatePair<>(State.S2, State2.S2),
                machine.sendEvent(Data2,
                        new StateMachine2d.StatePair<>(State.S1, State2.S1),
                        Event.E1));
        Assert.assertEquals("S2-S2--E2-->S4-S4",
                new StateMachine2d.StatePair<>(State.S4, State2.S4),
                machine.sendEvent(Data2,
                        new StateMachine2d.StatePair<>(State.S2, State2.S2),
                        Event.E2));
    }

    @Test
    public void TestStateMachineTextGuard() {
        Data2 Data2 = new Data2(new Data2.Inner(new BigDecimal(1)));
        Assert.assertEquals("S4--E3-->S5",
                new StateMachine2d.StatePair<>(State.S5, State2.S5),
                machine.sendEvent(Data2,
                        new StateMachine2d.StatePair<>(State.S4, State2.S4),
                        Event.E3));
        Assert.assertEquals("S5-S5--E4-->S6-S6",
                new StateMachine2d.StatePair<>(State.S6, State2.S6),
                machine.sendEvent(Data2,
                        new StateMachine2d.StatePair<>(State.S5, State2.S5),
                        Event.E4));
    }

    /**
     * 测试无状态转移异常
     */
    @Test(expected = NoStateTransformException.class)
    public void testNoStateTransformException() {
        Data2 Data2 = new Data2();
        machine.sendEvent(Data2,
                new StateMachine2d.StatePair<>(State.S1, State2.S1), Event.E3);
    }

    @Test
    public void testBigDecimalZero() {
        Data2 Data2 = new Data2();
        StateMachine2d<Data2, State, State2, Event, Review> machine = new StateMachine2d<>(
                (t) -> new StateMachine2d.StatePair<>(t.getS(), t.getS2()),
                (t, s) -> {
                    t.setS(s.getS1());
                    t.setS2(s.getS2());
                }, this.supplier);
        machine.addState(new StateMachine2d.StatePair<>(State.S1, State2.S1),
                Event.E1, new StateMachine2d.StatePair<>(State.S2, State2.S1),
                new StateMachineTextGuard<>("#.zero == 0.0;"));
        machine.addState(new StateMachine2d.StatePair<>(State.S2, State2.S1),
                Event.E2, new StateMachine2d.StatePair<>(State.S3, State2.S1),
                new StateMachineTextGuard<>("#.zero <> 1.0;"));
        Assert.assertEquals("S1-S1--E1+true-->S2-S1",
                new StateMachine2d.StatePair<>(State.S2, State2.S1),
                machine.sendEvent(Data2,
                        new StateMachine2d.StatePair<>(State.S1, State2.S1),
                        Event.E1));
        Assert.assertEquals("S2-S1--E2+true-->S3-S1",
                new StateMachine2d.StatePair<>(State.S3, State2.S1),
                machine.sendEvent(Data2,
                        new StateMachine2d.StatePair<>(State.S2, State2.S1),
                        Event.E2));
    }

    @Test
    public void testBoolean() {
        Data2 Data2 = new Data2();
        StateMachine2d<Data2, State, State2, Event, Review> machine = new StateMachine2d<>(
                (t) -> new StateMachine2d.StatePair<>(t.getS(), t.getS2()),
                (t, s) -> {
                    t.setS(s.getS1());
                    t.setS2(s.getS2());
                }, this.supplier);
        machine.addState(new StateMachine2d.StatePair<>(State.S1, State2.S1),
                Event.E1, new StateMachine2d.StatePair<>(State.S2, State2.S1),
                new StateMachineTextGuard<>("#.uBoolean;"));
        machine.addState(new StateMachine2d.StatePair<>(State.S2, State2.S1),
                Event.E2, new StateMachine2d.StatePair<>(State.S3, State2.S1),
                new StateMachineTextGuard<>("!#.lBoolean ;"));
        machine.addState(new StateMachine2d.StatePair<>(State.S3, State2.S1),
                Event.E3, new StateMachine2d.StatePair<>(State.S4, State2.S1),
                new StateMachineTextGuard<>("!(#.lBoolean && #.lBoolean);"));
        Assert.assertEquals("S3-S1--E3+!false-->S4-S1",
                new StateMachine2d.StatePair<>(State.S4, State2.S1),
                machine.sendEvent(Data2,
                        new StateMachine2d.StatePair<>(State.S3, State2.S1),
                        Event.E3));
        Assert.assertEquals("S1--E1+true-->S2",
                new StateMachine2d.StatePair<>(State.S2, State2.S1),
                machine.sendEvent(Data2,
                        new StateMachine2d.StatePair<>(State.S1, State2.S1),
                        Event.E1));
        Assert.assertEquals("S2--E2+true-->S3",
                new StateMachine2d.StatePair<>(State.S3, State2.S1),
                machine.sendEvent(Data2,
                        new StateMachine2d.StatePair<>(State.S2, State2.S1),
                        Event.E2));
    }

    /**
     * 测试是否正常调用listener
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testListener() {
        Data2 Data2 = new Data2();
        StateMachine2d<Data2, State, State2, Event, Review> machine = new StateMachine2d<>(
                (t) -> new StateMachine2d.StatePair<>(t.getS(), t.getS2()),
                (t, s) -> {
                    t.setS(s.getS1());
                    t.setS2(s.getS2());
                }, this.supplier);
        machine.addState(new StateMachine2d.StatePair<>(State.S1, State2.S2),
                Event.E1, new StateMachine2d.StatePair<>(State.S2, State2.S2));
        machine.addState(new StateMachine2d.StatePair<>(State.S2, State2.S2),
                Event.E2, new StateMachine2d.StatePair<>(State.S3, State2.S2));
        // preListener
        StateMachineListener2d<Data2, State, State2, Event> preListener = Mockito
                .mock(StateMachineListener2d.class);
        when(preListener.isPost()).thenReturn(false);
        machine.addListener(preListener);
        // preEventListener
        StateMachineEventListener2d<Data2, State, State2, Event, Review> preEventListener = Mockito
                .mock(StateMachineEventListener2d.class);
        when(preEventListener.isPost()).thenReturn(false);
        when(preEventListener.event()).thenReturn(Event.E1);
        machine.addEventListener(preEventListener);
        // exitStateListener
        StateMachineStateListener2d<Data2, State, State2, Event> exitStateListener = Mockito
                .mock(StateMachineStateListener2d.class);
        when(exitStateListener.isEnter()).thenReturn(false);
        when(exitStateListener.state()).thenReturn(
                new StateMachine2d.StatePair<>(State.S1, State2.S2));
        machine.addStateListener(exitStateListener);
        // exitStateListener2
        StateMachineStateListener2d<Data2, State, State2, Event> exitStateListener2 = Mockito
                .mock(StateMachineStateListener2d.class);
        when(exitStateListener2.isEnter()).thenReturn(false);
        when(exitStateListener2.state())
                .thenReturn(new StateMachine2d.StatePair<>(null, State2.S2));
        machine.addStateListener(exitStateListener2);
        // transformListener
        StateMachineTransformListener2d<Data2, State, State2, Event> transformListener = Mockito
                .mock(StateMachineTransformListener2d.class);
        when(transformListener.source()).thenReturn(
                new StateMachine2d.StatePair<>(State.S1, State2.S2));
        when(transformListener.target()).thenReturn(
                new StateMachine2d.StatePair<>(State.S2, State2.S2));
        machine.addTransformListener(transformListener);
        // transformListener2
        StateMachineTransformListener2d<Data2, State, State2, Event> transformListener2 = Mockito
                .mock(StateMachineTransformListener2d.class);
        when(transformListener2.source())
                .thenReturn(new StateMachine2d.StatePair<>(State.S1, null));
        when(transformListener2.target())
                .thenReturn(new StateMachine2d.StatePair<>(State.S2, null));
        machine.addTransformListener(transformListener2);
        // actionListener
        StateMachineActionListener2d<Data2, State, State2, Event, Review> actionListener = Mockito
                .mock(StateMachineActionListener2d.class);
        machine.addAction(actionListener);
        // enterStateListener
        StateMachineStateListener2d<Data2, State, State2, Event> enterStateListener = Mockito
                .mock(StateMachineStateListener2d.class);
        when(enterStateListener.isEnter()).thenReturn(true);
        when(enterStateListener.state()).thenReturn(
                new StateMachine2d.StatePair<>(State.S2, State2.S2));
        machine.addStateListener(enterStateListener);
        // enterStateListener2
        StateMachineStateListener2d<Data2, State, State2, Event> enterStateListener2 = Mockito
                .mock(StateMachineStateListener2d.class);
        when(enterStateListener2.isEnter()).thenReturn(true);
        when(enterStateListener2.state())
                .thenReturn(new StateMachine2d.StatePair<>(State.S2, null));
        machine.addStateListener(enterStateListener2);
        // preEventListener
        StateMachineEventListener2d<Data2, State, State2, Event, Review> postEventListener = Mockito
                .mock(StateMachineEventListener2d.class);
        when(postEventListener.isPost()).thenReturn(true);
        when(postEventListener.event()).thenReturn(Event.E1);
        machine.addEventListener(postEventListener);
        // postListener
        StateMachineListener2d<Data2, State, State2, Event> postListener = Mockito
                .mock(StateMachineListener2d.class);
        when(postListener.isPost()).thenReturn(true);
        machine.addListener(postListener);
        machine.sendEvent(Data2,
                new StateMachine2d.StatePair<>(State.S1, State2.S2), Event.E1);
        // 确认调用before
        verify(preListener).listener(Data2,
                new StateMachine2d.StatePair<>(State.S1, State2.S2), Event.E1);
        verify(preEventListener).listener(Data2,
                new StateMachine2d.StatePair<>(State.S1, State2.S2), null,
                null);
        verify(exitStateListener).listener(Data2, Event.E1);
        verify(exitStateListener2).listener(Data2, Event.E1);
        verify(transformListener).listener(Data2, Event.E1);
        verify(transformListener2).listener(Data2, Event.E1);
        verify(actionListener).listener(Data2,
                new StateMachine2d.StatePair<>(State.S1, State2.S2), Event.E1,
                new StateMachine2d.StatePair<>(State.S2, State2.S2), null);
        verify(enterStateListener).listener(Data2, Event.E1);
        verify(enterStateListener2).listener(Data2, Event.E1);
        verify(postEventListener).listener(Data2,
                new StateMachine2d.StatePair<>(State.S1, State2.S2),
                new StateMachine2d.StatePair<>(State.S2, State2.S2), null);
        verify(postListener).listener(Data2,
                new StateMachine2d.StatePair<>(State.S2, State2.S2), Event.E1);
        Assert.assertEquals(State.S2, Data2.getS());
    }

}
