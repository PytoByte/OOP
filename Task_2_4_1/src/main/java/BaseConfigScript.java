import Context.CheckContext;
import Context.GroupContext;
import Context.TaskContext;
import Domain.Config;
import Domain.Group;
import Domain.Task;
import groovy.lang.Closure;
import groovy.lang.Script;

import java.io.File;

public abstract class BaseConfigScript extends Script {
    private Config cfg() {
        return (Config) getBinding().getVariable("cfg");
    }

    public void importConfig(String name) {
        DslParser.parse(new File(name), cfg());
    }

    public void task(String id, Closure<?> block) {
        Task task = new Task(id);
        TaskContext context = new TaskContext(task);

        block.setDelegate(context);
        block.setResolveStrategy(Closure.DELEGATE_ONLY);
        block.call();

        if (context.title != null) {
            task.setTitle(context.title);
        }
        task.setMaxPoints(context.maxPoints);

        cfg().addTask(task);
    }

    public void group(String name, Closure<?> block) {
        Group group = new Group(name);
        cfg().addGroup(group);
        executeInContext(new GroupContext(group), block);
    }

    public void check(String groupName, String studentName, Closure<?> block) {
        executeInContext(new CheckContext(cfg(), groupName, studentName), block);
    }

    private void executeInContext(Object context, Closure<?> block) {
        block.setDelegate(context);
        block.setResolveStrategy(Closure.DELEGATE_ONLY);
        block.call();
    }
}