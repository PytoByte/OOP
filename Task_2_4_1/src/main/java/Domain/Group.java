package Domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class Group {
    private final String name;
    private final Map<String, Student> students = new LinkedHashMap<>();

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addStudent(Student student) {
        students.put(student.name(), student);
    }

    public Student getStudent(String name) {
        return students.get(name);
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", students=" + students +
                '}';
    }
}
