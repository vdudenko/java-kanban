import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subTaskIds;
    public TaskType type = TaskType.EPIC;
    private LocalDateTime endTime;

    public Epic(String name, String description, ArrayList<Integer> subTaskIds) {
        super(name, description);
        this.subTaskIds = subTaskIds;
    }

    public Epic(int id, String name, String description, Status status, long duration, String startTime) {
        super(id, name, description, status, duration, startTime);
    }

    public String getEndTimeFormat() {
        return endTime != null ? endTime.format(formatter) : "";
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}
