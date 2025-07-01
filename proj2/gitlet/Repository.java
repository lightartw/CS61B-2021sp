package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;
import java.util.*;
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
    // 核心目录
    public static final File COMMIT_DIR = join(GITLET_DIR, "commits");
    public static final File BRANCH_DIR = join(GITLET_DIR, "branches");
    public static final File BLOB_DIR = join(GITLET_DIR, "blobs");
    // Staging Area 相关
    public static final File STAGING_DIR = join(GITLET_DIR, "staging");
    public static final File ADDITION_DIR = join(STAGING_DIR, "addition");
    public static final File REMOVAL_DIR = join(STAGING_DIR, "removal");
    // HEAD
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");
    public static final File INDEX_FILE = join(GITLET_DIR, "index");

    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        // 创建目录结构
        GITLET_DIR.mkdirs();
        COMMIT_DIR.mkdirs();
        BRANCH_DIR.mkdirs();
        BLOB_DIR.mkdirs();
        // 创建初始提交
        Commit initCommit = new Commit("initial commit", new Date(0), null, null, new HashMap<>());
        String commitHash = Utils.sha1(initCommit);
        File commitFile = join(COMMIT_DIR, commitHash);
        Utils.writeObject(commitFile, initCommit);
        // 创建master分支，指向初始提交
        File masterBranch = join(BRANCH_DIR, "master");
        Utils.writeContents(masterBranch, commitHash);
        Utils.writeContents(HEAD_FILE, "master");
        Utils.writeObject(INDEX_FILE, new StagingArea());
    }

    public static void add(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        byte[] contents = Utils.readContents(file);
        String blobHash = Utils.sha1((Object) contents);
        File blobFile = join(BLOB_DIR, blobHash);
        if (!blobFile.exists()) {
            Utils.writeContents(blobFile, blobHash);
        }

        StagingArea stagingArea = Utils.readObject(INDEX_FILE, StagingArea.class);
        Commit currentCommit = getCurrentCommit();
        String currentVersion = currentCommit.getBlobs().get(fileName);

        if (blobHash.equals(currentVersion)) {
            stagingArea.getAddition().remove(fileName);
        }
        else {
            stagingArea.addFile(fileName, blobHash);
        }

        Utils.writeObject(INDEX_FILE, stagingArea);
    }

    public static void commit(String message) {
        StagingArea stagingArea = Utils.readObject(STAGING_DIR, StagingArea.class);
        Map<String, String> addition = stagingArea.getAddition();
        Set<String> removal = stagingArea.getRemoval();
        if (addition.isEmpty() && removal.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        //创建newCommit，设置parentCommit
        Commit currentCommit = getCurrentCommit();
        String currentHash = Utils.sha1(currentCommit);
        Commit newCommit = new Commit(currentCommit);
        newCommit.setParentCommit(currentHash);
        newCommit.setMessage(message);
        newCommit.setTimestamp(new Date());
        //update infos
        newCommit.update(stagingArea);
        //修改HEAD Branch的commit
        String currentBranch = getCurrentBranch();
        File branchFile = join(BRANCH_DIR, currentBranch);
        String commitHash = Utils.sha1(newCommit);
        Utils.writeContents(branchFile, commitHash);
        //写入commits文件
        File commitFile = join(COMMIT_DIR, commitHash);
        Utils.writeObject(commitFile, newCommit);
        //清空 StagingArea
        Utils.writeObject(INDEX_FILE, new StagingArea());
    }
    // 工具方法
    public static String getCurrentBranch() {
        return Utils.readContentsAsString(HEAD_FILE);
    }

    public static Commit getCurrentCommit() {
        String currentBranch = getCurrentBranch();
        File branchFile = join(BRANCH_DIR, currentBranch);
        String commitHash = Utils.readContentsAsString(branchFile);
        File commitFile = join(COMMIT_DIR, commitHash);
        return Utils.readObject(commitFile, Commit.class);
    }
}
