import Domain.CheckAssignment;
import Domain.Config;
import Domain.Student;
import Domain.TaskDef;
import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;
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
                cfg.currentTask = new TaskDef();
                cfg.currentTask.id = id;
                cfg.tasks.add(cfg.currentTask);
                block.setDelegate(cfg);
                block.setResolveStrategy(Closure.DELEGATE_FIRST);
                block.call();
                return null;
            }
        });

        b.setVariable("group", new Closure<Void>(cfg) {
            public Void doCall(String name, Closure<?> block) {
                cfg.currentGroup = new ArrayList<>();
                cfg.groups.put(name, cfg.currentGroup);
                b.setVariable("student", new Closure<Void>(cfg) {
                    public Void doCall(String nick, String fio, String url) {
                        cfg.currentGroup.add(new Student(nick, fio, url));
                        return null;
                    }
                });
                block.setDelegate(cfg);
                block.setResolveStrategy(Closure.DELEGATE_FIRST);
                block.call();
                b.setVariable("student", null);
                return null;
            }
        });

        b.setVariable("check", new Closure<Void>(cfg) {
            public Void doCall(String grp, String stu, Closure<?> block) {
                CheckAssignment c = new CheckAssignment();
                c.group = grp;
                c.student = stu;
                cfg.checks.add(c);
                b.setVariable("task", new Closure<Void>(cfg) {
                    public Void doCall(String id) {
                        c.taskId = id;
                        return null;
                    }
                });
                block.setDelegate(cfg);
                block.setResolveStrategy(Closure.DELEGATE_FIRST);
                block.call();
                b.setVariable("task", null);
                return null;
            }
        });

        b.setVariable("settings", new Closure<Void>(cfg) {
            public Void doCall(Closure<?> block) {
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
