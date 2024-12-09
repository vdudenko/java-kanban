import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
//        Task task = new Task("name", "test description", Status.NEW);
//        Task task2 = new Task("name2", "test description 2", Status.NEW);
//
//        //добавление
//        taskManager.addTask(task);
//        taskManager.addTask(task2);
//        taskManager.printTasks();
//
//        //обновление
//        task.name = "new name";
//        task.status = Status.IN_PROGRESS;
//        taskManager.updateTask(task);
//        taskManager.printTasks();
//
//        //Получение
//        Task task3 = taskManager.getTask(task.getId());
//        System.out.println(task);
//        System.out.println(task3);
//        taskManager.printTasks();
//
//        //Удаление
//        taskManager.deleteTask(task2.getId());
//        taskManager.printTasks();
//
//        //Очистка
//        taskManager.deleteAllTask();
//        taskManager.printTasks();


        Epic epic = new Epic("Epic 1", "test epic description", new ArrayList<>());
        SubTask subTask = new SubTask("Test sub task", "Test sub task description", Status.NEW, epic.getId());
        SubTask subTask2 = new SubTask("Test sub task2", "Test sub task description2", Status.NEW, epic.getId());

        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask2);

//        subTask.status = Status.DONE;
//        taskManager.updateSubTask(subTask);
//
//        subTask2.status = Status.DONE;
//        taskManager.updateSubTask(subTask2);

        subTask2.status = Status.IN_PROGRESS;
        taskManager.updateSubTask(subTask2);
        System.out.println(taskManager.getEpicSubtasks(epic.getId()));
//        taskManager.deleteSubTask(subTask.getId());
//        taskManager.deleteSubTask(subTask2.getId());

        taskManager.printEpics();
        taskManager.printSubTasks();
    }
}
