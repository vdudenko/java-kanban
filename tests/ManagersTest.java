import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    public void shouldNotBeAddedTo() {
        TaskManager taskManager = Managers.getDefault();
        TaskManager taskManager2 = Managers.getDefault();
        assertNotNull(taskManager);
        assertNotNull(taskManager2);


    }
}