package pl.edu.agh.zk.watchers;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

import static org.apache.zookeeper.Watcher.Event.EventType.NodeChildrenChanged;

public class Watcher implements org.apache.zookeeper.Watcher {

    private final ZooKeeper zooKeeper;
    private final String znodeName;

    public Watcher(ZooKeeper zooKeeper, String znodeName) {
        this.zooKeeper = zooKeeper;
        this.znodeName = znodeName;
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getPath() == null || !event.getPath().equals(znodeName)) {
            try {
                zooKeeper.getChildren(znodeName, this);
            } catch (KeeperException | InterruptedException e) {
                System.err.println(String.format("Failed to extract children from node %s. Exception: %s",zooKeeper, e));
            }
        }
        if (event.getType() == NodeChildrenChanged) {
            int count = countChildren(znodeName);
            System.out.println(String.format("Number of children is %s:",count));
        }
    }

    private int countChildren(String path) {
        int count = 0;
        List<String> children;
        try {
            children = zooKeeper.getChildren(path, this);
        } catch (KeeperException | InterruptedException e) {
            System.err.println(String.format("Failed to extract children from node %s. Exception: %s",zooKeeper, e));
            return 0;
        }
        for (String child : children) {
            final String childPath = path + "/" + child;
            count++;
            count += countChildren(childPath);
        }
        return count;
    }
}
