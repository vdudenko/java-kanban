public class Task {
    private final int id;
    public String name;
    public String description;
    public Status status;

    public Task (String name, String description) {
        this.name = name;
        this.description = description;
        this.id = TaskManager.nextTaskId++;
    }

    public Task (String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = TaskManager.nextTaskId++;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
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
}
