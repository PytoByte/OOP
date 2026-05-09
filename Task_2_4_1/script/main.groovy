importConfig("script/tasks.groovy")

group("24213") {
    student("Маркидонов В.В.", "PytoByte")
    student("Изместьев Д.Д.", "s2kach")
}

check("24213") {
    task("Task_2_1_1")
}

check("24213", "Маркидонов В.В.") {
    task("Task_2_2_1")
}
