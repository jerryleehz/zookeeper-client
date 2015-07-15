package org.jerryleehz.framework;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.jerryleehz.framework.exception.ZKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by lxji on 15/4/23.
 */
public class ZKClient {
    private static final Logger logger = LoggerFactory.getLogger(ZKClient.class);

    private ZKConnectionFactory connectionFactory;

    public ZKClient(ZKConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void createChild(String path) throws ZKException {
        if (!path.startsWith("/")) {
            throw new ZKException("path must start with /");
        } else if (!path.equals("/") && path.endsWith("/")) {
            throw new ZKException("path must not end with /");
        }

        String[] substrs = path.split("/");
        String curPath = "/";

        ZKConnection connection = connectionFactory.create();
        ZooKeeper zooKeeper = connection.getZooKeeper();

        try {
            for (int i = 1; i < substrs.length; i++) {
                curPath += substrs[i];
                if (!exists(curPath)) {
                    try {
                        zooKeeper.create(curPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                        throw new ZKException(e);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new ZKException(e);
                    }
                }

                curPath += "/";
            }
        } finally {
            connection.close();
        }
    }

    public void writeData(String path, byte[] data) throws ZKException {
        String parentPath = path.substring(0, path.lastIndexOf('/'));
        if (!parentPath.isEmpty()) {
            createChild(parentPath);
        }

        ZKConnection connection = connectionFactory.create();
        ZooKeeper zooKeeper = connection.getZooKeeper();
        try {
            if (exists(path)) {
                try {
                    Stat stat = zooKeeper.setData(path, data, -1);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            connection.close();
        }
    }

    public byte[] readData(String path) throws ZKException {
        ZKConnection connection = connectionFactory.create();
        ZooKeeper zooKeeper = connection.getZooKeeper();

        try {
            if (exists(path)) {
                try {
                    byte[] data = zooKeeper.getData(path, false, null);
                    if (data != null) {
                        return data;
                    }
                } catch (KeeperException e) {
                    e.printStackTrace();
                    throw new ZKException(e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new ZKException(e);
                }
            }
        } finally {
            connection.close();
        }

        return null;
    }

    public boolean exists(String path) throws ZKException {
        ZKConnection connection = connectionFactory.create();
        ZooKeeper zooKeeper = connection.getZooKeeper();

        try {
            Stat stat = zooKeeper.exists(path, false);
            return stat != null;
        } catch (KeeperException e) {
            logger.debug("", e);
        } catch (InterruptedException e) {
            logger.debug("", e);
        } finally {
            connection.close();
        }

        return false;
    }

    public List<String> getChildren(String path) throws ZKException {
        ZKConnection connection = connectionFactory.create();
        ZooKeeper zooKeeper = connection.getZooKeeper();

        List<String> children = null;
        try {
            children = zooKeeper.getChildren(path, false);
        } catch (KeeperException e) {
            e.printStackTrace();
            throw new ZKException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new ZKException(e);
        } finally {
            connection.close();
        }

        return children;
    }

    public void removeChild(String path) throws ZKException {
        if (!exists(path)) {
            return;
        }

        ZKConnection connection = connectionFactory.create();
        ZooKeeper zooKeeper = connection.getZooKeeper();

        try {
            zooKeeper.delete(path, -1);
        } catch (InterruptedException e) {
            logger.debug("", e);
            throw new ZKException(e);
        } catch (KeeperException e) {
            logger.debug("", e);
            throw new ZKException(e);
        } finally {
            connection.close();
        }
    }
}
