package pl.edu.agh.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;
import pl.edu.agh.zk.watchers.Watcher;
import pl.edu.agh.zk.watchers.Callback;

import java.io.IOException;

public class Runner implements org.apache.zookeeper.Watcher {

    private final String hostPort;
    private final String znode;
    private final String exec[];
    private Callback callback;

    public Runner(String hostPort, String znode, String exec[]) {
        this.hostPort = hostPort;
        this.znode = znode;
        this.exec = exec;
        start();
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Please follow the form: Runner <host>:<port> <znode> <program> <argumenty>");
            return;
        }
        String exec[] = new String[args.length - 2];
        System.arraycopy(args, 2, exec, 0, exec.length);
        try {
            new Runner(args[0], args[1], exec);
        } catch (Exception e) {
            System.err.println("Failed to run the app");
            e.printStackTrace();
        }
    }

    public void start() {
        ZooKeeper zooKeeper;
        try {
            zooKeeper = new ZooKeeper(hostPort, 3000, this);
            Watcher watcher = new Watcher(zooKeeper, znode);
            callback = new Callback(zooKeeper, znode, exec);
            new Thread(callback).start();
        } catch (IOException e) {
            System.err.println("Failed to initialize the app");
            e.printStackTrace();
            return;
        }
        TextFront textFront = new TextFront(zooKeeper, znode, callback);
        new Thread(textFront).start();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() != Event.EventType.None) {
            switch (watchedEvent.getState()) {
                case SyncConnected:
                    break;
                case Expired:
                    callback.quit();
                    break;
            }
        }
    }
}
