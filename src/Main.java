import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Epic epic = new Epic("Epic 1", "test epic description", new ArrayList<>());
        taskManager.addEpic(epic);

        SubTask subTask = new SubTask("Test sub task 1", "Test sub task description", Status.NEW, epic.getId(), 30, "17.03.2024 21:00");
        taskManager.addSubTask(subTask);
        SubTask subTask2 = new SubTask("Test sub task 1", "Test sub task description", Status.NEW, epic.getId(), 30, "18.03.2024 21:00");
        taskManager.addSubTask(subTask2);
        SubTask subTask3 = new SubTask("Test sub task 1", "Test sub task description", Status.NEW, epic.getId(), 30, "17.03.2024 15:00");
        taskManager.addSubTask(subTask3);

    }
}
