package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.join;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /** The message of this Commit. */
    private String message;
    private Date timestamp;
    private String parentCommit;
    /** if it has merge */
    private String secParentCommit;
    /** All the file it stores. */
    private Map<String, String> blobs;

    /** Creat a init commit */
    public Commit(String message, Date timestamp, String parentCommit, String secParentCommit, Map<String, String> blobs) {
        this.message = message;
        this.timestamp = timestamp;
        this.blobs = new HashMap<>(blobs);
        this.parentCommit = parentCommit;
        this.secParentCommit = secParentCommit;
    }

    public Commit(Commit commit) {
        this.message = commit.message;
        this.timestamp = commit.timestamp;
        this.parentCommit = commit.parentCommit;
        this.secParentCommit = commit.secParentCommit;
        this.blobs = commit.getBlobs();
    }

    public void update(StagingArea stagingArea) {
        Map<String, String> addition = stagingArea.getAddition();
        blobs.putAll(addition);

        Set<String> removal = stagingArea.getRemoval();
        for (String key : removal) {
            blobs.remove(key);
        }
    }

    public Map<String, String> getBlobs() {
        return new HashMap<>(blobs);
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setParentCommit(String parentCommit) {
        this.parentCommit = parentCommit;
    }

    public Commit getParent() {
        if (parentCommit == null) return null;
        File parentFile = join(Repository.COMMIT_DIR, parentCommit);
        return Utils.readObject(parentFile, Commit.class);
    }
}
