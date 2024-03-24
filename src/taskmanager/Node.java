package taskmanager;

import java.util.NoSuchElementException;
import java.util.Objects;

public class Node<E> {
    private E data;
    private Node<E> next;
    private Node<E> prev;

    public Node(Node<E> prev, E data, Node<E> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public E getData() {
        return data;
    }

    public Node<E> getNext() {
        return next;
    }

    public Node<E> getPrev() {
        return prev;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public void setPrev(Node<E> prev) {
        this.prev = prev;
    }

    protected void setData(E data) {
        this.data = data;
    }
}
