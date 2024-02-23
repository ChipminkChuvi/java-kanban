import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanager.HistoryManager;
import taskmanager.InMemoryHistoryManager;
import taskmodel.Task;
import taskmodel.TaskStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    Task task = new Task(5,"Задача1", "Описание Задачи1");
    Task task1 = new Task(10,"Задача2", "Описание Задачи2");
    HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void add() {
        historyManager.add(task);
        final ArrayList<Task> history = historyManager.getHistory();
        Assertions.assertNotNull(history, "История не пустая.");
        Assertions.assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void addMoreThan10Task() {
        for (int i = 0; i < 15; i++ ){
            historyManager.add(task);
        }
        final ArrayList<Task> history = historyManager.getHistory();
        Assertions.assertEquals(10, history.size(), "Количество задач в истории больше 10");
    }

    @Test
    void checkingIfTheFirstElementInHistoryIsOverwritten() {
        for (int i = 0; i < 10; i++ ){
            historyManager.add(task);
        }
        historyManager.add(task1);
        final ArrayList<Task> history = historyManager.getHistory();
        Assertions.assertEquals(history.get(history.size()-1),task1);
    }

    @Test
    void tasksAddedRetainThePreviousPersionOfTheTask() {
        historyManager.add(task);
        historyManager.add(task1);
        Task task3 = new Task(5,"Задача1", "Обновление задачи1", TaskStatus.IN_PROGRESS);
        historyManager.add(task3);
        final ArrayList<Task> history = historyManager.getHistory();
        Assertions.assertEquals(history.getFirst(),task);
        Assertions.assertEquals(history.getLast(),task3);

        }

}