import Domain.Config;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.IOException;
import org.codehaus.groovy.control.CompilerConfiguration;

public class DslParser {
    public static Config parse(File file, Config cfg) {
        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(BaseConfigScript.class.getName());

        Binding b = new Binding();
        b.setVariable("cfg", cfg);

        GroovyShell shell = new GroovyShell(b, config);
        try {
            shell.evaluate(file);
        } catch (IOException e) {
            throw new RuntimeException("Script error", e);
        }
        return cfg;
    }

    public static Config parse(File file) {
        return parse(file, new Config());
    }
}
