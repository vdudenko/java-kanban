import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) throws IOException {
        File newfile = File.createTempFile("test", null);
        FileBackedTaskManager tes = new FileBackedTaskManager(newfile);
        tes.addHeader();
        Task task = new Task("task1", "task", Status.NEW,  30, "17.03.2024 21:00");
        tes.addTask(task);
        Epic epic = new Epic("Epic 1", "test epic description", new ArrayList<>());
        tes.addEpic(epic);
        SubTask subTask = new SubTask("Test sub task 1", "Test sub task description", Status.NEW, epic.getId(), 30, "17.03.2024 17:00");
        tes.addSubTask(subTask);
        SubTask subTask2 = new SubTask("Test sub task 1", "Test sub task description", Status.NEW, epic.getId(), 30, "18.03.2024 17:00");
        tes.addSubTask(subTask2);
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            String fileData = Files.readString(Path.of(file.toURI()));
            if (fileData != null) {
                String[] lines = fileData.split("\\n");
                if (lines.length > 1) {
                    for (int i = 1; i < lines.length; i++) {
                        if (!lines[i].isBlank()) {
                            manager.fromString(lines[i]);
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл");
            return null;
        }
        return manager;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save(task);
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save(epic);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save(subTask);
    }

    private void save(Task task) {
        try (FileWriter file = new FileWriter(this.file, true)) {
            file.write(task.toString() + '\n');
        } catch (IOException e) {
            throw new ManagerSaveException("Произошло исключение в методе save()");
        }
    }

    private void addHeader() {
        try (FileWriter file = new FileWriter(this.file, true)) {
            file.write("id,type,name,status,description,start,duration,end,epic\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошло исключение в методе save()");
        }
    }

    private void fromString(String value) {
        String[] taskArr = value.split(",");
        switch (taskArr[1]) {
            case "EPIC":
                Epic epic = new Epic(Integer.parseInt(taskArr[0]), taskArr[2], taskArr[3], Status.valueOf(taskArr[4]), Integer.parseInt(taskArr[5]), taskArr[6]);
                addEpic(epic);
            case "SUB_TASK":
                SubTask subTask = new SubTask(Integer.parseInt(taskArr[0]), taskArr[2], taskArr[3], Status.valueOf(taskArr[4]), Integer.parseInt(taskArr[8]), Integer.parseInt(taskArr[5]), taskArr[6]);
                addSubTask(subTask);
            default:
                Task task = new Task(Integer.parseInt(taskArr[0]), taskArr[2], taskArr[3], Status.valueOf(taskArr[4]), Integer.parseInt(taskArr[5]), taskArr[6]);
                addTask(task);
        }
    }
}
