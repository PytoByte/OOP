package Services;

import Model.CheckAssignment;
import Model.Group;
import Model.Student;
import Model.Task;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Builder of check assignments.
 */
public class CheckAssignmentBuilder {
    private final Map<String, Task> tasks = new HashMap<>();
    private final Map<String, Group> groups = new HashMap<>();
    private final Set<CheckAssignment> checkAssignments = new HashSet<>();

    /**
     * Add task for future check assignment build.
     *
     * @param task new task with unique task id
     * @throws RuntimeException if task id is not unique
     */
    public void addTask(Task task) {
        if (tasks.containsKey(task.id())) {
            throw new RuntimeException("Task already exist: " + task.id());
        }
        tasks.put(task.id(), task);
    }

    /**
     * Add group for future check assignment build.
     *
     * @param group new group with unique name
     * @throws RuntimeException if group name is not unique
     */
    public void addGroup(Group group) {
        if (groups.containsKey(group.name())) {
            throw new RuntimeException("Group already exist: " + group.name());
        }
        groups.put(group.name(), group);
    }

    /**
     * Build check assignment and store it in class.
     *
     * @param groupName name of added group
     * @param studentName name of student in group
     * @param taskId id of added task
     * @throws NullPointerException if group, student or task not found
     */
    public void buildCheckAssignment(String groupName, String studentName, String taskId) {
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

    /**
     * Build check assignments for all students in group and store it in class.
     *
     * @param groupName name of added group
     * @param taskId id of added task
     * @throws NullPointerException if group or task not found
     */
    public void buildCheckAssignments(String groupName, String taskId) {
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

    /**
     * Get unmodifiable list of check assignments.
     *
     * @return list of check assignments
     */
    public List<CheckAssignment> getCheckAssignments() {
        return List.copyOf(checkAssignments);
    }
}
