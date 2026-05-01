importConfig("script/tasks_base.groovy")

group("24213") {
    student("Маркидонов В.В.", "PytoByte", "https://github.com/PytoByte/OOP")
    student("Изместьев Д.Д.", "s2kach", "https://github.com/s2kach/OOP")
}

check("24213", "Маркидонов В.В.") {
    task("Task_2_1_1")
    task("Task_2_4_1")
}

check("24213", "Изместьев Д.Д.") {
    task("Task_2_1_1")
    task("Task_2_4_1")
}
