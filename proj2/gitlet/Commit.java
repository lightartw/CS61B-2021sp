package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<Blob> bolbs;

    /** Creat a init commit */
    public Commit() {
        message = "initialCommit";
        timestamp = new Date();
        parentCommit = null;
        secParentCommit = null;
        bolbs = null;
    }
}
