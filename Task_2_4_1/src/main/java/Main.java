import Model.CheckAssignment;
import Model.CheckResult;
import Dsl.Parser;
import Services.Executor;
import Services.RealCommandExecutor;
import Services.ReportGenerator;

import java.io.File;
import java.util.List;

/**
 * Main class.
 */
public class Main {
    /**
     * Main method.
     *
     * @param args any args
     */
    public static void main(String[] args) {
        File script = new File("./script/main.groovy");
        if (!script.exists()) {
            System.err.println("File \"./script/main.groovy\" not found!");
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