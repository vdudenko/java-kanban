import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    public String name;
    public String description;
    public Status status;
    public TaskType type = TaskType.TASK;
    public Duration duration;
    public LocalDateTime startTime;
    public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String name, String description, Status status, long duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, formatter);
    }

    public Task(int id, String name, String description, Status status, long duration, String startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, formatter);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getDurationStr() {
        return duration != null ? String.valueOf(duration.toMinutes()) : "";
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getStartTimeFormat() {
        return startTime != null ? startTime.format(formatter) : "";
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public String getEndTimeFormat() {

            return startTime != null ? startTime.plus(duration).format(formatter) : "";
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return String.join(",", new String[]{
           String.valueOf(id),
           type.toString(),
           name,
           description,
           status.toString(),
           getStartTimeFormat(),
           getDurationStr(),
           getEndTimeFormat()
        });
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj != null || obj.getClass() != getClass()) return false;
        Task newObj = (Task) obj;
        return id == newObj.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
