package Domain;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Config {
    private Map<String, Task> tasks = new LinkedHashMap<>();
    private Map<String, Group> groups = new LinkedHashMap<>();
    private List<CheckAssignment> checkAssignments = new LinkedList<>();

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void addGroup(Group group) {
        groups.put(group.getName(), group);
    }

    public void addCheckAssignment(String groupName, String studentName, String taskId) {
        Group group = groups.get(groupName);
        if (group == null) {
            throw new NullPointerException("Group not found: " + groupName);
        }
        Student student = group.getStudent(studentName);
        if (student == null) {
            throw new NullPointerException("Student not found: " + studentName);
        }
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new NullPointerException("Task not found: " + taskId);
        }
        checkAssignments.add(new CheckAssignment(group, student, task));
    }

    public List<CheckAssignment> getCheckAssignments() {
        return new LinkedList<>(checkAssignments);
    }

    public Task getTask(String taskId) {
        return tasks.get(taskId);
    }

    public Group getGroup(String name) {
        return groups.get(name);
    }

    @Override
    public String toString() {
        return "Config{" +
                "tasks=" + tasks +
                ", groups=" + groups +
                ", checkAssignments=" + checkAssignments +
                '}';
    }
}
