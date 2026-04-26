importConfig("tasks_base.groovy")

group("24213") {
    student("Маркидонов В.В.", "PytoByte", "https://github.com/PytoByte/OOP")
}

check("24213", "Маркидонов В.В.") {
    task("Task_2_2_1")
}
