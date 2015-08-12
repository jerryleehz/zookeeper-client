package org.jerryleehz.framework;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.RetryNTimes;
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
        CuratorFramework curator = CuratorFrameworkFactory.newClient(uri, 3000, 3000, new RetryNTimes(5, 1000));
        curator.start();

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
