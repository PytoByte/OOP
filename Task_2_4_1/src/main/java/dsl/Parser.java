package dsl;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.IOException;
import java.util.List;
import model.CheckAssignment;
import org.codehaus.groovy.control.CompilerConfiguration;
import services.CheckAssignmentBuilder;

/**
 * Parses Groovy DSL configuration files into CheckAssignment models.
 */
public class Parser {

    /**
     * Parses a configuration file using the provided builder.
     *
     * @param file the configuration file to parse
     * @param builder the builder instance for constructing the model
     * @return list of CheckAssignment objects built from the script
     * @throws RuntimeException if an I/O error occurs during script evaluation
     */
    public static List<CheckAssignment> parse(File file, CheckAssignmentBuilder builder) {
        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(BaseConfigScript.class.getName());

        Binding b = new Binding();
        b.setVariable("builder", builder);

        GroovyShell shell = new GroovyShell(b, config);
        try {
            shell.evaluate(file);
        } catch (IOException e) {
            throw new RuntimeException("Script error", e);
        }
        return builder.getCheckAssignments();
    }

    /**
     * Parses a configuration file with a new builder instance.
     *
     * @param file the configuration file to parse
     * @return list of CheckAssignment objects built from the script
     */
    public static List<CheckAssignment> parse(File file) {
        return parse(file, new CheckAssignmentBuilder());
    }
}