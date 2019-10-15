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
 * 事件监听
 * 使用场景
 * 针对事件的监听，对特定事件前进行校验，对事件后数据设置
 *
 * @author wenyu
 *
 * @param <T> 数据
 * @param <S1> 状态
 * @param <S2> 状态
 * @param <S3>
 * @param <E> 事件
 * @param <C> 上下文
 */
public interface StateMachineEventListener3d<T, S1 extends Enum<S1>, S2 extends Enum<S2>, S3 extends Enum<S3>, E extends Enum<E>, C>
        extends
        AbstractStateMachineEventListener<T, StateMachine3d.StateTriple<S1, S2, S3>, E, C> {
}