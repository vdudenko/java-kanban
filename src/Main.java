import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Epic epic = new Epic("Epic 1", "test epic description", new ArrayList<>());
        taskManager.addEpic(epic);
        Epic epic2 = new Epic("Epic 2", "test epic description", new ArrayList<>());
        taskManager.addEpic(epic2);

        SubTask subTask = new SubTask("Test sub task 1", "Test sub task description", Status.NEW, epic.getId());
        taskManager.addSubTask(subTask);
        SubTask subTask2 = new SubTask("Test sub task 1", "Test sub task description", Status.NEW, epic.getId());
        taskManager.addSubTask(subTask2);
        SubTask subTask3 = new SubTask("Test sub task 1", "Test sub task description", Status.NEW, epic.getId());
        taskManager.addSubTask(subTask3);

        taskManager.getEpic(epic.getId());
        taskManager.getEpic(epic2.getId());
        taskManager.getSubTask(subTask.getId());
        taskManager.getSubTask(subTask2.getId());
        taskManager.getSubTask(subTask3.getId());
        System.out.println(taskManager.getHistory());

        taskManager.getEpic(epic.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getEpic(epic2.getId());
        taskManager.getSubTask(subTask.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getSubTask(subTask2.getId());
        taskManager.getSubTask(subTask3.getId());
        System.out.println(taskManager.getHistory());

        taskManager.deleteSubTask(subTask3.getId());

        System.out.println(taskManager.getHistory());

        taskManager.deleteEpic(epic.getId());
        System.out.println(taskManager.getHistory());

    }
}
