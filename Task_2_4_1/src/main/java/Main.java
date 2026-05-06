import dsl.Parser;
import java.io.File;
import java.util.List;
import model.CheckAssignment;
import model.CheckResult;
import services.Executor;
import services.RealCommandExecutor;
import services.ReportGenerator;

/**
 * Main class.
 */
public class Main {
    private static final String DEFAULT_SCRIPT_PATH = "./script/main.groovy";

    /**
     * Main method.
     *
     * @param args command-line arguments:
     *             [0] = path to script file (default: ./script/main.groovy)
     */
    public static void main(String[] args) {
        String scriptPath = (args.length > 0) ? args[0] : DEFAULT_SCRIPT_PATH;
        File script = new File(scriptPath);

        if (!script.exists()) {
            System.err.println("File \"" + scriptPath + "\" not found!");
            System.err.println("Usage: java Main [path/to/script.groovy]");
            return;
        }

        List<CheckAssignment> checkAssignments = Parser.parse(script);

        Executor executor = new Executor(new RealCommandExecutor());
        List<CheckResult> results = executor.execute(checkAssignments);

        File reportFile = new File("report.html");
        ReportGenerator.writeHtml(results, reportFile);

        System.out.println("Report saved at: " + reportFile.getAbsolutePath());
    }
}
