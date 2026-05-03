package dsl;

import context.CheckContext;
import context.GroupContext;
import context.TaskContext;
import groovy.lang.Closure;
import groovy.lang.Script;
import java.io.File;
import services.CheckAssignmentBuilder;

/**
 * Base script class for the custom Gradle DSL configuration.
 */
public abstract class BaseConfigScript extends Script {
    /**
     * Retrieves the CheckAssignmentBuilder instance from the script binding.
     *
     * @return the builder used to assemble the configuration model
     */
    private CheckAssignmentBuilder builder() {
        return (CheckAssignmentBuilder) getBinding().getVariable("builder");
    }

    /**
     * Imports and parses an external configuration script.
     *
     * @param name the path to the configuration file, for example script/tasks_base.groovy
     */
    public void importConfig(String name) {
        Parser.parse(new File(name), builder());
    }

    /**
     * Defines a task.
     *
     * @param id the unique identifier of the task
     * @param block the configuration closure for the task
     */
    public void task(String id, Closure<?> block) {
        TaskContext context = new TaskContext(id);
        executeInContext(context, block);
        builder().addTask(context.produce());
    }

    /**
     * Defines a group of students.
     *
     * @param name the name of the group
     * @param block the configuration closure for the group
     */
    public void group(String name, Closure<?> block) {
        GroupContext context = new GroupContext(name);
        executeInContext(context, block);
        builder().addGroup(context.produce());
    }

    /**
     * Initiates a check session for a specific student.
     *
     * @param groupName the name of the student's group
     * @param studentName the name of the student
     * @param block the configuration closure containing task selection
     */
    public void check(String groupName, String studentName, Closure<?> block) {
        executeInContext(new CheckContext(builder(), groupName, studentName), block);
    }

    /**
     * Initiates a check session for all students in the group.
     *
     * @param groupName the name of the group
     * @param block the configuration closure containing task selection
     */
    public void check(String groupName, Closure<?> block) {
        executeInContext(new CheckContext(builder(), groupName), block);
    }

    /**
     * Executes a closure within the scope of a given context object.
     *
     * @param context the object to act as the closure delegate
     * @param block the closure to execute
     */
    private void executeInContext(Object context, Closure<?> block) {
        block.setDelegate(context);
        block.setResolveStrategy(Closure.DELEGATE_ONLY);
        block.call();
    }
}