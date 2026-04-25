import Domain.CheckResult;
import Domain.Config;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File script = new File("oop-checker.groovy");
        if (!script.exists()) {
            System.err.println("Файл oop-checker.groovy не найден!");
            return;
        }

        Config config = DslParser.parse(script);
        List<CheckResult> results = MockExecutor.execute(config);
        ReportGenerator.writeHtml(results);
        ConfigPrinter.print(config);
        System.out.println("Отчёт сохранён в report.html");
    }
}
