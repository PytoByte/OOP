package Domain;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Config {
    private Map<String, Task> tasks = new LinkedHashMap<>();
    private Map<String, List<Student>> groups = new LinkedHashMap<>();
    private List<CheckAssignment> checkAssignments = new LinkedList<>();
    private Settings settings = new Settings();

    private List<Student> currentGroup;

    void setMaxScorePerTask(int v) {
        settings.maxScorePerTask = v;
    }

    void setPassThreshold(int v) {
        settings.passThreshold = v;
    }

    public void addTask(String taskId, Task task) {
        tasks.put(taskId, task);
    }

    public void addCheckAssignment(CheckAssignment checkAssignment) {
        checkAssignments.add(checkAssignment);
    }

    public List<CheckAssignment> getCheckAssignments() {
        return new LinkedList<>(checkAssignments);
    }

    public Task getTask(String taskId) {
        return tasks.get(taskId);
    }
}
