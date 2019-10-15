package com.wenyu7980.statemachine.listener;

/**
 * Copyright 2019 WenYu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import com.wenyu7980.statemachine.StateMachine2d;

/**
 * 状态机监听
 * 使用场景：
 * 对状态的所有调用都监听
 * @author wenyu
 * @date 2018年11月2日 下午6:24:13
 * @param <T> 数据
 * @param <S1> 状态
 * @param <S2> 状态
 * @param <E> 事件
 */
public interface StateMachineListener2d<T, S1 extends Enum<S1>, S2 extends Enum<S2>, E extends Enum<E>>
        extends
        AbstractStateMachineListener<T, StateMachine2d.StatePair<S1, S2>, E> {
}