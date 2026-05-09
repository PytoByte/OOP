package model;

/**
 * Task test results model.
 *
 * @param passed passed tests count
 * @param failed failed tests count
 * @param skipped skipped tests count
 */
public record TestResults(int passed, int failed, int skipped) {
    /**
     * Factory of test results model in error state.
     *
     * @return test results in error state
     */
    public static TestResults error() {
        return new TestResults(-1, -1, -1);
    }

    /**
     * Check if model in error state.
     *
     * @return true if in error state, false overwise
     */
    public boolean isError() {
        return passed == -1 && failed == -1 && skipped == -1;
    }
}
