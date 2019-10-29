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

import com.wenyu7980.statemachine.StateMachine3d;

/**
 * 状态机监听
 * 使用场景：
 * 对状态的所有调用都监听
 * @author wenyu
 * @date 2018年11月2日 下午6:24:13
 * @param <T> 数据
 * @param <S1> 状态
 * @param <S2> 状态
 * @param <S3>
 * @param <E> 事件
 */
public interface StateMachineListener3d<T, S1, S2, S3, E> extends
        AbstractStateMachineListener<T, StateMachine3d.StateTriple<S1, S2, S3>, E> {
}