package pl.edu.agh.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import pl.edu.agh.zk.watchers.Callback;

import java.util.List;
import java.util.Scanner;

public class TextFront implements Runnable {

    public static final String HELP_MSG =  String.format("%s\n%s\n%s\n%s\n"
            , "Commands:"
            , "s - show the structure"
            , "q - quit the program"
            , "help - display help"
            );

    private final ZooKeeper zooKeeper;
    private final String znode;
    private final Callback callback;

    public TextFront(ZooKeeper zooKeeper, String znode, Callback callback) {
        this.zooKeeper = zooKeeper;
        this.znode = znode;
        this.callback = callback;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            System.err.println("Interrupted exception occurred");
            e.printStackTrace();
        }
        printPrompt();
        String line = scanner.nextLine();
        while (!line.equals("q")) {
            printPrompt();
            switch (line) {
                case "help":
                    System.out.print(HELP_MSG);
                    break;
                case "s":
                    try {
                        showStructure(znode);
                    } catch (KeeperException.NoNodeException ignored) {

                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case "q":
                    callback.quit();
                    break;
                default:
                    System.out.println("Write \"help\" to get the list of commands");
                    break;
            }
            line = scanner.nextLine();
        }
    }

    private void printPrompt() {
        System.out.print("> ");
    }

    private void showStructure(String znode) throws KeeperException, InterruptedException {
        showStructure(znode, 0);
    }

    private void showStructure(String path, int indent) throws KeeperException, InterruptedException {
        List<String> children;

        try {
            children = zooKeeper.getChildren(path, false);
        } catch (KeeperException.NoNodeException ignored) {
            System.err.println("No node named " + znode);
            return;
        } catch (KeeperException e) {
            e.printStackTrace();
            return;
        }

        for (int i = 0;i < indent; i++) {
            System.out.print("    ");
        }

        System.out.println(path);

        for (String child : children) {
            showStructure(String.format("%s/%s",path,child), indent + 1);
        }
    }
}
