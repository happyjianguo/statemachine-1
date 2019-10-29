package com.wenyu7980.statemachine.listener;
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

import com.wenyu7980.statemachine.StateMachine2d;

/**
 * 事件监听
 * 使用场景
 * 针对事件的监听，对特定事件前进行校验，对事件后数据设置
 *
 * @author wenyu
 *
 * @param <T> 数据
 * @param <S1> 状态
 * @param <S2> 状态
 * @param <E> 事件
 * @param <C> 上下文
 */
public interface StateMachineEventListener2d<T, S1, S2, E, C> extends
        AbstractStateMachineEventListener<T, StateMachine2d.StatePair<S1, S2>, E, C> {
}