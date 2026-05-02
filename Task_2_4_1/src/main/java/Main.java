import Domain.CheckResult;
import Domain.Config;
import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File script = new File("./script/main.groovy");
        if (!script.exists()) {
            System.err.println("Файл ./script/main.groovy не найден!");
            return;
        }

        Config config = DslParser.parse(script);

        List<CheckResult> results = Executor.execute(config);

        ReportGenerator.writeHtml(results);
        System.out.println("Отчёт сохранён в report.html");
    }
}