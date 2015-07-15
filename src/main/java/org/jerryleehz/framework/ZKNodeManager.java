package org.jerryleehz.framework;

import org.jerryleehz.framework.exception.ZKException;

import java.util.List;

/**
 * Created by lxji on 15/4/23.
 */
public interface ZKNodeManager {
    void createChild(String path) throws ZKException;
    void setNodeData(String path, byte[] data) throws ZKException;
    byte[] getNodeData(String path) throws ZKException;
    boolean exists(String path) throws ZKException;
    List<String> getChildren(String path) throws ZKException;
    void removeChild(String path) throws ZKException;
}
