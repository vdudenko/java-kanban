import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subTaskIds;
    private Status status = Status.NEW;

    public Epic(String name, String description, ArrayList<Integer> subTaskIds) {
        super(name, description);
        this.subTaskIds = subTaskIds;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subTaskIds=" + subTaskIds +
                '}';
    }
}
