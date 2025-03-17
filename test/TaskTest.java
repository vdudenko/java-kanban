import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class TaskTest {

    private static TaskManager taskManager;

    @BeforeAll
    static void beforeAll() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void shouldBeEqualById() {
        Task task = new Task("name", "test description", Status.NEW, 30, "17.03.2024 21:00");
        Task task2 = new Task("name", "test description", Status.NEW, 30, "17.03.2024 21:00");

        taskManager.addTask(task);
        taskManager.addTask(task2);
        assertEquals(task, taskManager.getTask(task.getId()));
        assertNotEquals(task, task2);
    }

    @Test
    public void nodesShouldBeEqualById() {
        Epic epic = new Epic("Epic 1", "test epic description", new ArrayList<>());
        Epic epic2 = new Epic("Epic 1", "test epic description", new ArrayList<>());

        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);

        assertEquals(epic, taskManager.getEpic(epic.getId()));
        assertNotEquals(epic, epic2);

        SubTask subTask = new SubTask("Test sub task", "Test sub task description", Status.NEW, epic.getId(), 30, "17.03.2024 17:00");
        SubTask subTask2 = new SubTask("Test sub task", "Test sub task description", Status.NEW, epic.getId(), 30, "17.03.2024 17:00");

        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask2);

        assertEquals(subTask, taskManager.getSubTask(subTask.getId()));
        assertNotEquals(subTask, subTask2);
    }
}