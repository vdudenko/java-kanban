import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static TaskManager taskManager;

    @BeforeAll
    static void beforeAll() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addToHistory() {
        Task task = new Task("name", "test description", Status.NEW);
        taskManager.addTask(task);

        taskManager.getTask(1);
        assertEquals(1, taskManager.getHistory().size());
        taskManager.getTask(1);
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void shouldBeMoreThan10() {
        for (int i = 1; i < 12; i++) {
            Task task = new Task("name", "test description", Status.NEW);
            taskManager.addTask(task);
            taskManager.getTask(i);
        }
        assertEquals(11, taskManager.getHistory().size());
    }
}