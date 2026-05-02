package Context;

import Domain.Config;

public class CheckContext {
    private final Config cfg;
    private final String groupName;
    private final String studentName;

    public CheckContext(Config cfg, String groupName, String studentName) {
        this.cfg = cfg;
        this.groupName = groupName;
        this.studentName = studentName;
    }

    public void task(String taskId) {
        cfg.addCheckAssignment(groupName, studentName, taskId);
    }
}