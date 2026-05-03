package Model;

import java.util.Map;

/**
 * Student's group model.
 *
 * @param name unique group name
 * @param students map of student name and student model
 */
public record Group(String name, Map<String, Student> students) {
}
