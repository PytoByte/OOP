importConfig("tasks_base.groovy")

group("ИТ-101") {
    student("ivanov", "Иванов И.И.", "https://github.com/ivanov/lab-oop")
    student("petrov", "Петров П.П.", "https://github.com/petrov/lab-oop")
}

check("ИТ-101", "ivanov") { task("LAB_2_4_1") }
check("ИТ-101", "petrov") { task("LAB_2_4_1") }

settings {
    maxScorePerTask = 10
    passThreshold = 50
}
