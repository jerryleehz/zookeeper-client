package org.jerryleehz.framework;

import org.jerryleehz.framework.exception.ZKException;

import java.io.IOException;

/**
 * Created by lxji on 15/4/23.
 */
public class ZKExecutor implements Runnable {
    private static final int SESSION_TIMEOUT = 30000;

    private ZKConnectionFactory connectionFactory;
    private ZKClientFactory clientFactory;

    public ZKExecutor(final String uri, AbstractZKWatcher watcher) throws ZKException {
        connectionFactory = new ZKConnectionFactory(uri, watcher, SESSION_TIMEOUT);
        clientFactory = new ZKClientFactory();

        ZKClient client = clientFactory.create(connectionFactory);
        client.writeData("/root/node1/name/0", "ccc".getBytes());
        System.out.println(new String(client.readData("/root/node1/name/0")));
    }

    @Override
    public void run() {

    }

    public static void main(final String[] args) throws ZKException, IOException {
        ZKExecutor executor = new ZKExecutor("127.0.0.1", null);
    }
}
