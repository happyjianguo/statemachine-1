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
 * 状态迁移监听
 * 使用场景：
 * 状态迁移之后，监听特定的状态迁移
 *
 * @author wenyu
 * @date 2018年11月2日 下午6:40:04
 * @param <T>
 * @param <S1>
 * @param <S2>
 * @param <E>
 */
public interface StateMachineTransformListener2d<T, S1, S2, E> extends
        AbstractStateMachineTransformListener<T, StateMachine2d.StatePair<S1, S2>, E> {
}
