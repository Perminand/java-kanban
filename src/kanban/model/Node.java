package kanban.model;

public class Node<E> {
    private final E item;
    private Node<E> pref;
    private Node<E> next;

    public Node(Node<E> pref, E element, Node<E> next) {
        this.pref = pref;
        this.item = element;
        this.next = next;
    }

    public Node<E> getPref() {
        return pref;
    }

    public void setPref(Node<E> pref) {
        this.pref = pref;
    }

    public Node<E> getNext() {
        return next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "item=" + item +
                '}';
    }
}
