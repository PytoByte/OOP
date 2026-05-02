package Context;

import Domain.Group;
import Domain.Student;

public class GroupContext {
    private final Group group;

    public GroupContext(Group group) {
        this.group = group;
    }

    public void student(String name, String nick, String url) {
        group.addStudent(new Student(name, nick, url));
    }
}