import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int nextTaskId = 1;

    private final HistoryManager historyManager;
    private final HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<Integer, SubTask>();
    private final HashMap<Integer, Epic> epics = new HashMap<Integer, Epic>();

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTask(int taskId) {
        Task task = tasks.get(taskId);
        historyManager.addToHistory(task);
        return task;
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = epics.get(epicId);
        historyManager.addToHistory(epic);
        return epic;
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        SubTask subTask = subTasks.get(subTaskId);
        historyManager.addToHistory(subTask);
        return subTask;
    }

    @Override
    public void addTask(Task task) {
        if (task.getId() == 0) {
            task.setId(generateId());
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        if(epic.getId() == 0) {
            epic.setId(generateId());
        }
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    @Override
    public void addSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        int subTaskId = subTask.getId();

        if (!epic.subTaskIds.contains(subTaskId)) {
            epic.subTaskIds.add(subTaskId);
        }
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        updateEpicStatus(subTask.getEpicId());
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        updateEpicStatus(subTask.getEpicId());
    }

    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpic(int epcId) {
        for (SubTask subTask: subTasks.values()) {
            if (subTask.getEpicId() == epcId) {
                subTasks.remove(subTask);
                historyManager.remove(subTask.getId());
            }
        }
        epics.remove(epcId);
        historyManager.remove(epcId);
    }

    @Override
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
        historyManager.remove(subTaskId);
        updateEpicStatus(subTask.getEpicId());
    }

    @Override
    public void deleteAllTask() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public ArrayList<SubTask> getEpicSubtasks(int epicId) {
        ArrayList<SubTask> epicSubTasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        for (Integer subtaskId: epic.subTaskIds) {
            epicSubTasks.add(subTasks.get(subtaskId));
        }
        return epicSubTasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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

    public int getNextTaskId() {
        return this.nextTaskId;
    }
}
