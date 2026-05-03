package Context;

import Services.CheckAssignmentBuilder;

public class CheckContext {
    private final CheckAssignmentBuilder builder;
    private final String groupName;
    private final String studentName;

    public CheckContext(CheckAssignmentBuilder builder, String groupName, String studentName) {
        this.builder = builder;
        this.groupName = groupName;
        this.studentName = studentName;
    }

    public CheckContext(CheckAssignmentBuilder builder, String groupName) {
        this.builder = builder;
        this.groupName = groupName;
        this.studentName = null;
    }

    public void task(String taskId) {
        if (studentName == null) {
            builder.addCheckAssignment(groupName, taskId);
        } else {
            builder.addCheckAssignment(groupName, studentName, taskId);
        }
    }
}