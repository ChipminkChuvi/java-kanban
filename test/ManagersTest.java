import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanager.*;
import taskmodel.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    private Task task = new Task(5, "Задача1", "Описание Задачи1");

    @Test
    void testCreateTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        taskManager.createTask(task);

        final int taskId = taskManager.getId();

        Task testTask = taskManager.getTask(taskId);

        Assertions.assertNotNull(testTask, "Задача не найдена.");
        Assertions.assertEquals(task, testTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        Assertions.assertNotNull(tasks, "Задачи не возвращаются.");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void testCreateHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

}