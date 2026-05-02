package Domain;

public record TestResults(int passed, int failed, int skipped) {
    public static TestResults empty() {
        return new TestResults(0, 0, 0);
    }
}
