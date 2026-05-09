package context;

import java.util.HashMap;
import java.util.Map;
import model.Group;
import model.Student;

/**
 * Context for "group" function in gradle.
 */
public class GroupContext {
    private final Map<String, Student> students = new HashMap<>();
    private final String name;

    /**
     * Default constructor.
     *
     * @param name unique group name
     */
    public GroupContext(String name) {
        this.name = name;
    }

    /**
     * Add student to the group.
     *
     * @param name real name
     * @param nick GitHub nick
     */
    public void student(String name, String nick) {
        if (students.containsKey(name)) {
            throw new RuntimeException(String.format("Duplicate name \"%s\"", name));
        }
        students.put(name, new Student(name, nick));
    }

    /**
     * Produce group model.
     *
     * @return group model
     */
    public Group produce() {
        return new Group(name, Map.copyOf(students));
    }
}