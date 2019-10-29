package com.wenyu7980.statemachine;

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

import com.wenyu7980.statemachine.exception.StatemachineExceptionSupplier;
import com.wenyu7980.statemachine.util.MatchUtil;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 状态机
 *
 * @author wenyu
 *
 * @param <T>
 * @param <S>
 * @param <E>
 */
public class StateMachine<T, S, E, C>
        extends AbstractStateMachine<T, StateMachine.StateSingle<S>, E, C> {

    /**
     * 构造函数
     * @param getState
     * @param setState
     * @param supplier
     */
    public StateMachine(Function<T, StateSingle<S>> getState,
            BiConsumer<T, StateSingle<S>> setState,
            StatemachineExceptionSupplier<T, StateSingle<S>, E, ? extends RuntimeException> supplier) {
        super(getState, setState, supplier);
    }

    public static class StateSingle<S>
            implements StateContainer<StateSingle<S>> {
        private S s;

        public StateSingle(S s) {
            this.s = s;
        }

        public S getS() {
            return s;
        }

        @Override
        public boolean match(StateSingle<S> single) {
            return MatchUtil.matchOrNull(this.s, single.s);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            StateSingle<?> statePair = (StateSingle<?>) o;
            return Objects.equals(s, statePair.s);
        }

        @Override
        public int hashCode() {
            return Objects.hash(s);
        }

        @Override
        public String toString() {
            return "{" + "s=" + s + '}';
        }
    }
}
