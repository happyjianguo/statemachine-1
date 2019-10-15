package com.wenyu7980.statemachine.guard.textguard;

import java.io.StringReader;

/**
 *
 * @author:benshuo.chi
 * @date:2019/6/4
 */
public class StateMachineValue {
    public static Object get(String text, Object data, Object context) {
        StateMachineValuePaser paser = new StateMachineValuePaser(
                new StringReader(text));
        try {
            return paser.express(data, context);
        } catch (ParseException e1) {
            e1.printStackTrace();
            throw new TextGuardException(text);
        }
    }
}
