package Services;

import Domain.CheckAssignment;
import Domain.Group;
import Domain.Student;
import Domain.Task;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CheckAssignmentBuilder {
    private final Map<String, Task> tasks = new HashMap<>();
    private final Map<String, Group> groups = new HashMap<>();
    private final Set<CheckAssignment> checkAssignments = new HashSet<>();

    public void addTask(Task task) {
        if (tasks.containsKey(task.id())) {
            throw new RuntimeException("Task already exist: " + task.id());
        }
        tasks.put(task.id(), task);
    }

    public void addGroup(Group group) {
        if (groups.containsKey(group.name())) {
            throw new RuntimeException("Group already exist: " + group.name());
        }
        groups.put(group.name(), group);
    }

    public void addCheckAssignment(String groupName, String studentName, String taskId) {
        Group group = groups.get(groupName);
        if (group == null) {
            throw new NullPointerException("Group not found: " + groupName);
        }
        Student student = group.students().get(studentName);
        if (student == null) {
            throw new NullPointerException("Student not found: " + studentName);
        }
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new NullPointerException("Task not found: " + taskId);
        }
        checkAssignments.add(new CheckAssignment(group, student, task));
    }

    public void addCheckAssignment(String groupName, String taskId) {
        Group group = groups.get(groupName);
        if (group == null) {
            throw new NullPointerException("Group not found: " + groupName);
        }
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new NullPointerException("Task not found: " + taskId);
        }
        for (Student student : group.students().values()) {
            checkAssignments.add(new CheckAssignment(group, student, task));
        }
    }

    public List<CheckAssignment> getCheckAssignments() {
        return List.copyOf(checkAssignments);
    }
}
