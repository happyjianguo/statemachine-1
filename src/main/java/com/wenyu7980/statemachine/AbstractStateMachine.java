package com.wenyu7980.statemachine;

import com.wenyu7980.statemachine.exception.StatemachineExceptionSupplier;
import com.wenyu7980.statemachine.guard.StateMachineGuard;
import com.wenyu7980.statemachine.listener.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

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

/**
 * 状态机
 * =============================================================================
 * preListener         -> listen before statemachine
 * exitStateListeners  -> listen exit state (离开状态)
 * preEventListeners   -> listen before event (事件触发前)
 *                     -> S + E + if(guard成立) => S 获取新状态
 * setState            -> 设置新状态
 * transformListeners  -> transform listen （状态转换）
 * postEventListeners  -> listen after event （时间触发后）
 * enterStateListeners -> listen enter state (进入状态)
 * actionListener      -> action listen
 * postListener        -> listen after statemachine
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
    /** 状态机状态列表: <<当前状态，事件>,<列表<守卫，转换后状态>>> */
    private List<MachineContainer<T, S, E>> states = new ArrayList<>();
    /** 状态机监听（前） */
    private List<AbstractStateMachineListener<T, S, E>> preListeners = new ArrayList<>();
    /** 状态机监听（后） */
    private List<AbstractStateMachineListener<T, S, E>> postListeners = new ArrayList<>();
    /** 状态机事件监听列表（前） */
    private Map<E, List<AbstractStateMachineEventListener<T, S, E, C>>> preEventListeners = new HashMap<>();
    /** 状态机事件监听列表（后） */
    private Map<E, List<AbstractStateMachineEventListener<T, S, E, C>>> postEventListeners = new HashMap<>();
    /** 设置*/
    private BiConsumer<T, S> setState;
    /** 状态机状态监听列表（进入） */
    private List<AbstractStateMachineStateListener<T, S, E>> enterStateListeners = new ArrayList<>();
    /** 状态机状态监听列表（离开） */
    private List<AbstractStateMachineStateListener<T, S, E>> exitStateListeners = new ArrayList<>();
    /** 状态机转态迁移监听 */
    private List<AbstractStateMachineTransformListener<T, S, E>> transformListeners = new ArrayList<>();
    /** 动作监听 */
    private List<AbstractStateMachineActionListener<T, S, E, C>> actions = new ArrayList<>();
    /** 异常处理 */
    private StatemachineExceptionSupplier<T, S, E, ?> supplier;

    public <X extends RuntimeException> void setSupplier(
            StatemachineExceptionSupplier<T, S, E, X> supplier) {
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
    public void addListener(AbstractStateMachineListener<T, S, E> listener) {
        if (listener.isPost()) {
            this.postListeners.add(listener);
        } else {
            this.preListeners.add(listener);
        }
    }

    /**
     * 状态机监听
     * @param listeners
     */
    public void addListeners(
            Collection<AbstractStateMachineListener<T, S, E>> listeners) {
        listeners.stream().forEach((listener) -> {
            addListener(listener);
        });
    }

    /**
     * 状态机事件监听列表
     * @param listener
     */
    public void addEventListener(
            AbstractStateMachineEventListener<T, S, E, C> listener) {
        if (listener.isPost()) {
            if (!this.postEventListeners.containsKey(listener.event())) {
                this.postEventListeners
                        .put(listener.event(), new ArrayList<>());
            }
            this.postEventListeners.get(listener.event()).add(listener);
        } else {
            if (!this.preEventListeners.containsKey(listener.event())) {
                this.preEventListeners.put(listener.event(), new ArrayList<>());
            }
            this.preEventListeners.get(listener.event()).add(listener);
        }
    }

    /**
     * 状态机状态监听列表
     * @param listener
     */
    public void addStateListener(
            AbstractStateMachineStateListener<T, S, E> listener) {
        if (listener.isEnter()) {
            this.enterStateListeners.add(listener);
        } else {
            this.exitStateListeners.add(listener);
        }
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
     * 动作监听
     * @param action
     */
    public void addAction(
            AbstractStateMachineActionListener<T, S, E, C> action) {
        this.actions.add(action);
    }

    /**
     * 动作监听
     * @param actions
     */
    public void addActions(
            Collection<AbstractStateMachineActionListener<T, S, E, C>> actions) {
        this.actions.addAll(actions);
    }

    /**
     * 批量设定事件监听
     * @param listensers
     * @return
     */
    public AbstractStateMachine<T, S, E, C> addEventListeners(
            Collection<AbstractStateMachineEventListener<T, S, E, C>> listensers) {
        listensers.stream().forEach((listener) -> {
            addEventListener(listener);
        });
        return this;
    }

    /**
     * 批量设定状态监听
     * @param listensers
     * @return
     */
    public AbstractStateMachine<T, S, E, C> addStateListeners(
            Collection<AbstractStateMachineStateListener<T, S, E>> listensers) {
        listensers.stream().forEach((listener) -> {
            addStateListener(listener);
        });
        return this;
    }

    /**
     * 批量设定状态后监听
     * @param listensers
     * @return
     */
    public AbstractStateMachine<T, S, E, C> addTransformListeners(
            Collection<AbstractStateMachineTransformListener<T, S, E>> listensers) {
        listensers.stream().forEach((listener) -> {
            addTransformListener(listener);
        });
        return this;
    }

    /**
     * 状态设置回调
     * @param setState
     */
    public void setSetState(BiConsumer<T, S> setState) {
        this.setState = setState;
    }

    /**
     * 状态切换
     * @param t
     * @param state
     * @param event
     * @param <X>
     * @return
     */
    public <X extends RuntimeException> S sendEvent(final T t, final S state,
            final E event) {
        return this.sendEvent(t, state, event, null);
    }

    /**
     *
     * 状态切换
     *
     * @param t
     * @param state
     *            现在状态
     * @param event
     * @param context 守卫判断的值
     * @return 转换后状态
     */
    public <X extends RuntimeException> S sendEvent(final T t, final S state,
            final E event, final C context) {
        LOGGER.debug("状态机{}开始调用,源状态:{},事件:{},上下文:{}", this.getClass(), state,
                event, context);
        // 监听状态机（前）
        this.preListeners.stream().forEach((listener) -> {
            LOGGER.debug("监听状态机（前）:{}", listener.getClass());
            listener.listener(t, state, event);
        });
        // 状态事件（离开）
        this.exitStateListeners.stream()
                .filter(listener -> listener.state().match(state))
                .forEach(listener -> {
                    LOGGER.debug("状态事件（离开）:{}", listener.getClass());
                    listener.listener(t, event);
                });
        // 监听事件（前）
        if (this.preEventListeners.containsKey(event)) {
            this.preEventListeners.get(event).forEach(action -> {
                LOGGER.debug("监听事件（前）:{}", action.getClass());
                action.listener(t, state, null, context);
            });
        }
        // 转换
        List<S> stats = this.states.stream()
                .filter(container -> container.match(t, state, event, context))
                .map(MachineContainer::getTo).collect(Collectors.toList());
        if (stats.size() > 1 || stats.size() == 0) {
            throw this.supplier.get(t, state, event);
        }
        final S s = stats.get(0);
        if (setState != null) {
            setState.accept(t, s);
        }
        LOGGER.debug("迁移后状态:{}", s);
        // 状态迁移
        this.transformListeners.stream()
                .filter(listener -> listener.source().match(state) && listener
                        .target().match(s)).forEach(listener -> {
            LOGGER.debug("状态迁移:{}", listener.getClass());
            listener.listener(t, event);
        });
        // 监听事件（后）
        if (this.postEventListeners.containsKey(event)) {
            this.postEventListeners.get(event).forEach(action -> {
                LOGGER.debug("监听事件（后）:{}", action.getClass());
                action.listener(t, state, s, context);
            });
        }
        // 状态事件（进入）
        this.enterStateListeners.stream()
                .filter(listener -> listener.state().match(s))
                .forEach(listener -> {
                    LOGGER.debug("状态事件（进入）:{}", listener.getClass());
                    listener.listener(t, event);
                });
        // 动作监听
        this.actions.stream().forEach((action) -> {
            LOGGER.debug("动作监听:{}", action.getClass());
            action.listener(t, state, event, s, context);
        });
        // 监听状态机（后）
        this.postListeners.stream().forEach((listener) -> {
            LOGGER.debug("监听状态机（后）:{}", listener.getClass());
            listener.listener(t, s, event);
        });
        LOGGER.debug("状态机{}调用结束", this.getClass());
        return s;
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
