import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanager.HistoryManager;
import taskmanager.InMemoryHistoryManager;
import taskmanager.Node;
import taskmodel.Task;
import taskmodel.TaskStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private Task task = new Task(5, "Задача1", "Описание Задачи1");
    private Task task1 = new Task(10, "Задача2", "Описание Задачи2");
    private Task task2 = new Task(15, "Задача3", "Описание Задачи3");
    private Task task3 = new Task(20, "Задача4", "Описание Задачи4");
    private HistoryManager historyManager = new InMemoryHistoryManager();
    private InMemoryHistoryManager historyManageFromInMemoryHistoryManager = new InMemoryHistoryManager();

    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        Assertions.assertNotNull(history, "История не пустая.");
        Assertions.assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void сheckingForNoDuplicationTasks() {
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(2, history.size(), "Произошло дублирование задач");
    }

    @Test
    void checkingDeletionFirstFromHistory() {
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task.getId());
        final List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(history.getFirst(), task1);
    }

    @Test
    void checkingDeletionMedtFromHistory() {
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());
        final List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(history.size(), 3);
    }

    @Test
    void checkingDeletionLastFromHistory() {
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task2.getId());
        final List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(history.getLast(), task1);
    }

    @Test
    void addedTasksToUpdateThePreviousVersionTaskAndAddToEndOfTheList() {
        historyManager.add(task);
        historyManager.add(task1);
        Task task3 = new Task(5, "Задача1", "Обновление задачи1", TaskStatus.IN_PROGRESS, "08.06.2024 12:15", "15");
        historyManager.add(task3);
        final List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(history.getFirst(), task1);
        Assertions.assertEquals(history.getLast(), task3);
    }

    @Test
    void checkingConnectionsInLinkedMap() {
        historyManageFromInMemoryHistoryManager.add(task);
        historyManageFromInMemoryHistoryManager.add(task1);

        final Map<Integer, Node> linkHashMap = historyManageFromInMemoryHistoryManager.getLinkHashMap();
        final Node nodeFirst = linkHashMap.get(task.getId());
        final Node nodeSecond = linkHashMap.get(task1.getId());

        Assertions.assertEquals(nodeFirst.getPrev(), null);
        Assertions.assertEquals(nodeFirst.getNext(), nodeSecond);
        Assertions.assertEquals(nodeSecond.getPrev(), nodeFirst);
        Assertions.assertEquals(nodeSecond.getNext(), null);
    }

}