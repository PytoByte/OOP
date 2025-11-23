import java.util.LinkedList;

public class Gradebook {
    private final LinkedList<Semester> semesters = new LinkedList<>();

    public void addSemester(Semester semester) {
        semesters.add(semester);
    }


}
