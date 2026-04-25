importConfig("tasks_base.groovy")

group("ИТ-101") {
    student("PytoByte", "Иванов И.И.", "https://github.com/PytoByte/OOP")
}

check("ИТ-101", "ivanov") {
    task("task-2-4-1")
}

check("ИТ-101", "petrov") {
    task("task-2-4-1")
}

settings {
    maxScorePerTask = 10
    passThreshold = 50
}
