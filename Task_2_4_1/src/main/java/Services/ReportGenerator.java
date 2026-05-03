package Services;

import Domain.CheckResult;
import Domain.Checkpoint;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReportGenerator {
    public static void writeHtml(List<CheckResult> result, File reportFile) {
        try (PrintWriter out = new PrintWriter(reportFile, StandardCharsets.UTF_8)) {
            out.println("<html><head><meta charset='UTF-8'>");
            out.println("<style>table{border-collapse:collapse;} " +
                    "th,td{border:1px solid #ccc; padding:8px;}</style>");
            out.println("</head><body>");
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
                    "<th>Tests build</th>" +
                    "<th>Tests</th>" +
                    "<th>Points</th>" +
                    "</tr>");

            for (CheckResult checkResult : result) {
                String checkpointsStatus = checkResult.checkAssignment().task().checkpoints()
                        .stream()
                        .map(checkpoint -> {
                            String status = getStatusLabel(
                                    checkResult.completeDateTime(), checkpoint
                            );
                            return String.format("%s (%s) %s",
                                    checkpoint.name(),
                                    checkpoint.date(),
                                    status);
                        })
                        .collect(Collectors.joining("<br>"));

                out.printf("<tr>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>%s</td>" +
                                "<td>passed %d<br>failed %d<br>skipped %d</td>" +
                                "<td>%s</td>" +
                                "</tr>\n",
                        checkResult.checkAssignment().group().name(),
                        checkResult.checkAssignment().student().name(),
                        String.format("%s<br>%s",
                                checkResult.checkAssignment().task().id(),
                                checkResult.checkAssignment().task().title()
                        ),
                        checkpointsStatus,
                        checkResult.download() ? "OK" : "FAIL",
                        checkResult.build() ? "OK" : "FAIL",
                        checkResult.doc() ? "OK" : "FAIL",
                        checkResult.style() ? "OK" : "FAIL",
                        checkResult.tests() ? "OK" : "FAIL",
                        checkResult.passedTestsCount(),
                        checkResult.failedTestsCount(),
                        checkResult.skippedTestsCount(),
                        String.format("%.02f/%.02f",
                                checkResult.points(),
                                checkResult.checkAssignment().task().basePoints() +
                                        checkResult.checkAssignment().task().checkpoints().stream()
                                                .mapToDouble(Checkpoint::rewardPoints).sum()
                        )
                );
            }

            out.println("</table>");
            out.println("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сравнивает время коммита с датой дедлайна.
     * Коммит считается вовремя, если его дата (без учета времени) <= дате дедлайна.
     */
    private static String getStatusLabel(OffsetDateTime commitTime, Checkpoint checkpoint) {
        if (commitTime == null) {
            return "[NO DATA]";
        }

        if (commitTime.toLocalDate().isAfter(checkpoint.date())) {
            return "[FAILED]";
        } else {
            return "[PASSED]";
        }
    }
}