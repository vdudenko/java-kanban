import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int nextTaskId = 1;

    private final HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<Integer, SubTask>();
    private final HashMap<Integer, Epic> epics = new HashMap<Integer, Epic>();

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    // Получение тасков
    public Task getTask(int taskId) {
        return tasks.get(taskId);
    }

    // Получение эпиков
    public Epic getEpic(int epicId) {
        return epics.get(epicId);
    }

    // Получение сабтасков
    public SubTask getSubTask(int subTaskId) {
        return subTasks.get(subTaskId);
    }

    // Добавление тасков
    public void addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    // Добавление эпиков
    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    // Добавление сабтасков, обновляем статус эпика
    public void addSubTask(SubTask subTask) {
        Epic epic = getEpic(subTask.getEpicId());
        int subTaskId = subTask.getId();

        if (!epic.subTaskIds.contains(subTaskId)) {
            epic.subTaskIds.add(subTaskId);
        }
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        updateEpicStatus(subTask.getEpicId());
    }

    // обновление тасков
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    // обновление эпиков
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    // обновляем subTask, сразу проверяем статусы всех сабтасков эпика для обновления статуса
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        updateEpicStatus(subTask.getEpicId());
    }

    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteEpic(int epcId) {
        for (SubTask subTask: subTasks.values()) {
            if (subTask.getEpicId() == epcId) {
                subTasks.remove(subTask.getId());
            }
        }
        epics.remove(epcId);
    }

    public void deleteSubTask(int subTaskId) {
        SubTask subTask = subTasks.get(subTaskId);
        Epic epic = epics.get(subTask.getEpicId());
        ArrayList<Integer> epicSubTaskIds = epic.subTaskIds;
        for (int i = 0; i < epicSubTaskIds.size(); i++) {
            if (subTaskId == epicSubTaskIds.get(i)) {
                epicSubTaskIds.remove(i);
            }
        }
        subTasks.remove(subTaskId);
        updateEpicStatus(subTask.getEpicId());
    }

    public void deleteAllTask() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public ArrayList<SubTask> getEpicSubtasks(int epicId) {
        ArrayList<SubTask> epicSubTasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        for (Integer subtaskId: epic.subTaskIds) {
            epicSubTasks.add(subTasks.get(subtaskId));
        }
        return epicSubTasks;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        int newCount = 0;
        int inProgressCount = 0;
        int doneCount = 0;

        for (Integer subTaskId: epic.subTaskIds) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask != null) {
                switch (subTask.getStatus()) {
                    case Status.IN_PROGRESS:
                        inProgressCount++;
                        break;
                    case Status.DONE:
                        doneCount++;
                        break;
                    default:
                        newCount++;
                        break;
                }
            }
        }

        if (newCount >= 0) {
            epic.setStatus(Status.NEW);
        }
        if (inProgressCount > 0) {
            epic.setStatus(Status.IN_PROGRESS);
        }
        if (doneCount > 0 && doneCount == epic.subTaskIds.size()) {
            epic.setStatus(Status.DONE);
        }
    }

    private int generateId() {
        return nextTaskId++;
    }
}
