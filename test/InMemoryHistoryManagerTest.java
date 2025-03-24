import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void addToHistory() {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("name", "test description", Status.NEW, 30, "17.03.2024 21:00");
        taskManager.addTask(task);

        taskManager.getTask(1);
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void shouldBeMoreThan10() {
        TaskManager taskManager = Managers.getDefault();
        for (int i = 1; i < 12; i++) {
            String $str = "0" + i;
            if (i > 9) {
                $str = "" + i;
            }
            Task task = new Task("name", "test description", Status.NEW, 0, "17.03.2024 21:" + $str);
            taskManager.addTask(task);
            taskManager.getTask(i);
        }
        assertEquals(11, taskManager.getHistory().size());
    }
}