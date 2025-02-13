import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final HashMap<Integer, Node<Task>> history = new HashMap<>();

    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void addToHistory(Task task) {
        removeNode(history.get(task.getId()));
        linkLast(task);
        history.remove(task.getId());
        history.put(task.getId(), tail);
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
        history.remove(id);
    }

    private void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null || head == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }

    private List<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node<Task> head1 = head;
        if (head1 == null) return tasks;
        while (head1.next != null) {
            tasks.add(head1.data);
            head1 = head1.next;
        }
        tasks.add(head1.data);
        return tasks;
    }

    private void removeNode(Node<Task> node) {
        if (history.containsValue(node)) {
            if (node.prev == null && node.next == null) {
                this.tail = null;
                this.head = null;
            }
            if (node.prev == null && node.next != null) {
                Node<Task> nextNode = node.next;
                nextNode.prev = null;
                this.head = nextNode;
                if (nextNode.next == null) {
                    this.tail = null;
                }
            }
            if (node.prev != null && node.next != null) {
                Node<Task> prevNode = node.prev;
                Node<Task> nextNode = node.next;
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            }
            if (node.prev != null && node.next == null) {
                Node<Task> prevNode = node.prev;
                this.tail = prevNode;
                prevNode.next = null;
            }
            this.size--;
        }
    }
}
