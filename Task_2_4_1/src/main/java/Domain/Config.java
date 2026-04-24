package Domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public List<TaskDef> tasks = new ArrayList<>();
    public Map<String, List<Student>> groups = new LinkedHashMap<>();
    public List<CheckAssignment> checks = new ArrayList<>();
    Settings settings = new Settings();

    public TaskDef currentTask;
    public List<Student> currentGroup;

    void setTitle(String v) {
        if (currentTask != null) currentTask.title = v;
    }

    void setMaxPoints(int v) {
        if (currentTask != null) currentTask.maxPoints = v;
    }

    void setMaxScorePerTask(int v) {
        settings.maxScorePerTask = v;
    }

    void setPassThreshold(int v) {
        settings.passThreshold = v;
    }
}
