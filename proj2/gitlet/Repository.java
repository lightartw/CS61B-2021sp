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
        isInitialized();

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
            stagingArea.stageForAddition(fileName, blobHash);
        }

        Utils.writeObject(INDEX_FILE, stagingArea);
    }

    public static void commit(String message) {
        isInitialized();

        StagingArea stagingArea = Utils.readObject(INDEX_FILE, StagingArea.class);
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
        //TODO 日期应该输入实际时间
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
    /** DANGEROUS!!! */
    public static void rm(String fileName) {
        isInitialized();

        StagingArea stagingArea = Utils.readObject(INDEX_FILE, StagingArea.class);
        Map<String, String> addition = stagingArea.getAddition();
        Commit currentCommit = getCurrentCommit();
        Map<String, String> blobs = currentCommit.getBlobs();

        if (!blobs.containsKey(fileName) && !addition.containsKey(fileName)) {
            System.out.println("No reason to remove the file.");
            return; //直接返回，不需要exit
        }
        // 从staging area的addition中移除（如果存在）
        addition.remove(fileName);
        // 如果文件在当前commit中存在，标记为删除
        if (blobs.containsKey(fileName)) {
            stagingArea.stageForRemoval(fileName);
            // 从工作目录删除文件（如果用户还没删除）
            File file = join(CWD, fileName);
            if (file.exists()) {
                Utils.restrictedDelete(file); // 使用Utils提供的安全删除方法
            }
        }
        // 保存更新后的staging area
        Utils.writeObject(INDEX_FILE, stagingArea);
    }

    public static void log() {
        isInitialized();

        Commit currentCommit = getCurrentCommit();
        while (currentCommit != null) {
            System.out.println(currentCommit);
            currentCommit = currentCommit.getParent();
        }
    }

    public static void globalLog() {
        isInitialized();
        //获得所有commit
        List<String> commitFiles = Utils.plainFilenamesIn(COMMIT_DIR);
        if (commitFiles == null) {
            return;
        }
        for (String commitHash : commitFiles) {
            File commitFile = join(COMMIT_DIR, commitHash);
            Commit commit = Utils.readObject(commitFile, Commit.class);
            System.out.println(commit);
        }
    }

    public static void find(String commitMessage) {
        isInitialized();
        //获得所有commit
        List<String> commitFiles = Utils.plainFilenamesIn(COMMIT_DIR);

        boolean found = false;
        for (String commitHash : commitFiles) {
            File commitFile = join(COMMIT_DIR, commitHash);
            Commit commit = Utils.readObject(commitFile, Commit.class);
            if (commitMessage.equals(commit.getMessage())) {
                found = true;
                System.out.println(commit.getHash());
            }
        }
        if(!found) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void status() {
        isInitialized();

        List<String> branches = Utils.plainFilenamesIn(BRANCH_DIR);
        String currentBranch = getCurrentBranch();
        StagingArea stagingArea = Utils.readObject(INDEX_FILE, StagingArea.class);
        //打印branches
        System.out.println("=== Branches ===");
        Collections.sort(branches);
        for (String branch : branches) {
            if (branch.equals(currentBranch)) {
                System.out.println("*" + branch);
            }
            else {
                System.out.println(branch);
            }
        }
        System.out.println();
        //打印Staged Files与Removed Files
        System.out.println("=== Staged Files ===");
        List<String> stagedFiles = new ArrayList<>(stagingArea.getAddition().keySet());
        Collections.sort(stagedFiles);
        for (String file : stagedFiles) {
            System.out.println(file);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        List<String> removedFiles = new ArrayList<>(stagingArea.getRemoval());
        Collections.sort(removedFiles);
        for (String file : removedFiles) {
            System.out.println(file);
        }
        System.out.println();

        //TODO Modifications Not Staged For Commit + Untracked Files
    }
    /** Branch家族 */
    public static void checkout(String[] args) {
        isInitialized();
        //替换这个commit
        if (args.length == 2) {
            checkoutBranch(args[1]);
        }
        else if (args.length == 3) { // 替换单个file
            Commit currentCommit = getCurrentCommit();
            String fileName = args[2];
            //ERROR
            if (!currentCommit.getBlobs().containsKey(fileName)) {
                System.out.println("File does not exist in that commit.");
                return;
            }
            Map<String, String> blobs = currentCommit.getBlobs();
            //写入
            String blobHash = blobs.get(fileName);
            restoreToWorkingDirectory(fileName, blobHash);
        }
        else if (args.length == 4){
            String commitId = args[1];
            String commitHash = findFullCommitId(commitId);
            String fileName = args[3];
            File commitFile = join(COMMIT_DIR, commitHash);
            // ERROR
            if (!commitFile.exists()) {
                System.out.println("No commit with that id exists.");
                return;
            }
            Commit commit = Utils.readObject(commitFile, Commit.class);
            Map<String, String> blobs = commit.getBlobs();
            if (!blobs.containsKey(fileName)) {
                System.out.println("File does not exist in that commit.");
                return;
            }
            //写入工作目录
            String blobHash = blobs.get(fileName);
            restoreToWorkingDirectory(fileName, blobHash);
        }
    }

    public static void checkoutBranch(String branchName) {
        File branchFile = join(BRANCH_DIR, branchName);
        //ERROR
        if (!branchFile.exists()) {
            System.out.println("No such branch exists.");
            return;
        }
        if (branchName.equals(getCurrentBranch())) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        //获取目标commit
        String commitHash = Utils.readContentsAsString(branchFile);
        Commit commit = Utils.readObject(join(COMMIT_DIR, commitHash), Commit.class);

        checkUntrackedFiles(commit);
        updateWorkingDirectory(commit);
        //切换HEAD
        Utils.writeContents(HEAD_FILE, branchFile);
        //清空stagingArea
        Utils.writeObject(INDEX_FILE, new StagingArea());
    }

    public static void restoreToWorkingDirectory(String fileName, String blobHash) {
        File blobFile = join(BLOB_DIR, blobHash);
        byte[] content = Utils.readContents(blobFile);
        File workingFile = join(CWD, fileName);
        Utils.writeContents(workingFile, (Object) content);
    }

    public static String findFullCommitId (String shortId) {
        List<String> allCommits = Utils.plainFilenamesIn(COMMIT_DIR);
        if (allCommits == null) return null;
        if (shortId.length() == UID_LENGTH && allCommits.contains(shortId)) {
            return shortId;
        }
        //查找匹配的缩写
        List<String> matches = new ArrayList<>();
        for (String commitHash : allCommits) {
            if (commitHash.startsWith(shortId)) {
                matches.add(commitHash);
            }
        }
        if (matches.size() != 1) {
            return null;
        }
        return matches.get(0);
    }

    public static void checkUntrackedFiles(Commit commit) {
        Commit currentCommit = getCurrentCommit();
        StagingArea stagingArea = Utils.readObject(INDEX_FILE, StagingArea.class);

        Map<String, String> currentBlobs = currentCommit.getBlobs();
        Map<String, String> commitBlobs = commit.getBlobs();
        Map<String, String> addition = stagingArea.getAddition();
        //获取工作目录所有文件
        List<String> workingFiles = Utils.plainFilenamesIn(CWD);
        if (workingFiles != null) {
            for (String fileName : workingFiles) {
                //文件未被追踪
                if (!currentBlobs.containsKey(fileName) && !addition.containsKey(fileName)) {
                    if (commitBlobs.containsKey(fileName)) {
                        System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                        System.exit(0);
                    }
                }
            }
        }
    }

    public static void updateWorkingDirectory(Commit commit) {
        Commit currentCommit = getCurrentCommit();

        Map<String, String> currentBlobs = currentCommit.getBlobs();
        Map<String, String> commitBlobs = commit.getBlobs();
        //删除当前commit存在但是目标commit中不存在的文件
        for (String fileName : currentBlobs.keySet()) {
            if (!commitBlobs.containsKey(fileName)) {
                File file = join(CWD, fileName);
                if (file.exists()) {
                    Utils.restrictedDelete(file);
                }
            }
        }
        //覆写或者添加新文件
        for (Map.Entry<String, String> entry : commitBlobs.entrySet()) {
            String fileName = entry.getKey();
            String blobHash = entry.getValue();
            //读取文件内容
            File blobFile = join(BLOB_DIR, blobHash);
            byte[] content = Utils.readContents(blobFile);
            //写入工作目录
            File workingFile = join(CWD, fileName);
            Utils.writeContents(workingFile, (Object) content);
        }
    }

    // 工具方法
    public static void isInitialized() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

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
