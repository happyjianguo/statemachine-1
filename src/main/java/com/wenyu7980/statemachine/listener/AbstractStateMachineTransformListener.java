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
 * 状态迁移监听
 * 使用场景：
 * 状态迁移之后，监听特定的状态迁移
 *
 * @author wenyu
 * @date 2018年11月2日 下午6:40:04
 * @param <T>
 * @param <S>
 * @param <E>
 */
public interface AbstractStateMachineTransformListener<T, S, E> {
    /**
     * 源状态
     * @return
     */
    S source();

    /**
     * 目标状态
     * @return
     */
    S target();

    /**
     * 监听
     *
     * @param t
     * @param event
     */
    void listener(final T t, final E event);
}
