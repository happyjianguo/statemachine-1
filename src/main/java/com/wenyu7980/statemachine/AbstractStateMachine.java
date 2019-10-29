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
import com.wenyu7980.statemachine.guard.StateMachineGuard;
import com.wenyu7980.statemachine.listener.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 状态机
 * =============================================================================
 * listen before statemachine（状态机前）
 * listen exit state (离开状态)
 * listen before event (事件触发前)
 * S + E + if（guard） => S 获取新状态
 * setState => 设置新状态
 * listen transform（状态迁移）
 * listen action （动作监听）
 * listen after event （事件触发后）
 * listen enter state （进入状态）
 * listen after statemachine （状态机后）
 * =============================================================================
 *
 * @author wenyu
 *
 * @param <T>
 * @param <S>
 * @param <E>
 * @param <C>
 */
public abstract class AbstractStateMachine<T, S extends StateContainer, E, C> {

    /** 日志 */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractStateMachine.class);
    /** 获取状态 */
    public Function<T, S> getState;
    /** 设置*/
    private BiConsumer<T, S> setState;
    /** 状态机状态列表*/
    private List<MachineContainer<T, S, E>> states = new ArrayList<>();
    /** 状态机监听 */
    private List<AbstractStateMachineListener<T, S, E>> listeners = new ArrayList<>();
    /** 状态机事件监听列表 */
    private List<AbstractStateMachineEventListener<T, S, E, C>> eventListeners = new ArrayList<>();
    /** 状态机状态监听列表 */
    private List<AbstractStateMachineStateListener<T, S, E>> stateListeners = new ArrayList<>();
    /** 状态机转态迁移监听 */
    private List<AbstractStateMachineTransformListener<T, S, E>> transformListeners = new ArrayList<>();
    /** 动作监听 */
    private List<AbstractStateMachineActionListener<T, S, E, C>> actions = new ArrayList<>();
    /** 异常处理 */
    private StatemachineExceptionSupplier<T, S, E, ? extends RuntimeException> supplier;

    /**
     * 构造函数
     * @param getState
     * @param setState
     * @param supplier
     */
    public AbstractStateMachine(Function<T, S> getState,
            BiConsumer<T, S> setState,
            StatemachineExceptionSupplier<T, S, E, ? extends RuntimeException> supplier) {
        assert setState != null;
        assert getState != null;
        assert supplier != null;
        this.setState = setState;
        this.getState = getState;
        this.supplier = supplier;
    }

    /**
     * 添加状态
     *
     * @param from
     * @param event
     * @param to
     * @param guard
     * @return
     */
    public AbstractStateMachine<T, S, E, C> addState(S from, E event, S to,
            StateMachineGuard<T, S, E> guard) {
        states.add(new MachineContainer<>(from, event, to, guard));
        return this;
    }

    /**
     * 添加状态
     *
     * @param from
     * @param event
     * @param to
     * @return
     */
    public AbstractStateMachine<T, S, E, C> addState(S from, E event, S to) {
        addState(from, event, to, null);
        return this;
    }

    /**
     * 状态机监听
     * @param listener
     */
    public AbstractStateMachine<T, S, E, C> addListener(
            AbstractStateMachineListener<T, S, E> listener) {
        listeners.add(listener);
        return this;
    }

    /**
     * 状态机监听
     * @param listeners
     */
    public AbstractStateMachine<T, S, E, C> addListeners(
            Collection<AbstractStateMachineListener<T, S, E>> listeners) {
        listeners.addAll(listeners);
        return this;
    }

    /**
     * 事件监听
     * @param listener
     */
    public AbstractStateMachine<T, S, E, C> addEventListener(
            AbstractStateMachineEventListener<T, S, E, C> listener) {
        this.eventListeners.add(listener);
        return this;
    }

    /**
     * 事件监听
     * @param listensers
     * @return
     */
    public AbstractStateMachine<T, S, E, C> addEventListeners(
            Collection<AbstractStateMachineEventListener<T, S, E, C>> listensers) {
        this.eventListeners.addAll(listensers);
        return this;
    }

    /**
     * 状态监听
     * @param listener
     */
    public AbstractStateMachine<T, S, E, C> addStateListener(
            AbstractStateMachineStateListener<T, S, E> listener) {
        this.stateListeners.add(listener);
        return this;
    }

    /**
     * 状态监听
     * @param listensers
     * @return
     */
    public AbstractStateMachine<T, S, E, C> addStateListeners(
            Collection<AbstractStateMachineStateListener<T, S, E>> listensers) {
        this.stateListeners.addAll(listensers);
        return this;
    }

    /**
     * 状态迁移监听
     * @param listener
     */
    public void addTransformListener(
            AbstractStateMachineTransformListener<T, S, E> listener) {
        this.transformListeners.add(listener);
    }

    /**
     * 状态迁移监听
     * @param listensers
     * @return
     */
    public AbstractStateMachine<T, S, E, C> addTransformListeners(
            Collection<AbstractStateMachineTransformListener<T, S, E>> listensers) {
        this.transformListeners.addAll(listensers);
        return this;
    }

    /**
     * 动作监听
     * @param action
     */
    public AbstractStateMachine<T, S, E, C> addAction(
            AbstractStateMachineActionListener<T, S, E, C> action) {
        this.actions.add(action);
        return this;
    }

    /**
     * 动作监听
     * @param actions
     */
    public AbstractStateMachine<T, S, E, C> addActions(
            Collection<AbstractStateMachineActionListener<T, S, E, C>> actions) {
        this.actions.addAll(actions);
        return this;
    }

    /**
     *
     * 状态切换
     *
     * @param t
     * @param state 现在状态
     * @param event 事件
     * @param context
     * @return 转换后状态
     */
    public <X extends RuntimeException> S sendEvent(final T t, final S state,
            final E event, final C context) {
        LOGGER.debug("状态机{}开始调用,源状态:{},事件:{},上下文:{}", this.getClass(), state,
                event, context);
        // 监听状态机（前）
        this.listeners.stream().filter(listener -> !listener.isPost())
                .forEach((listener) -> {
                    LOGGER.debug("监听状态机（前）:{}", listener.getClass());
                    listener.listener(t, state, event);
                });
        // 状态事件（离开）
        this.stateListeners.stream()
                .filter(listener -> !listener.isEnter() && listener.state()
                        .match(state)).forEach(listener -> {
            LOGGER.debug("状态事件（离开）:{}", listener.getClass());
            listener.listener(t, event);
        });
        // 监听事件（前）
        this.eventListeners.stream()
                .filter(listener -> !listener.isPost() && Objects
                        .equals(listener.event(), event)).forEach(action -> {
            LOGGER.debug("监听事件（前）:{}", action.getClass());
            action.listener(t, state, null, context);
        });
        // 转换
        List<S> stats = this.states.stream()
                .filter(container -> container.match(t, state, event, context))
                .map(MachineContainer::getTo).collect(Collectors.toList());
        if (stats.size() > 1 || stats.size() == 0) {
            throw this.supplier.get(t, state, event);
        }
        final S s = stats.get(0);
        setState.accept(t, s);
        LOGGER.debug("迁移后状态:{}", s);
        // 状态迁移
        this.transformListeners.stream()
                .filter(listener -> listener.source().match(state) && listener
                        .target().match(s)).forEach(listener -> {
            LOGGER.debug("状态迁移:{}", listener.getClass());
            listener.listener(t, event);
        });
        // 动作监听
        this.actions.stream().forEach((action) -> {
            LOGGER.debug("动作监听:{}", action.getClass());
            action.listener(t, state, event, s, context);
        });
        // 监听事件（后）
        this.eventListeners.stream()
                .filter(listener -> listener.isPost() && Objects
                        .equals(listener.event(), event)).forEach(action -> {
            LOGGER.debug("监听事件（后）:{}", action.getClass());
            action.listener(t, state, s, context);
        });
        // 状态事件（进入）
        this.stateListeners.stream()
                .filter(listener -> listener.isEnter() && listener.state()
                        .match(s)).forEach(listener -> {
            LOGGER.debug("状态事件（进入）:{}", listener.getClass());
            listener.listener(t, event);
        });
        // 监听状态机（后）
        this.listeners.stream().filter(listener -> listener.isPost())
                .forEach((listener) -> {
                    LOGGER.debug("监听状态机（后）:{}", listener.getClass());
                    listener.listener(t, s, event);
                });
        LOGGER.debug("状态机{}调用结束", this.getClass());
        return s;
    }

    public <X extends RuntimeException> S sendEvent(final T t, final S state,
            final E event) {
        return this.sendEvent(t, state, event, null);
    }

    public S sendEvent(T t, E event, C context) {
        return this.sendEvent(t, getState.apply(t), event, context);
    }

    public S sendEvent(T t, E event) {
        return this.sendEvent(t, event, null);
    }

    protected static class MachineContainer<T, S extends StateContainer, E> {
        private S from;
        private E event;
        private StateMachineGuard<T, S, E> guard;
        private S to;

        public MachineContainer(S from, E event, S to,
                StateMachineGuard<T, S, E> guard) {
            this.from = from;
            this.event = event;
            this.to = to;
            this.guard = guard;
        }

        public boolean match(T t, S from, E event, Object object) {
            return this.from.match(from) && Objects.equals(this.event, event)
                    && (this.guard == null || this.guard
                    .guard(t, from, event, object));
        }

        public S getTo() {
            return this.to;
        }
    }
}
