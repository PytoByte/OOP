package Context;

import Domain.Group;
import Domain.Student;
import java.util.HashMap;
import java.util.Map;

public class GroupContext {
    private final Map<String, Student> students = new HashMap<>();
    private final String name;

    public GroupContext(String name) {
        this.name = name;
    }

    public void student(String name, String nick) {
        if (students.containsKey(name)) {
            throw new RuntimeException(String.format("Duplicate name \"%s\"", name));
        }
        students.put(name, new Student(name, nick));
    }

    public Group produce() {
        return new Group(name, Map.copyOf(students));
    }
}