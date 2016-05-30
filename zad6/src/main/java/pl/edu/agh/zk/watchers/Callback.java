package pl.edu.agh.zk.watchers;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Arrays;

import static org.apache.zookeeper.AsyncCallback.StatCallback;

public class Callback implements StatCallback, Runnable {

    private final ZooKeeper zooKeeper;
    private final String znodeName;
    private final String[] executableString;
    private Process child;
    private Watcher watcher;
    private byte[] prevData;

    public Callback(ZooKeeper zooKeeper, String znodeName, String[] executableString) {
        this.zooKeeper = zooKeeper;
        this.znodeName = znodeName;
        this.executableString = executableString;
    }

    @Override
    public void processResult(int code, String path, Object ctx, Stat stat) {
        boolean exists;
        switch (code) {
            case KeeperException.Code.Ok:
                exists = true;
                break;
            case KeeperException.Code.NoNode:
                exists = false;
                break;
            case KeeperException.Code.NoAuth:
                quit();
            case KeeperException.Code.SessionExpired:
            default:
                zooKeeper.exists(znodeName, true, this, null);
                return;
        }
        byte b[] = null;
        if (exists) {
            try {
                b = zooKeeper.getData(znodeName, false, null);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                return;
            }
        }
        if (znodeName.equals(path) && ((b == null && prevData != null) || (b != null && !Arrays.equals(prevData, b)))) {
            if (b != null) {
                if (child != null) {
                    child.destroy();
                    child = null;
                }
                try {
                    child = Runtime.getRuntime().exec(executableString);
                } catch (IOException e) {
                    System.err.println("Error running app: " + Arrays.toString(executableString));
                }
            } else {
                if (child != null) {
                    child.destroy();
                    child = null;
                }
            }
            prevData = b;
        }
        if (b != null && path.equals(znodeName)) {
            try {
                zooKeeper.getChildren(znodeName, watcher);
            } catch (KeeperException.NoNodeException ignored) {

            } catch (KeeperException | InterruptedException e) {
                System.err.println(String.format("Failed to extract children from node %s. Exception: %s",zooKeeper, e));
            }
        }
        zooKeeper.exists(znodeName, true, this, null);
    }

    @Override
    public void run() {
        watcher = new Watcher(zooKeeper, znodeName);
        zooKeeper.exists(znodeName, true, this, null);
    }

    public void quit() {
        if (child != null) {
            child.destroy();
            child = null;
        }
        System.exit(0);
    }
}
