import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int nextTaskId = 1;

    private final HistoryManager historyManager;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    protected TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
        prioritizedTasks = new TreeSet<>((Task task1, Task task2) -> {
            if (task1 != null) {
                if (task1.startTime != null && task2.startTime != null) {
                    if (task1.startTime.isAfter(task2.startTime)) {
                        return 1;
                    } else if (task1.startTime.isEqual(task2.startTime)) {
                        return -1;
                    }
                } else if (task1.startTime != null) {
                    return -1;
                } else if (task2.startTime != null) {
                    return 1;
                }
            }
            return -1;
        });
    }

    public int getNextTaskId() {
        return this.nextTaskId;
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
    public void addTask(Task task) throws TaskIntersectedException {
        if (isIntersected(task)) throw new TaskIntersectedException("Not Acceptable");
        if (task.getId() == 0) {
            task.setId(generateId());
        }
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic.getId() == 0) {
            epic.setId(generateId());
        }
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    @Override
    public void addSubTask(SubTask subTask) throws TaskIntersectedException {
        if (isIntersected(subTask)) throw new TaskIntersectedException("Not Acceptable");
        Epic epic = epics.get(subTask.getEpicId());
        int subTaskId = subTask.getId();

        if (subTask.getId() == 0) {
            subTask.setId(generateId());
        }

        if (!epic.subTaskIds.contains(subTaskId)) {
            epic.subTaskIds.add(subTask.getId());
        }

        subTasks.put(subTask.getId(), subTask);
        updateEpicStatus(subTask.getEpicId());
        setEpicDateTimesAndDuration(subTask.getEpicId());
        prioritizedTasks.add(subTask);
    }

    @Override
    public void updateTask(Task task) throws TaskIntersectedException {
        if (isIntersected(task)) throw new TaskIntersectedException("Not Acceptable");
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    @Override
    public void updateSubTask(SubTask subTask) throws TaskIntersectedException {
        if (isIntersected(subTask)) throw new TaskIntersectedException("Not Acceptable");
        subTasks.put(subTask.getId(), subTask);
        updateEpicStatus(subTask.getEpicId());
        setEpicDateTimesAndDuration(subTask.getEpicId());
    }

    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
        prioritizedTasks.remove(tasks.get(taskId));
    }

    @Override
    public void deleteEpic(int epcId) {
        for (SubTask subTask: subTasks.values()) {
            if (subTask.getEpicId() == epcId) {
                subTasks.remove(subTask);
                historyManager.remove(subTask.getId());
                prioritizedTasks.remove(subTask);
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
        setEpicDateTimesAndDuration(subTask.getEpicId());
        prioritizedTasks.remove(subTask);
    }

    @Override
    public void deleteAllTask() {
        tasks.clear();
        prioritizedTasks.clear();
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

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }


    private void setEpicDateTimesAndDuration(int epicId) {
        Epic epic = epics.get(epicId);
        if (!epic.subTaskIds.isEmpty()) {
            LocalDateTime startTime;
            LocalDateTime endTime = null;
            Duration duration = null;

            Optional<SubTask> firstSub = epic.subTaskIds.stream()
                    .map(subTasks::get)
                    .min(Comparator.comparing(Task::getStartTime));

            startTime = firstSub.get().getStartTime();
            Optional<SubTask> lastSub = epic.subTaskIds.stream()
                    .map(subTasks::get)
                    .max(Comparator.comparing(Task::getEndTime));

            if (lastSub.isPresent()) {
                endTime = lastSub.get().getEndTime();
                duration = Duration.between(startTime, endTime);
            }

            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            epic.setDuration(duration);
        }
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

    private boolean isIntersected(Task task) {
        List<Task> intersectedList = prioritizedTasks.stream()
                .filter(prioritizedTask -> (task.startTime.isAfter(prioritizedTask.startTime) &&
                            task.startTime.isBefore(prioritizedTask.getEndTime())) ||
                    (task.getEndTime().isAfter(prioritizedTask.startTime) &&
                            task.getEndTime().isBefore(prioritizedTask.getEndTime())) ||
                    ((task.startTime.isEqual(prioritizedTask.startTime) &&
                            (task.getEndTime().isEqual(prioritizedTask.getEndTime()) ||
                                    task.getEndTime().isBefore(prioritizedTask.getEndTime()))))
                )
                .toList();

        return !intersectedList.isEmpty();
    }
}
