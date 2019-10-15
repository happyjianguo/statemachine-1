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

import com.wenyu7980.statemachine.StateMachine3d;

/**
 * 动作监听
 * 使用场景
 * 状态迁移之后，对所有的状态迁移都监听
 *
 * @author wenyu
 * @date 2018年11月2日 下午6:24:44
 * @param <T>
 * @param <S1>
 * @param <S2>
 * @param <S3>
 * @param <E>
 */
@FunctionalInterface
public interface StateMachineActionListener3d<T, S1 extends Enum<S1>, S2 extends Enum<S2>, S3 extends Enum<S3>, E extends Enum<E>, C>
        extends
        AbstractStateMachineActionListener<T, StateMachine3d.StateTriple<S1, S2, S3>, E, C> {
}
