package Dsl;

import Domain.CheckAssignment;
import Services.CheckAssignmentBuilder;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.groovy.control.CompilerConfiguration;

public class Parser {
    public static List<CheckAssignment> parse(File file, CheckAssignmentBuilder builder) {
        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(BaseConfigScript.class.getName());

        Binding b = new Binding();
        b.setVariable("generator", builder);

        GroovyShell shell = new GroovyShell(b, config);
        try {
            shell.evaluate(file);
        } catch (IOException e) {
            throw new RuntimeException("Script error", e);
        }
        return builder.getCheckAssignments();
    }

    public static List<CheckAssignment> parse(File file) {
        return parse(file, new CheckAssignmentBuilder());
    }
}
