import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void addToHistory() {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("name", "test description", Status.NEW);
        taskManager.addTask(task);

        taskManager.getTask(1);
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void shouldBeMoreThan10() {
        TaskManager taskManager = Managers.getDefault();
        for (int i = 1; i < 12; i++) {
            Task task = new Task("name", "test description", Status.NEW);
            taskManager.addTask(task);
            taskManager.getTask(i);
        }
        assertEquals(11, taskManager.getHistory().size());
    }
}