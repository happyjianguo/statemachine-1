package com.wenyu7980.statemachine.guard.textguard;

import java.io.StringReader;
/**
 * Copyright wenyu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *
 * @author:wenyu
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
