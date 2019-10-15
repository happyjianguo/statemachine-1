package com.wenyu7980.statemachine;

import com.wenyu7980.statemachine.guard.textguard.StateMachineValue;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 *
 * @author:benshuo.chi
 * @date:2019/6/4
 */
public class StateMachineValueTest {
    @Test
    public void t1() {
        Data data = new Data(new Data.Inner(BigDecimal.ONE));
        Assert.assertEquals(BigDecimal.ONE,
                StateMachineValue.get("#.inner.data;", data, null));
        Assert.assertEquals(BigDecimal.valueOf(2), StateMachineValue
                .get("#.inner.data + #.inner.data;", data, null));
        Assert.assertEquals(BigDecimal.valueOf(1), StateMachineValue
                .get("#.inner.data * #.inner.data;", data, null));
        Assert.assertEquals(BigDecimal.ONE,
                StateMachineValue.get("$.inner.data;", null, data));
    }

    @Test
    public void testNull() {
        Assert.assertNull("null", StateMachineValue.get("null;", null, null));
        Assert.assertFalse("null <> null;",
                (boolean) StateMachineValue.get("null <> null;", null, null));
        Assert.assertTrue("null == null;",
                (boolean) StateMachineValue.get("null == null;", null, null));
    }

    @Test
    public void testString() {
        Assert.assertTrue("'' <> null;",
                (boolean) StateMachineValue.get("\"\" <> null;", null, null));
        Assert.assertFalse("'' == null;",
                (boolean) StateMachineValue.get("\"\" == null;", null, null));
    }
}
