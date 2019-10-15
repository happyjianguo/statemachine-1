package com.wenyu7980.statemachine;

import java.text.MessageFormat;

/**
 * 状态迁移失败
 * 
 * @author wenyu
 *
 */
public class NoStateTransformException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = -5468786026216839313L;

    public NoStateTransformException(String msg, Object... args) {
        super(MessageFormat.format(msg, args));
    }
}
