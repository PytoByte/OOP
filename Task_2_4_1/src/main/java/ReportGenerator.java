import Domain.CheckResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class ReportGenerator {
    public static void writeHtml(List<CheckResult> result) {
        try (PrintWriter out = new PrintWriter("report.html", StandardCharsets.UTF_8)) {
            out.println("<html><head><meta charset='UTF-8'></head><body>");
            out.println("<table>");
            out.println("<tr>" +
                    "<th>Студент</th>" +
                    "<th>Задача</th>" +
                    "<th>Чекпоинты</th>" +
                    "<th>Клонирование</th>" +
                    "<th>Сборка</th>" +
                    "<th>Документация</th>" +
                    "<th>Стиль кода</th>" +
                    "<th>Тесты</th>" +
                    "<th>Балл</th>" +
                    "</tr>");
            for (CheckResult checkResult : result) {
                out.printf("<tr>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>%d/%d</td>" +
                                "<td>%d</td>" +
                                "</tr>\n",
                        checkResult.checkAssignment().student().name(),
                        checkResult.checkAssignment().task().getId(),
                        checkResult.checkAssignment().task().getCheckpoints().stream().map(
                                checkpoint -> checkpoint.name() + " " + checkpoint.date().toString()
                        ).collect(Collectors.joining(",<br>")),
                        checkResult.download() ? "OK" : "FAIL",
                        checkResult.build() ? "OK" : "FAIL",
                        checkResult.doc() ? "OK" : "FAIL",
                        checkResult.style() ? "OK" : "FAIL",
                        checkResult.passedTestsCount(), checkResult.failedTestsCount(),
                        checkResult.points());
            }

            out.println("</table>");
            out.println("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}