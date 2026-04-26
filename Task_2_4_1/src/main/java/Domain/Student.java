package Domain;

public record Student(String name, String nick, String repoUrl) {
    @Override
    public String toString() {
        return "Student{" +
                "nick='" + nick + '\'' +
                ", name='" + name + '\'' +
                ", repoUrl='" + repoUrl + '\'' +
                '}';
    }
}
