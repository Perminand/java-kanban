package kanban.service;

import kanban.model.Node;
import kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> history = new CustomLinkedList<>();
    public HashMap<Integer, Node<Task>> idNodeForCustomLinkedList = new HashMap<>();


    @Override
    public void add(Task task) {
        Node<Task> node = history.add(task);
        history.removeNode(idNodeForCustomLinkedList.get(task.getUin()));
        idNodeForCustomLinkedList.put(task.getUin(), node);

    }

    @Override
    public void remove(int id) {
        history.removeNode(idNodeForCustomLinkedList.get(id));
        idNodeForCustomLinkedList.remove(id);
    }

    @Override
    public ArrayList<Node<Task>> getHistory() {
        ArrayList<Node<Task>> arrayList = new ArrayList<>();
        for (Node<Task> x = history.first; x != null; x = x.getNext()) {
            arrayList.add(x);
        }
        return arrayList;
    }

    public static class CustomLinkedList<E> {
        Node<E> first;
        Node<E> last;
        transient int size = 0;

        public Node<E> add(E e) {
            Node<E> oldNode = first;
            final Node<E> newNode = new Node<>(null, e, oldNode);
            first = newNode;
            if (oldNode == null) last = newNode;
            else oldNode.setPref(newNode);
            size++;
            return newNode;
        }

        public void remove(E e) {
            for (Node<E> x = first; x != null; x = x.getNext()) {
                if (x == e) {
                    removeNode(x);
                }
            }
        }

        public void linkLast(E e) {
            final Node<E> oldNode = last;
            final Node<E> newNode = new Node<>(oldNode, e, null);
            last = newNode;
            if (oldNode == null) first = newNode;
            else oldNode.setNext(newNode);
            size++;
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


