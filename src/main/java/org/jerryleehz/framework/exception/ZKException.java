package org.jerryleehz.framework.exception;

/**
 * Created by lxji on 15/4/23.
 */
public class ZKException extends Exception {
    public ZKException(String message) {
        super(message);
    }

    public ZKException(Throwable throwable) {
        super(throwable);
    }
}
