package Dsl;

import Context.CheckContext;
import Context.GroupContext;
import Context.TaskContext;
import Services.CheckAssignmentBuilder;
import groovy.lang.Closure;
import groovy.lang.Script;

import java.io.File;

public abstract class BaseConfigScript extends Script {
    private CheckAssignmentBuilder builder() {
        return (CheckAssignmentBuilder) getBinding().getVariable("builder");
    }

    public void importConfig(String name) {
        Parser.parse(new File(name), builder());
    }

    public void task(String id, Closure<?> block) {
        TaskContext context = new TaskContext(id);
        executeInContext(context, block);
        builder().addTask(context.produce());
    }

    public void group(String name, Closure<?> block) {
        GroupContext context = new GroupContext(name);
        executeInContext(context, block);
        builder().addGroup(context.produce());
    }

    public void check(String groupName, String studentName, Closure<?> block) {
        executeInContext(new CheckContext(builder(), groupName, studentName), block);
    }

    private void executeInContext(Object context, Closure<?> block) {
        block.setDelegate(context);
        block.setResolveStrategy(Closure.DELEGATE_ONLY);
        block.call();
    }
}