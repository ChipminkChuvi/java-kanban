package taskmanager;

import taskmodel.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node> linkHashMap = new HashMap<>();
    private LinkList linkList = new LinkList();

    @Override
    public void add(Task task) {
        if (linkHashMap.containsKey(task.getId())) {
            Node node = linkList.linkLast(task);
            remove(task.getId());
            linkHashMap.put(task.getId(), node);
        } else {
            Node node = linkList.linkLast(task);
            linkHashMap.put(task.getId(), node);
        }
    }

    @Override
    public List<Task> getHistory() {
        return linkList.getTasks();
    }

    @Override
    public void remove(int id) {
        Node node = linkHashMap.get(id);
        if (node != null) {
            removeNode(node);
            linkHashMap.remove(id);
        } else {
            System.out.println("Задачи нет в списке");
        }
    }

    public void removeNode(Node node) {
        final Node next = node.getNext();
        final Node prev = node.getPrev();
        if (prev == null) {
            linkList.head = next;
        } else {
            prev.setNext(next);
            node.setPrev(null);
        }

        if (next == null) {
            linkList.tail = prev;
        } else {
            next.setPrev(prev);
            node.setNext(null);
        }
        node.setData(null);
        linkList.size--;
    }


    public Map<Integer, Node> getLinkHashMap() {
        return linkHashMap;
    }

    public LinkList getLinkList() {
        return linkList;
    }

    private class LinkList<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        public int getSize() {
            return size;
        }

        public Node<T> linkLast(T element) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.setNext(newNode);
            size++;
            return newNode;
        }

        public List<Task> getTasks() {
            List<Task> list = new ArrayList<>();
            for (Node<T> i = head; i != null; i = i.getNext()) {
                list.add((Task) i.getData());
            }
            return list;
        }

    }
}
