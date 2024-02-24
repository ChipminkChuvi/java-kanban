import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanager.InMemoryTaskManager;
import taskmodel.*;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private Task task = new Task(inMemoryTaskManager.getId() + 5, "Задача1", "Описание Задачи1");
    private Epic epic = new Epic(inMemoryTaskManager.getId() + 5, "Эпик1", "Описание Эпика1");


    @Test
    void createTask() {
        inMemoryTaskManager.createTask(task);
        final int taskId = inMemoryTaskManager.getId();
        Task testTask = inMemoryTaskManager.getTask(taskId);
        Assertions.assertNotNull(testTask, "Задача не найдена.");
        Assertions.assertEquals(task, testTask, "Задачи не совпадают.");
    }

    @Test
    void createEpic() {
        inMemoryTaskManager.createEpic(epic);
        final int epicId = inMemoryTaskManager.getId();
        Epic testEpic = inMemoryTaskManager.getEpic(epicId);
        Assertions.assertNotNull(testEpic, "Эпик не найден.");
        Assertions.assertEquals(epic, testEpic, "Эпики не совпадают.");
    }

    @Test
    void createSubTask() {
        inMemoryTaskManager.createEpic(epic);
        int idEpic = inMemoryTaskManager.getId();
        SubTask subTask = new SubTask(inMemoryTaskManager.getId() + 5, "Сабтаска1", "Описание Сабтаски 1", idEpic);
        inMemoryTaskManager.createSubTask(subTask);
        final int subTaskId = inMemoryTaskManager.getId();
        SubTask testSubTask = inMemoryTaskManager.getSubTask(subTaskId);
        Assertions.assertNotNull(testSubTask, "Сабтаска не найдена.");
        Assertions.assertEquals(subTask, testSubTask, "Сабтаски не совпадают.");
    }

    @Test
    void checkSubtaskCannotBeMadeIntoItsOwnEpic() {
        SubTask subTask = new SubTask("Сабтаска1", "Описание Сабтаски 1", inMemoryTaskManager.getId() + 5);
        SubTask subTask1 = inMemoryTaskManager.createSubTask(subTask);
        Assertions.assertNull(subTask1);
    }

    @Test
    void updateTask() {
        inMemoryTaskManager.createTask(task);
        Task task1 = new Task(inMemoryTaskManager.getId(), "Задача1", "Обновление задачи1", TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.updateTask(task1);
        Assertions.assertEquals(inMemoryTaskManager.getTask(inMemoryTaskManager.getId()), task1);

    }

    @Test
    void updateEpicAndSubtaskStatus() {
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаски 1, принадлежит эпику1", inMemoryTaskManager.getId()));
        inMemoryTaskManager.updateSubTask(new SubTask(inMemoryTaskManager.getId(), "Сабтаска1", "Обновление сабтаски1", inMemoryTaskManager.getId() - 5, TaskStatus.IN_PROGRESS));
        SubTask testSubTask = inMemoryTaskManager.getSubTask((inMemoryTaskManager.getId()));
        Epic testEpic = inMemoryTaskManager.getEpic(inMemoryTaskManager.getId() - 5);
        Assertions.assertEquals(testSubTask.getTaskStatus(), TaskStatus.IN_PROGRESS);
        Assertions.assertEquals(testEpic.getTaskStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void checkTasksWithGivenIdentifierAndGeneratedIdentifierDoNotConflictWithinTheManager() {
        Task task1 = new Task("Задача1", "Описание Задачи1");
        inMemoryTaskManager.createTask(task1);
        Task task2 = new Task(5, "Задача2", "Описание Задачи2");
        inMemoryTaskManager.createTask(task2);
        Task testTask1 = inMemoryTaskManager.getTask(inMemoryTaskManager.getId());
        Assertions.assertNotEquals(task1, testTask1);
    }

    ;

    @Test
    void immutabilityOfTheTaskIsCheckedWhenAddingTheTaskToTheManager() {
        Task task1 = new Task("Задача1", "Описание Задачи1");
        inMemoryTaskManager.createTask(task1);
        Task testTask1 = inMemoryTaskManager.getTask(inMemoryTaskManager.getId());
        Assertions.assertEquals(testTask1.getName(), task1.getName());
        Assertions.assertEquals(testTask1.getDescription(), task1.getDescription());
        Assertions.assertEquals(testTask1.getTaskStatus(), task1.getTaskStatus());
    }
}