import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private static TaskManager taskManager;

    @BeforeAll
    static void beforeAll() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void shouldNotBeAddedTo() {
        Epic epic = new Epic("Epic 1", "test epic description", new ArrayList<>());

        taskManager.addEpic(epic);

        SubTask subTask = new SubTask("Test sub task", "Test sub task description", Status.NEW, epic.getId());
        taskManager.addSubTask(subTask);

        SubTask subTask2 = new SubTask("Test sub task", "Test sub task description", Status.NEW, subTask.getId());
        //taskManager.addSubTask(subTask);
        assertNull(taskManager.getEpic(subTask.getId()));
        assertNotNull(taskManager.getEpic(epic.getId()));

    }
}