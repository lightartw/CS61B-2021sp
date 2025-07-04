package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StagingArea implements Serializable {
    private HashMap<String, String> addition;  // 文件名-->blobHash
    private HashSet<String> removal;     //待删除文件名

    public StagingArea() {
        addition = new HashMap<>();
        removal = new HashSet<>();
    }

    public Map<String, String>  getAddition() {
        return addition;
    }
    public Set<String> getRemoval() {
        return removal;
    }

    public void stageForAddition(String fileName, String blobHash) {
        addition.put(fileName, blobHash);
    }

    public void stageForRemoval(String fileName) {
        removal.add(fileName);
    }
}
