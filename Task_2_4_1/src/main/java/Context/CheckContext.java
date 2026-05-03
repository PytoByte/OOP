package Context;

import Services.CheckAssignmentBuilder;

public class CheckContext {
    private final CheckAssignmentBuilder generator;
    private final String groupName;
    private final String studentName;

    public CheckContext(CheckAssignmentBuilder generator, String groupName, String studentName) {
        this.generator = generator;
        this.groupName = groupName;
        this.studentName = studentName;
    }

    public CheckContext(CheckAssignmentBuilder generator, String groupName) {
        this.generator = generator;
        this.groupName = groupName;
        this.studentName = null;
    }

    public void task(String taskId) {
        if (studentName == null) {
            generator.addCheckAssignment(groupName, taskId);
        } else {
            generator.addCheckAssignment(groupName, studentName, taskId);
        }
    }
}