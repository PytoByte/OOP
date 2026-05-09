package model;

/**
 * Model of task check assignment.
 *
 * @param group student's group model
 * @param student student model
 * @param task task model
 */
public record CheckAssignment(Group group, Student student, Task task) {
}
