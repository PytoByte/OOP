package services;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import model.CheckResult;
import model.Checkpoint;

/**
 * Report generator for task check results.
 */
public class ReportGenerator {

    /**
     * Write report in html format.
     *
     * @param result list of task check results
     * @param reportFile file where report will be written
     */
    public static void writeHtml(List<CheckResult> result, File reportFile) throws IOException {
        try (PrintWriter out = new PrintWriter(reportFile, StandardCharsets.UTF_8)) {
            out.println("<html><head><meta charset='UTF-8'>");
            out.println("<style>table{border-collapse:collapse;} "
                    + "th,td{border:1px solid #ccc; padding:8px;}</style>");
            out.println("</head><body>");
            out.println("<table>");

            out.println("<tr>"
                    + "<th>Group</th>"
                    + "<th>Student</th>"
                    + "<th>Task</th>"
                    + "<th>Checkpoints</th>"
                    + "<th>Download</th>"
                    + "<th>Task found</th>"
                    + "<th>Build</th>"
                    + "<th>Docs</th>"
                    + "<th>Checkstyle</th>"
                    + "<th>Tests build</th>"
                    + "<th>Tests</th>"
                    + "<th>Points</th>"
                    + "</tr>");

            for (CheckResult checkResult : result) {
                writeResultRow(out, checkResult);
            }

            out.println("</table></body></html>");
        } catch (IOException e) {
            throw new IOException(
                    String.format("Can't create report file %s", reportFile.getAbsolutePath()), e
            );
        }
    }

    /**
     * Writes a single result row to the table.
     *
     * @param out the PrintWriter to write to
     * @param checkResult the check result to render
     */
    private static void writeResultRow(PrintWriter out, CheckResult checkResult) {
        String checkpointsHtml = formatCheckpoints(checkResult);
        String taskCell = String.format("%s<br>%s",
                checkResult.checkAssignment().task().id(),
                checkResult.checkAssignment().task().title()
        );
        String pointsCell = formatPoints(checkResult);
        String testsCell = checkResult.testResults().isError() ? "Error" :
                String.format("passed %s<br>failed %s<br>skipped %s",
                checkResult.testResults().passed(),
                checkResult.testResults().failed(),
                checkResult.testResults().skipped());

        out.printf("<tr>"
                        + "<td>%s</td>"
                        + "<td>%s</td>"
                        + "<td>%s</td>"
                        + "<td>%s</td>"
                        + "<td>%s</td>"
                        + "<td>%s</td>"
                        + "<td>%s</td>"
                        + "<td>%s</td>"
                        + "<td>%s</td>"
                        + "<td>%s</td>"
                        + "<td>%s</td>"
                        + "<td>%s</td>"
                        + "</tr>\n",
                checkResult.checkAssignment().group().name(),
                checkResult.checkAssignment().student().name(),
                taskCell,
                checkpointsHtml,
                boolToStatus(checkResult.download()),
                boolToStatus(checkResult.taskFound()),
                boolToStatus(checkResult.build()),
                boolToStatus(checkResult.doc()),
                boolToStatus(checkResult.style()),
                boolToStatus(!checkResult.testResults().isError()
                        && checkResult.testResults().failed() == 0),
                testsCell,
                pointsCell
        );
    }

    /**
     * Formats checkpoints as HTML with status labels.
     *
     * @param checkResult the check result containing checkpoint data
     * @return HTML string with checkpoint info
     */
    private static String formatCheckpoints(CheckResult checkResult) {
        return checkResult.checkAssignment().task().checkpoints()
                .stream()
                .map(checkpoint -> {
                    String status = getCheckpointStatusLabel(
                            checkResult.completeDateTime(), checkpoint
                    );
                    return String.format("%s (%s) %s",
                            checkpoint.name(),
                            checkpoint.date(),
                            status);
                })
                .collect(Collectors.joining("<br>"));
    }

    /**
     * Formats total and max points for display.
     *
     * @param checkResult the check result containing points data
     * @return formatted points string, e.g. "85.00/100.00"
     */
    private static String formatPoints(CheckResult checkResult) {
        float earned = checkResult.points();
        double max = checkResult.checkAssignment().task().basePoints()
                + checkResult.checkAssignment().task().checkpoints()
                        .stream()
                        .mapToDouble(Checkpoint::rewardPoints)
                        .sum();
        return String.format("%.02f/%.02f", earned, max);
    }

    /**
     * Converts boolean to OK/FAIL label.
     *
     * @param value the boolean value
     * @return "OK" if true, "FAIL" otherwise
     */
    private static String boolToStatus(boolean value) {
        return value ? "OK" : "FAIL";
    }

    /**
     * Get label of checkpoint status.
     *
     * @param completeDateTime when task was completed
     * @param checkpoint checkpoint model
     * @return checkpoint status label
     */
    private static String getCheckpointStatusLabel(
            OffsetDateTime completeDateTime,
            Checkpoint checkpoint
    ) {
        if (completeDateTime == null) {
            return "[NO DATA]";
        }
        return completeDateTime.toLocalDate().isAfter(checkpoint.date())
                ? "[FAILED]"
                : "[PASSED]";
    }
}