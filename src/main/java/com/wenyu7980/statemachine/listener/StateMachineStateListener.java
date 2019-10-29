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

import com.wenyu7980.statemachine.StateMachine;

/**
 * 状态监听
 * 使用场景：
 * 进入状态或者离开状态对数据（与状态相关的数据）进行相应的设置
 *
 * @author wenyu
 *
 * @param <T> 数据
 * @param <S> 状态
 * @param <E> 事件
 */
public interface StateMachineStateListener<T, S, E> extends
        AbstractStateMachineStateListener<T, StateMachine.StateSingle<S>, E> {
}
