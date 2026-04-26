import Domain.CheckAssignment;
import Domain.Checkpoint;
import Domain.Config;
import Domain.Group;
import Domain.Student;
import Domain.Task;
import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DslParser {
    public static Config parse(File file) {
        return parse(file, new Config());
    }

    public static Config parse(File file, Config cfg) {
        Binding b = new Binding();

        b.setVariable("importConfig", new Closure<Void>(cfg) {
            public Void doCall(String name) {
                parse(new File(name), cfg);
                return null;
            }
        });

        b.setVariable("task", new Closure<Void>(cfg) {
            public Void doCall(String id, Closure<?> block) {
                Task task = new Task(id);
                cfg.addTask(task);
                b.setVariable("checkpoint", new Closure<Void>(task) {
                    public Void doCall(String name, String formattedDate) {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            task.addCheckpoint(new Checkpoint(
                                    name,
                                    LocalDate.parse(formattedDate, formatter))
                            );
                        } catch (Exception e) {
                            throw new RuntimeException(
                                    "Ошибка даты '" + formattedDate + "': нужен формат ДД.ММ.ГГГГ",
                                    e
                            );
                        }
                        return null;
                    }
                });
                block.setDelegate(task);
                block.setResolveStrategy(Closure.DELEGATE_FIRST);
                block.call();
                return null;
            }
        });

        b.setVariable("group", new Closure<Void>(cfg) {
            public Void doCall(String name, Closure<?> block) {
                Group group = new Group(name);
                cfg.addGroup(group);
                b.setVariable("student", new Closure<Void>(cfg) {
                    public Void doCall(String name, String nick, String url) {
                        group.addStudent(new Student(name, nick, url));
                        return null;
                    }
                });
                block.setDelegate(group);
                block.setResolveStrategy(Closure.DELEGATE_FIRST);
                block.call();
                return null;
            }
        });

        b.setVariable("check", new Closure<Void>(cfg) {
            public Void doCall(String groupName, String studentName, Closure<?> block) {
                b.setVariable("task", new Closure<Void>(cfg) {
                    public Void doCall(String taskId) {
                        cfg.addCheckAssignment(groupName, studentName, taskId);
                        return null;
                    }
                });
                block.setDelegate(cfg);
                block.setResolveStrategy(Closure.DELEGATE_FIRST);
                block.call();
                return null;
            }
        });

        try {
            new GroovyShell(b).evaluate(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cfg;
    }
}
