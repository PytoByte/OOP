package Domain;

import java.time.LocalDate;

public record Checkpoint(String name, LocalDate date) {
    @Override
    public String toString() {
        return "Checkpoint{" +
                "name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
