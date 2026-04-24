import Domain.CheckAssignment;
import Domain.CheckResult;
import Domain.Config;

import java.util.ArrayList;
import java.util.List;

public class MockExecutor {
    public static List<CheckResult> execute(Config cfg) {
        List<CheckResult> out = new ArrayList<>();
        for (CheckAssignment c : cfg.checks) {
            CheckResult r = new CheckResult();
            r.group = c.group;
            r.student = c.student;
            r.taskId = c.taskId;
            r.title = cfg.tasks.stream().filter(t -> t.id.equals(c.taskId)).map(t -> t.title).findFirst().orElse("N/A");
            // Здесь будет логика: git clone -> javac -> checkstyle -> junit
            r.build = true;
            r.doc = true;
            r.style = true;
            r.p = 5;
            r.f = 0;
            r.s = 0;
            r.total = 8; // мок-баллы
            out.add(r);
        }
        return out;
    }
}