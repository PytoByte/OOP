package Domain;

import java.util.Map;

public record Group(String name, Map<String, Student> students) {
}
