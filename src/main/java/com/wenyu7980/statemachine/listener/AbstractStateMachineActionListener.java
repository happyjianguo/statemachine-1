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

/**
 * 动作监听
 * 使用场景
 * 状态迁移之后，对所有的状态迁移都监听
 *
 * @author wenyu
 * @date 2018年11月2日 下午6:24:44
 * @param <T>
 * @param <S>
 * @param <E>
 */
@FunctionalInterface
public interface AbstractStateMachineActionListener<T, S, E, C> {

    /**
     * 状态机监听
     * @param t
     * @param from
     * @param e
     * @param to
     * @param c
     */
    void listener(final T t, final S from, final E e, final S to, final C c);
}
