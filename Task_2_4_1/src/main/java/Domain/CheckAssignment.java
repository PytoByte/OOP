package Domain;

public record CheckAssignment(Group group, Student student, Task task) {
    @Override
    public String toString() {
        return "CheckAssignment{" +
                "group=" + group +
                ", student=" + student +
                ", task=" + task +
                '}';
    }
}
