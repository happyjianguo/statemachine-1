package com.wenyu7980.statemachine;

import com.wenyu7980.statemachine.exception.StatemachineExceptionSupplier;
import com.wenyu7980.statemachine.guard.StateMachineGuard;
import com.wenyu7980.statemachine.listener.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
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
public abstract class AbstractStateMachine<T, S extends StateContainer, E extends Enum<E>, C> {

    /** 日志 */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractStateMachine.class);
    /** 状态机状态列表: <<当前状态，事件>,<列表<守卫，转换后状态>>> */
    private Map<Node<S, E>, List<Node<StateMachineGuard<T, S, E>, S>>> states = new HashMap<>();
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
    /** 获取状态 */
    public Function<T, S> getState;
    /** 状态机状态监听列表（进入） */
    private List<AbstractStateMachineStateListener<T, S, E>> enterStateListeners = new ArrayList<>();
    /** 状态机状态监听列表（离开） */
    private List<AbstractStateMachineStateListener<T, S, E>> exitStateListeners = new ArrayList<>();
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
        assert from.nonContainNull() && to.nonContainNull();
        Node<S, E> key = new Node<>(from, event);
        if (!states.containsKey(key)) {
            states.put(key, new ArrayList<>());
        }
        states.get(key).add(new Node<>(guard, to));
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
        assert listener.state().isNonNull();
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
        assert listener.source().isNonNull() && listener.target().isNonNull()
                && listener.source().isSameFormat(listener.target());
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
        Node<S, E> pair = new Node<>(state, event);
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
        if (!this.states.containsKey(pair)) {
            throw this.supplier.get(t, state, event);
        }
        List<S> stats = this.states.get(pair).stream()
                .filter(item -> Objects.isNull(item.getKey()) || item.getKey()
                        .guard(t, state, event, context))
                .map(item -> item.getValue()).collect(Collectors.toList());
        if (stats.size() > 1 || stats.size() == 0) {
            throw this.supplier.get(t, state, event);
        }
        final S s = stats.get(0);
        if (setState != null) {
            setState.accept(t, s);
        }
        Node<S, S> states = new Node<>(state, s);
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

    /**
     * Basic hash bin node, used for most entries. (See below for TreeNode
     * subclass, and in LinkedHashMap for its Entry subclass.)
     */
    protected static class Node<K, V> {
        K key;
        V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        @Override
        public final String toString() {
            return key + "=" + value;
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Node) {
                Node<?, ?> e = (Node<?, ?>) o;
                if (Objects.equals(key, e.getKey()) && Objects
                        .equals(value, e.getValue())) {
                    return true;
                }
            }
            return false;
        }
    }
}
