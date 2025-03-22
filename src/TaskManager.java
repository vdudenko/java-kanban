import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<SubTask> getSubTasks();

    ArrayList<Epic> getEpics();

    Task getTask(int taskId);

    Epic getEpic(int epicId);

    SubTask getSubTask(int subTaskId);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void deleteTask(int taskId);

    void deleteEpic(int epcId);

    void deleteSubTask(int subTaskId);

    void deleteAllTask();

    void deleteAllEpics();

    List<Task> getHistory();

    ArrayList<SubTask> getEpicSubtasks(int epicId);

    int getNextTaskId();
}
