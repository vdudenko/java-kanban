import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subTaskIds;
    public TaskType type = TaskType.EPIC;

    public Epic(String name, String description, ArrayList<Integer> subTaskIds) {
        super(name, description);
        this.subTaskIds = subTaskIds;
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }
}
