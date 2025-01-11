import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("name", "test description", Status.NEW);

        taskManager.addTask(task);

        task = taskManager.getTask(task.getId());

        System.out.println(taskManager.getHistory());
        //обновление
        task.name = "new name";
        task.status = Status.IN_PROGRESS;
        taskManager.updateTask(task);

        System.out.println(task);

        System.out.println(taskManager.getHistory());
    }
}
