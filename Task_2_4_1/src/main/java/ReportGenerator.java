import Domain.CheckResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ReportGenerator {
    public static void writeHtml(List<CheckResult> res) {
        try (PrintWriter out = new PrintWriter("report.html", StandardCharsets.UTF_8)) {
            // Добавлен meta charset для корректного отображения кириллицы
            out.println("<html><head><meta charset='UTF-8'></head><body>");
            out.println("<h1>Отчет</h1>");
            out.println("<table border='1' cellpadding='5'>");
            // Заголовки
            out.println("<tr><th>Студент</th><th>Задача</th><th>Чекпоинты</th><th>Сборка</th><th>Тесты</th><th>Балл</th></tr>");

            // Строки
            for (CheckResult r : res) {
                String checkpointsInfo = "Нет данных"; // Заглушка

                out.printf("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%d/%d</td><td>%d</td></tr>%n",
                        r.student,
                        r.taskId,
                        checkpointsInfo,
                        r.build ? "OK" : "FAIL",
                        r.p, r.f,
                        r.total);
            }

            out.println("</table>");
            out.println("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}