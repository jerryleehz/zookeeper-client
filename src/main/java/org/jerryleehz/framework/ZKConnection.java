package org.jerryleehz.framework;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created by lxji on 15/4/23.
 */
public class ZKConnection {
    private ZooKeeper zooKeeper;

    public ZKConnection() {

    }

    public void connect(String uri, AbstractZKWatcher watcher, int timeout) throws IOException {
        zooKeeper = new ZooKeeper(uri, timeout, watcher);
    }

    public void close() {
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }
}
