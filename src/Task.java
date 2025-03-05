public class Task {
    private int id;
    public String name;
    public String description;
    public Status status;
    public TaskType type = TaskType.TASK;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.join(",", new String[]{
           String.valueOf(id),
           type.toString(),
           name,
           description,
           status.toString(),
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
