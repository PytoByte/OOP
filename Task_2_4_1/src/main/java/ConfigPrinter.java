import Domain.CheckAssignment;
import Domain.Checkpoint;
import Domain.Config;
import Domain.Student;
import Domain.Task;

import java.util.List;
import java.util.Map;

public class ConfigPrinter {

    public static void print(Config config) {
        System.out.println("=== КОНФИГУРАЦИЯ ===");

        // 1. Задачи
        System.out.println("\n--- ЗАДАЧИ ---");
        for (Task task : config.tasks) {
            System.out.println("ID: " + task.id);
            System.out.println("  Название: " + task.title);
            System.out.println("  Макс. баллы: " + task.maxPoints);

            if (task.checkpoints != null && !task.checkpoints.isEmpty()) {
                System.out.println("  Чекпоинты:");
                for (Checkpoint cp : task.checkpoints) {
                    System.out.println("    - " + cp.name() + " (до " + cp.date() + ")");
                }
            } else {
                System.out.println("  Чекпоинты: нет");
            }
            System.out.println("------------------");
        }

        // 2. Группы и студенты
        System.out.println("\n--- ГРУППЫ И СТУДЕНТЫ ---");
        for (Map.Entry<String, List<Student>> entry : config.groups.entrySet()) {
            System.out.println("Группа: " + entry.getKey());
            for (Student s : entry.getValue()) {
                System.out.println("  Студент: " + s.fio() + " (@" + s.nick() + ")");
                System.out.println("    Репозиторий: " + s.repoUrl());
            }
        }

        // 3. Задания на проверку (Checks)
        System.out.println("\n--- ЗАДАНИЯ НА ПРОВЕРКУ ---");
        for (CheckAssignment check : config.checks) {
            System.out.println("Группа: " + check.group + " | Студент: " + check.student + " | Задача: " + check.taskId);
        }

        // 4. Настройки
        System.out.println("\n--- НАСТРОЙКИ ---");
        System.out.println("Макс. баллов за задачу: " + config.settings.maxScorePerTask);
        System.out.println("Порог сдачи (%): " + config.settings.passThreshold);

        System.out.println("\n=====================");
    }
}