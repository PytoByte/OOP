package model;

import java.time.OffsetDateTime;

/**
 * Task check result model.
 *
 * @param checkAssignment task check assignment
 * @param completeDateTime date and time when task was completed
 * @param download is download successful
 * @param taskFound is repository contains task
 * @param build is build successful
 * @param doc is docs generated successfully
 * @param style is code checkstyle passed
 * @param testResults results of tests run
 * @param points points for task
 */
public record CheckResult(
    CheckAssignment checkAssignment,
    OffsetDateTime completeDateTime,
    boolean download,
    boolean taskFound,
    boolean build,
    boolean doc,
    boolean style,
    TestResults testResults,
    float points
) {
    /**
     * Factory of check result with failed download.
     *
     * @param checkAssignment task check assignment
     * @return check result with everything failed
     */
    public static CheckResult failedDownload(CheckAssignment checkAssignment) {
        return new CheckResult(
                checkAssignment,
                null,
                false,
                false,
                false,
                false,
                false,
                TestResults.error(),
                0
        );
    }

    /**
     * Factory of check result with task not found.
     *
     * @param checkAssignment task check assignment
     * @return check result with everything failed except download
     */
    public static CheckResult taskNotFound(CheckAssignment checkAssignment) {
        return new CheckResult(
                checkAssignment,
                null,
                true,
                false,
                false,
                false,
                false,
                TestResults.error(),
                0
        );
    }
}
