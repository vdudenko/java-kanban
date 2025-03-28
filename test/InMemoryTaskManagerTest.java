import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static TaskManager taskManager;

    @BeforeAll
    static void beforeAll() {
        taskManager = Managers.getDefault();
    }

    @Test
    void shouldAddAndGetTasks() {
        Task task = new Task("name", "test description", Status.NEW, 30, "17.03.2024 21:00");
        taskManager.addTask(task);
        Task task2 = new Task("name", "test description", Status.NEW, 30, "18.03.2024 21:00");
        taskManager.addTask(task2);

        assertNotNull(taskManager.getTask(task.getId()));

        Epic epic = new Epic("Epic 1", "test epic description", new ArrayList<>());
        taskManager.addEpic(epic);

        assertNotNull(taskManager.getEpic(epic.getId()));

        SubTask subTask = new SubTask("Test sub task", "Test sub task description", Status.NEW, epic.getId(), 30, "20.03.2024 21:00");
        taskManager.addSubTask(subTask);

        assertNotNull(taskManager.getSubTask(subTask.getId()));
    }

    @Test
    void shouldBeEqualBeforeAndAfterAdding() {
        String string = String.join(",", new String[]{
                String.valueOf(1),
                "TASK",
                "name",
                "test description",
                "NEW",
                "21.03.2024 21:00",
                "30",
                "21.03.2024 21:30"

        });
        Task task = new Task("name", "test description", Status.NEW, 30, "21.03.2024 21:00");
        taskManager.addTask(task);

        assertEquals(string, task.toString());
    }

}