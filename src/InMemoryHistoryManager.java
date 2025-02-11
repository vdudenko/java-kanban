import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final HashMap<Integer, Node<Task>> history = new HashMap<>();
    private final CustomLinkedList<Task> customLinkedList = new CustomLinkedList<>();

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    public void addToHistory(Task task) {
        removeNode(history.get(task.getId()));
        customLinkedList.linkLast(task);
        history.remove(task.getId());
        history.put(task.getId(), customLinkedList.tail);
    }

    public void remove(int id) {
        removeNode(history.get(id));
        history.remove(id);
    }

    public void removeNode(Node<Task> node) {
        if(history.containsValue(node)) {
            if (node.prev == null && node.next == null) {
                customLinkedList.tail = null;
                customLinkedList.head = null;
            }
            if (node.prev == null && node.next != null) {
                Node<Task> nextNode = node.next;
                nextNode.prev = null;
                customLinkedList.head = nextNode;
                if (nextNode.next == null) {
                    customLinkedList.tail = null;
                }
            }
            if (node.prev != null && node.next != null) {
                Node<Task> prevNode = node.prev;
                Node<Task> nextNode = node.next;
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            };
            if (node.prev != null && node.next == null) {
                Node<Task> prevNode = node.prev;
                customLinkedList.tail = prevNode;
                prevNode.next = null;
            }
            customLinkedList.size--;
        }
    }

    static public class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        public void linkLast(T element) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail == null || head == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            size++;
        }

        public List<T> getTasks() {
            ArrayList<T> tasks = new ArrayList<>();
            Node<T> head1 = head;
            if(head1 == null) return tasks;
            while (head1.next != null) {
                tasks.add(head1.data);
                head1 = head1.next;
            }
            tasks.add(head1.data);
            return tasks;
        }
    }
}
