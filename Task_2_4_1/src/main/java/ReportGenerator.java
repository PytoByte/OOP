import Domain.CheckResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ReportGenerator {
    public static void writeHtml(List<CheckResult> res) {
        try (PrintWriter out = new PrintWriter("report.html", StandardCharsets.UTF_8)) {
            out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>OOP Checker Report</title>");
            out.println("<style>table{border-collapse:collapse}th,td{border:1px solid #ccc;padding:8px;text-align:center}th{background:#f0f0f0}</style></head><body>");
            out.println("<h2>Отчёт проверки</h2><table>");
            out.println("<tr><th>Группа</th><th>Студент</th><th>Задача</th><th>Сборка</th><th>Докум.</th><th>Style</th><th>Тесты</th><th>Балл</th></tr>");
            for (CheckResult r : res) {
                out.printf("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%d/%d/%d</td><td><b>%d</b></td></tr>%n",
                        escape(r.group), escape(r.student), escape(r.title),
                        r.build ? "+" : "-", r.doc ? "+" : "-", r.style ? "+" : "-",
                        r.p, r.f, r.s, r.total);
            }
            out.println("</table></body></html>");
        } catch (IOException e) {
            System.err.println("Ошибка записи файла: " + e.getMessage());
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}