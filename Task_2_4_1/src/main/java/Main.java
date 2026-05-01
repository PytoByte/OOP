import Domain.CheckResult;
import Domain.Config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File script = new File("./script/main.groovy");
        if (!script.exists()) {
            System.err.println("Файл ./script/main.groovy не найден!");
            return;
        }

        Config config = DslParser.parse(script);
        List<CheckResult> results;
        try {
            Path workDir = Files.createTempDirectory("oop-checker-");
            results = Executor.execute(config, workDir);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        ReportGenerator.writeHtml(results);
        System.out.println(config);
        System.out.println("Отчёт сохранён в report.html");
    }
}
