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

public class StateMachineTest {

    private StateMachine<Data, State, Event, Review> machine = new StateMachine<>();
    private StatemachineExceptionSupplier<Data, State, Event, NoStateTransformException> supplier = (Data d, State s, Event e) -> new NoStateTransformException(
            "没有状态转换{0},{1}", s.toString(), e.toString());
    private static final Review reviewPass = new Review(ReviewStatus.PASS);
    private static final Review reviewReject = new Review(ReviewStatus.REJECT);

    @Before
    public void Before() {
        machine.setSupplier(supplier);
        // S1--E1-->S2
        machine.addState(State.S1, Event.E1, State.S2);
        // S2--E2+false-->S3
        machine.addState(State.S2, Event.E2, State.S3,
                (data, state, event, value) -> {
                    return false;
                });
        // S2--E2+true-->S4
        machine.addState(State.S2, Event.E2, State.S4,
                (data, state, event, value) -> {
                    return true;
                });
        // S4--E2+true-->S5
        machine.addState(State.S4, Event.E3, State.S5,
                new StateMachineTextGuard<>("1==1;"));
        // S5--E4+<>1-->S5
        machine.addState(State.S5, Event.E4, State.S5,
                new StateMachineTextGuard<>("#.inner.data<>1;"));
        // S5--E4+==1-->S6
        machine.addState(State.S5, Event.E4, State.S6,
                new StateMachineTextGuard<>("#.inner.data==1;"));
        // S6--E5+PASS-->S7
        machine.addState(State.S6, Event.E5, State.S7,
                new StateMachineTextGuard<>("$.status==\"PASS\";"));
        // S6--E5+<>PASS-->S8
        machine.addState(State.S6, Event.E5, State.S8,
                new StateMachineTextGuard<>("$.status<>\"PASS\";"));
        // S8--E6+true-->S9
        machine.addState(State.S8, Event.E6, State.S9,
                new StateMachineTextGuard<>(
                        "$.status==\"PASS\" || $.status==\"Other\" ;"));
    }

    @Test
    public void test() {
        Data data = new Data();
        Assert.assertEquals("S1--E1-->S2", State.S2,
                machine.sendEvent(data, State.S1, Event.E1));
        Assert.assertEquals("S2--E2-->S4", State.S4,
                machine.sendEvent(data, State.S2, Event.E2));
    }

    @Test
    public void TestStateMachineTextGuard() {
        Data data = new Data(new Data.Inner(new BigDecimal(1)));
        Assert.assertEquals("S4--E3-->S5", State.S5,
                machine.sendEvent(data, State.S4, Event.E3));
        Assert.assertEquals("S5--E4-->S6", State.S6,
                machine.sendEvent(data, State.S5, Event.E4));
    }

    @Test
    public void TestStateMachineTextGuard$() {
        Data data = new Data(new Data.Inner(new BigDecimal(1)));
        Assert.assertEquals("S6--E5+PASS-->S7", State.S7,
                machine.sendEvent(data, State.S6, Event.E5, reviewPass));
        Assert.assertEquals("S6--E5+<>PASS-->S8", State.S8,
                machine.sendEvent(data, State.S6, Event.E5, reviewReject));
    }

    @Test
    public void TestStateMachineTextGuardLogic() {
        Data data = new Data(new Data.Inner(new BigDecimal(1)));
        // S8--E6+true-->S9
        Assert.assertEquals("S8--E6+true-->S9", State.S9,
                machine.sendEvent(data, State.S8, Event.E6, reviewPass));
    }

    /**
     * 测试无状态转移异常
     */
    @Test(expected = NoStateTransformException.class)
    public void testNoStateTransformException() {
        Data data = new Data();
        machine.sendEvent(data, State.S1, Event.E3);
    }

    @Test
    public void testBigDecimalZero() {
        Data data = new Data();
        StateMachine<Data, State, Event, Review> machine = new StateMachine<>();
        machine.setSupplier(supplier);
        machine.addState(State.S1, Event.E1, State.S2,
                new StateMachineTextGuard<>("#.zero == 0.0;"));
        machine.addState(State.S2, Event.E2, State.S3,
                new StateMachineTextGuard<>("#.zero <> 1.0;"));
        Assert.assertEquals("S1--E1+true-->S2", State.S2,
                machine.sendEvent(data, State.S1, Event.E1));
        Assert.assertEquals("S2--E2+true-->S3", State.S3,
                machine.sendEvent(data, State.S2, Event.E2));
    }

