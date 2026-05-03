package Domain;

public record TestResults(int passed, int failed, int skipped) {
    public static TestResults error() {
        return new TestResults(-1, -1, -1);
    }

    public boolean isError() {
        return passed == -1 && failed == -1 && skipped == -1;
    }
}
