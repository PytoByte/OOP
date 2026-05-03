package Context;

import Services.CheckAssignmentBuilder;

/**
 * Context for "check" function in gradle.
 */
public class CheckContext {
    private final CheckAssignmentBuilder builder;
    private final String groupName;
    private final String studentName;

    /**
     * Check for specific student.
     *
     * @param builder builder of check assignments
     * @param groupName existing group name
     * @param studentName existing student name
     */
    public CheckContext(CheckAssignmentBuilder builder, String groupName, String studentName) {
        this.builder = builder;
        this.groupName = groupName;
        this.studentName = studentName;
    }

    /**
     * Check for all students in group.
     *
     * @param builder builder of check assignments
     * @param groupName existing group name
     */
    public CheckContext(CheckAssignmentBuilder builder, String groupName) {
        this.builder = builder;
        this.groupName = groupName;
        this.studentName = null;
    }

    /**
     * Specifies a task to be checked.
     *
     * @param taskId the ID of the task
     */
    public void task(String taskId) {
        if (studentName == null) {
            builder.buildCheckAssignments(groupName, taskId);
        } else {
            builder.buildCheckAssignment(groupName, studentName, taskId);
        }
    }
}