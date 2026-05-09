package dsl;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import model.CheckAssignment;
import org.codehaus.groovy.control.CompilationFailedException;
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
     * @throws IOException if can't access the script file
     * @throws FileNotFoundException if file not fount
     * @throws CompilationFailedException if script compilation failed
     */
    public static List<CheckAssignment> parse(File file, CheckAssignmentBuilder builder)
            throws IOException, CompilationFailedException {
        if (!file.exists()) {
            throw new FileNotFoundException("Script file not found " + file.getAbsolutePath());
        }

        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(BaseConfigScript.class.getName());

        Binding b = new Binding();
        b.setVariable("builder", builder);

        GroovyShell shell = new GroovyShell(b, config);
        shell.evaluate(file);
        return builder.getCheckAssignments();
    }

    /**
     * Parses a configuration file with a new builder instance.
     *
     * @param file the configuration file to parse
     * @return list of CheckAssignment objects built from the script
     * @throws IOException if file not found
     * @throws CompilationFailedException if script compilation failed
     */
    public static List<CheckAssignment> parse(File file)
            throws IOException, CompilationFailedException {
        return parse(file, new CheckAssignmentBuilder());
    }
}