public class SubTask extends Task {
    private final int epicId;
    public TaskType type = TaskType.SUB_TASK;

    public SubTask(String name, String description, Status status, int epicId, long duration, String startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, String description, Status status, int epicId, long duration, String  startTime) {
        super(id, name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.join(",", new String[]{
                String.valueOf(this.getId()),
                type.toString(),
                name,
                status.toString(),
                description,
                getStartTimeFormat(),
                getDurationStr(),
                getEndTimeFormat(),
                String.valueOf(epicId),
        });
    }
}
