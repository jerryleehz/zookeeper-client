package org.jerryleehz.framework;

/**
 * Created by lxji on 15/4/24.
 */
public class ZKClientFactory {
    public ZKClient create(ZKConnectionFactory connectionFactory) {
        return new ZKClient(connectionFactory);
    }
}
