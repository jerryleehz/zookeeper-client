package org.jerryleehz.framework;

import org.jerryleehz.framework.exception.ZKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by lxji on 15/4/23.
 */
public class ZKConnectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(ZKConnectionFactory.class);

    private static final int SESSION_TIMEOUT = 50000;

    protected String uri;
    protected AbstractZKWatcher watcher;
    protected int sessionTimeout;

    public ZKConnectionFactory(String uri, AbstractZKWatcher watcher, int timeout) {
        this.uri = uri;
        this.watcher = watcher;
        setSessionTimeout(timeout);
    }

    public ZKConnectionFactory(String uri) {
        this(uri, null, SESSION_TIMEOUT);
    }

    public void setWatcher(AbstractZKWatcher watcher) {
        this.watcher = watcher;
    }

    public AbstractZKWatcher getWatcher() {
        return watcher;
    }

    public void setSessionTimeout(int timeout) {
        this.sessionTimeout = Math.max(timeout, SESSION_TIMEOUT);
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public ZKConnection create() throws ZKException {
        ZKConnection connection = new ZKConnection();
        try {
            connection.connect(uri, watcher, sessionTimeout);
            return connection;
        } catch (IOException e) {
            logger.debug("", e);
            throw new ZKException(e);
        }
    }
}
