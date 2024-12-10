import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subTaskIds;

    public Epic(String name, String description, ArrayList<Integer> subTaskIds) {
        super(name, description);
        this.subTaskIds = subTaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id='" + getId() + '\''+
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subTaskIds=" + subTaskIds +
                '}';
    }
}
