package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMIT_DIR = join(GITLET_DIR, "commits");
    public static final File BRANCH_DIR = join(GITLET_DIR, "branches");
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");

    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        //创建目录结构
        GITLET_DIR.mkdirs();
        COMMIT_DIR.mkdirs();
        BRANCH_DIR.mkdirs();

        // 创建初始提交
        Commit initCommit = new Commit();
        String commitHash = Utils.sha1(initCommit);

        // 保存初始提交
        File commitFile = join(COMMIT_DIR, commitHash);
        Utils.writeObject(commitFile, initCommit);
        // 创建master分支，指向初始提交
        File masterBranch = join(BRANCH_DIR, "master");
        Utils.writeContents(masterBranch, commitHash);

        // 设置HEAD指向master分支
        Utils.writeContents(HEAD_FILE, "master");
    }

    public static void add(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

    }

    // 工具方法：获取当前分支名
    public static String getCurrentBranch() {
        return Utils.readContentsAsString(HEAD_FILE);
    }
}