    @Test
    public void testBoolean() {
        Data data = new Data();
        StateMachine<Data, State, Event, Review> machine = new StateMachine<>();
        machine.setSupplier(supplier);
        machine.addState(State.S1, Event.E1, State.S2,
                new StateMachineTextGuard<>("#.uBoolean;"));
        machine.addState(State.S2, Event.E2, State.S3,
                new StateMachineTextGuard<>("!#.lBoolean ;"));
        machine.addState(State.S3, Event.E3, State.S4,
                new StateMachineTextGuard<>("!(#.lBoolean && #.lBoolean);"));
        Assert.assertEquals("S3--E3+!false-->S3", State.S4,
                machine.sendEvent(data, State.S3, Event.E3));
        Assert.assertEquals("S1--E1+true-->S2", State.S2,
                machine.sendEvent(data, State.S1, Event.E1));
        Assert.assertEquals("S2--E2+true-->S3", State.S3,
                machine.sendEvent(data, State.S2, Event.E2));
    }

    /**
     * 测试是否正常调用listener
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testListener() {
        Data data = new Data();
        StateMachine<Data, State, Event, Review> machine = new StateMachine<>();
        machine.setSupplier(supplier);
        machine.addState(State.S1, Event.E1, State.S2);
        machine.addState(State.S2, Event.E2, State.S3);
        // preListener
        StateMachineListener<Data, State, Event> preListener = Mockito
                .mock(StateMachineListener.class);
        when(preListener.isPost()).thenReturn(false);
        machine.addListener(preListener);
        // preEventListener
        StateMachineEventListener<Data, State, Event, Review> preEventListener = Mockito
                .mock(StateMachineEventListener.class);
        when(preEventListener.isPost()).thenReturn(false);
        when(preEventListener.event()).thenReturn(Event.E1);
        machine.addEventListener(preEventListener);
        // exitStateListener
        StateMachineStateListener<Data, State, Event> exitStateListener = Mockito
                .mock(StateMachineStateListener.class);
        when(exitStateListener.isEnter()).thenReturn(false);
        when(exitStateListener.state()).thenReturn(State.S1);
        machine.addStateListener(exitStateListener);
        // transformListener
        StateMachineTransformListener<Data, State, Event> transformListener = Mockito
                .mock(StateMachineTransformListener.class);
        when(transformListener.source()).thenReturn(State.S1);
        when(transformListener.target()).thenReturn(State.S2);
        machine.addTransformListener(transformListener);
        // actionListener
        StateMachineActionListener<Data, State, Event, Review> actionListener = Mockito
                .mock(StateMachineActionListener.class);
        machine.addAction(actionListener);
        // enterStateListener
        StateMachineStateListener<Data, State, Event> enterStateListener = Mockito
                .mock(StateMachineStateListener.class);
        when(enterStateListener.isEnter()).thenReturn(true);
        when(enterStateListener.state()).thenReturn(State.S2);
        machine.addStateListener(enterStateListener);
        // preEventListener
        StateMachineEventListener<Data, State, Event, Review> postEventListener = Mockito
                .mock(StateMachineEventListener.class);
        when(postEventListener.isPost()).thenReturn(true);
        when(postEventListener.event()).thenReturn(Event.E1);
        machine.addEventListener(postEventListener);
        // postListener
        StateMachineListener<Data, State, Event> postListener = Mockito
                .mock(StateMachineListener.class);
        when(postListener.isPost()).thenReturn(true);
        machine.addListener(postListener);

        machine.setSetState(Data::setS);
        machine.sendEvent(data, State.S1, Event.E1);
        // 确认调用before
        verify(preListener).listener(data, State.S1, Event.E1);
        verify(preEventListener).listener(data, State.S1, null, null);
        verify(exitStateListener).listener(data, Event.E1);
        verify(transformListener).listener(data, Event.E1);
        verify(enterStateListener).listener(data, Event.E1);
        verify(postEventListener).listener(data, State.S1, State.S2, null);
        verify(actionListener)
                .listener(data, State.S1, Event.E1, State.S2, null);
        verify(postListener).listener(data, State.S2, Event.E1);
        Assert.assertEquals(State.S2, data.getS());
    }

}
