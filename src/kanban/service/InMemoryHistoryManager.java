package kanban.service;

import kanban.model.Node;
import kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> history = new CustomLinkedList<>();
    private final HashMap<Integer, Node<Task>> nodeMap = new HashMap<>();


    @Override
    public void add(Task task) {
        Node<Task> node = history.linkLast(task);
        history.removeNode(nodeMap.get(task.getUin()));
        nodeMap.put(task.getUin(), node);

    }

    @Override
    public void remove(int id) {
        history.removeNode(nodeMap.get(id));
        nodeMap.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> list = new ArrayList<>();
        Node<Task> node = history.first;
        while (node != null) {
            list.add(node.getItem());
            node = node.getNext();
        }
        return List.copyOf(list);
    }

    public static class CustomLinkedList<E> {
        Node<E> first;
        Node<E> last;
        int size = 0;

        public Node<E> linkLast(E e) {
            final Node<E> oldNode = last;
            final Node<E> newNode = new Node<>(oldNode, e, null);
            last = newNode;
            if (oldNode == null) first = newNode;
            else oldNode.setNext(newNode);
            size++;
            return newNode;
        }

        public void removeNode(Node<E> node) {
            if (node != null) {
                if (node.getPref() != null) node.getPref().setNext(node.getNext());
                else first = node.getNext();
                if (node.getNext() != null) node.getNext().setPref(node.getPref());
                else last = node.getPref();
                size--;
            }
        }
    }

}


