package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
        this.blobs = blobs;
        this.parentCommit = parentCommit;
        this.secParentCommit = secParentCommit;
    }

    public void update(StagingArea stagingArea) {
        Map<String, String> addition = stagingArea.getAddition();
        blobs.putAll(addition);

        Set<String> removal = stagingArea.getRemoval();
        for (String key : removal) {
            blobs.remove(key);
        }
    }

    public String getFormattedTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return "Date: " + formatter.format(timestamp);
    }

    @Override
    public String toString() {
        if (this.secParentCommit == null) {
            return "===\n" +
                    "commit " + getHash() + "\n" +
                    getFormattedTimestamp() + "\n" +
                    message + "\n";
        }

        return  "===\n" +
                "commit " + getHash() + "\n" +
                "Merge: "  + parentCommit.substring(0,7) + " " + secParentCommit.substring(0,7) +"\n" +
                getFormattedTimestamp() + "\n" +
                message + "\n";
    }

    public Map<String, String> getBlobs() {
        return blobs;
    }

    public String getMessage() {
        return message;
    }
    public String getHash() {
        return Utils.sha1((Object) Utils.serialize(this));
    }
    public Commit getParent() {
        if (parentCommit == null) return null;
        File parentFile = join(Repository.COMMIT_DIR, parentCommit);
        return Utils.readObject(parentFile, Commit.class);
    }
    public Commit getSecParent() {
        if (secParentCommit == null) return null;
        File parentFile = join(Repository.COMMIT_DIR, secParentCommit);
        return Utils.readObject(parentFile, Commit.class);
    }
}
