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
                    "<th>Group</th>" +
                    "<th>Student</th>" +
                    "<th>Task</th>" +
                    "<th>Checkpoints</th>" +
                    "<th>Clone</th>" +
                    "<th>Build</th>" +
                    "<th>Docs</th>" +
                    "<th>Checkstyle</th>" +
                    "<th>Tests</th>" +
                    "<th>Points</th>" +
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
                                "<td>%s</td>" +
                                "<td>%d/%d</td>" +
                                "<td>%d</td>" +
                                "</tr>\n",
                        checkResult.checkAssignment().group().getName(),
                        checkResult.checkAssignment().student().name(),
                        checkResult.checkAssignment().task().getId(),
                        checkResult.checkAssignment().task().getCheckpoints().stream().map(
                                checkpoint -> checkpoint.name() + " " + checkpoint.date()
                                        .toString()
                        ).collect(Collectors.joining("<br>")),
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