import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanager.InMemoryTaskManager;
import taskmodel.*;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private InMemoryTaskManager inMemoryTaskManagerTest = new InMemoryTaskManager();
    private Task task = new Task(inMemoryTaskManagerTest.getId() + 5, "Задача1", "Описание Задачи1");
    private Task updateTask = new Task(inMemoryTaskManagerTest.getId() + 5, "Задача1", "Обновление Задачи1", TaskStatus.IN_PROGRESS, "08.06.2024 12:15", "15");
    private Epic epic = new Epic(inMemoryTaskManagerTest.getId() + 5, "Эпик1", "Описание Эпика1");

    @Test
    void checkingThatNoIrrelevantIdSubtasksShouldRemainInsideEpics() {
        inMemoryTaskManagerTest.createEpic(new Epic("Эпик1", "Описание Эпика1"));
        Integer idEpic = inMemoryTaskManagerTest.getId();
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаски 1", idEpic, "08.06.2024 12:15", "15"));
        List<Integer> list = inMemoryTaskManagerTest.getEpic(inMemoryTaskManagerTest.getId() - 5).getLinkedSubTask();
        Assertions.assertEquals(list.size(), 1);

        inMemoryTaskManagerTest.removeSubTaskById(inMemoryTaskManagerTest.getId());
        list = epic.getLinkedSubTask();
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    void createTask() {
        inMemoryTaskManagerTest.createTask(task);
        final int taskId = inMemoryTaskManagerTest.getId();
        Task testTask = inMemoryTaskManagerTest.getTask(taskId);
        Assertions.assertNotNull(testTask, "Задача не найдена.");
        Assertions.assertEquals(task, testTask, "Задачи не совпадают.");
    }

    @Test
    void createEpic() {
        inMemoryTaskManagerTest.createEpic(epic);
        final int epicId = inMemoryTaskManagerTest.getId();
        Epic testEpic = inMemoryTaskManagerTest.getEpic(epicId);
        Assertions.assertNotNull(testEpic, "Эпик не найден.");
        Assertions.assertEquals(epic, testEpic, "Эпики не совпадают.");
    }

    @Test
    void createSubTask() {
        inMemoryTaskManagerTest.createEpic(epic);
        int idEpic = inMemoryTaskManagerTest.getId();
        SubTask subTask = new SubTask(inMemoryTaskManagerTest.getId() + 5, "Сабтаска1", "Описание Сабтаски 1", idEpic, TaskStatus.NEW, "08.06.2024 12:15", "15");
        inMemoryTaskManagerTest.createSubTask(subTask);
        final int subTaskId = inMemoryTaskManagerTest.getId();
        SubTask testSubTask = inMemoryTaskManagerTest.getSubTask(subTaskId);
        Assertions.assertNotNull(testSubTask, "Сабтаска не найдена.");
        Assertions.assertEquals(subTask, testSubTask, "Сабтаски не совпадают.");
    }

    @Test
    void checkSubtaskCannotBeMadeIntoItsOwnEpic() {
        SubTask subTask = new SubTask("Сабтаска1", "Описание Сабтаски 1", inMemoryTaskManagerTest.getId() + 5);
        SubTask subTask1 = inMemoryTaskManagerTest.createSubTask(subTask);
        Assertions.assertNull(subTask1);
    }

    @Test
    void CheckEpicStatusIfAllSubTaskWithStatusNEW() {
        inMemoryTaskManagerTest.createEpic(new Epic("Эпик1", "Описание Эпика1"));
        Integer idEpic = inMemoryTaskManagerTest.getId();
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаски 1", idEpic, "08.06.2024 12:15", "15"));
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска2", "Описание Сабтаски 2", idEpic, "08.06.2024 13:15", "15"));
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска2", "Описание Сабтаски 3", idEpic, "08.06.2024 14:15", "15"));
        Assertions.assertEquals(inMemoryTaskManagerTest.getEpic(idEpic).getTaskStatus(), TaskStatus.NEW, "Статус не NEW");
    }

    @Test
    void CheckEpicStatusIfAllSubTaskWithStatusDONE() {
        inMemoryTaskManagerTest.createEpic(new Epic("Эпик1", "Описание Эпика1"));
        Integer idEpic = inMemoryTaskManagerTest.getId();
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаски 1", idEpic, TaskStatus.DONE, "08.06.2024 12:15", "15"));
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска2", "Описание Сабтаски 2", idEpic, TaskStatus.DONE, "08.06.2024 13:15", "15"));
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска3", "Описание Сабтаски 3", idEpic, TaskStatus.DONE, "08.06.2024 14:15", "15"));
        Assertions.assertEquals(inMemoryTaskManagerTest.getEpic(idEpic).getTaskStatus(), TaskStatus.DONE, "Статус не DONE");
    }

    @Test
    void CheckEpicStatusIfSubTaskWithStatusDONEAndNEW() {
        inMemoryTaskManagerTest.createEpic(new Epic("Эпик1", "Описание Эпика1"));
        Integer idEpic = inMemoryTaskManagerTest.getId();
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаски 1", idEpic, TaskStatus.NEW, "08.06.2024 12:15", "15"));
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска2", "Описание Сабтаски 2", idEpic, TaskStatus.DONE, "08.06.2024 13:15", "15"));
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска3", "Описание Сабтаски 3", idEpic, TaskStatus.DONE, "08.06.2024 14:15", "15"));
        Assertions.assertEquals(inMemoryTaskManagerTest.getEpic(idEpic).getTaskStatus(), TaskStatus.IN_PROGRESS, "Статус не IN_PROGRESS");
    }

    @Test
    void CheckEpicStatusIfAllSubTaskWithStatusIN_PROGRESS() {
        inMemoryTaskManagerTest.createEpic(new Epic("Эпик1", "Описание Эпика1"));
        Integer idEpic = inMemoryTaskManagerTest.getId();
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаски 1", idEpic, TaskStatus.IN_PROGRESS, "08.06.2024 12:15", "15"));
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска2", "Описание Сабтаски 2", idEpic, TaskStatus.IN_PROGRESS, "08.06.2024 13:15", "15"));
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска3", "Описание Сабтаски 3", idEpic, TaskStatus.IN_PROGRESS, "08.06.2024 14:15", "15"));
        Assertions.assertEquals(inMemoryTaskManagerTest.getEpic(5).getTaskStatus(), TaskStatus.IN_PROGRESS, "Статус не IN_PROGRESS");
    }

    @Test
    void updateTask() {
        inMemoryTaskManagerTest.createTask(updateTask);
        Task task1 = new Task(inMemoryTaskManagerTest.getId(), "Задача1", "Обновление задачи1",
                TaskStatus.IN_PROGRESS, "08.06.2024 12:15", "15");
        inMemoryTaskManagerTest.updateTask(task1);
        Assertions.assertEquals(inMemoryTaskManagerTest.getTask(inMemoryTaskManagerTest.getId()), task1);

    }

    @Test
    void updateEpicAndSubtaskStatus() {
        inMemoryTaskManagerTest.createEpic(epic);
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаски 1, принадлежит эпику1", inMemoryTaskManagerTest.getId()));
        inMemoryTaskManagerTest.updateSubTask(new SubTask(inMemoryTaskManagerTest.getId(), "Сабтаска1",
                "Обновление сабтаски1", inMemoryTaskManagerTest.getId() - 5, TaskStatus.IN_PROGRESS, "08.06.2024 12:15", "15"));
        SubTask testSubTask = inMemoryTaskManagerTest.getSubTask((inMemoryTaskManagerTest.getId()));
        Epic testEpic = inMemoryTaskManagerTest.getEpic(inMemoryTaskManagerTest.getId() - 5);
        Assertions.assertEquals(testSubTask.getTaskStatus(), TaskStatus.IN_PROGRESS);
        Assertions.assertEquals(testEpic.getTaskStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void checkTasksWithGivenIdentifierAndGeneratedIdentifierDoNotConflictWithinTheManager() {
        Task task1 = new Task("Задача1", "Описание Задачи1", "08.06.2024 12:15", "15");
        inMemoryTaskManagerTest.createTask(task1);
        Task task2 = new Task(5, "Задача2", "Описание Задачи2", TaskStatus.NEW, "08.06.2024 13:15", "15");
        inMemoryTaskManagerTest.createTask(task2);
        Task testTask1 = inMemoryTaskManagerTest.getTask(inMemoryTaskManagerTest.getId());
        Assertions.assertNotEquals(task1, testTask1);
    }

    ;

    @Test
    void immutabilityOfTheTaskIsCheckedWhenAddingTheTaskToTheManager() {
        Task task1 = new Task("Задача1", "Описание Задачи1");
        inMemoryTaskManagerTest.createTask(task1);
        Task testTask1 = inMemoryTaskManagerTest.getTask(inMemoryTaskManagerTest.getId());
        Assertions.assertEquals(testTask1.getName(), task1.getName());
        Assertions.assertEquals(testTask1.getDescription(), task1.getDescription());
        Assertions.assertEquals(testTask1.getTaskStatus(), task1.getTaskStatus());
    }

    @Test
    void checkTaskOverlapTimeIntervalErrorMessage() {
        inMemoryTaskManagerTest.createTask(new Task("Задача1", "Описание Сабтаски 1", "08.06.2024 12:15", "90"));
        inMemoryTaskManagerTest.createTask(new Task("Задача2", "Пересекающиеся задача", "08.06.2024 13:15", "15"));
        Assertions.assertEquals(inMemoryTaskManagerTest.getAllTasks().size(), 1);
    }

    @Test
    void checkTaskOverlapTimeIntervalSuccess() {
        inMemoryTaskManagerTest.createTask(new Task("Задача1", "Описание Сабтаски 1", "08.06.2024 12:15", "15"));
        inMemoryTaskManagerTest.createTask(new Task("Задача2", "Не пересекающиеся задача", "08.06.2024 13:15", "15"));
        Assertions.assertEquals(inMemoryTaskManagerTest.getAllTasks().size(), 2);
    }

    @Test
    void checkTaskAndSubtaskOverlapTimeIntervalErrorMessage() {
        inMemoryTaskManagerTest.createTask(new Task("Задача1", "Описание Сабтаски 1", "08.06.2024 12:15", "90"));
        inMemoryTaskManagerTest.createEpic(new Epic("Эпик1", "Описание Эпика1"));
        int idEpic = inMemoryTaskManagerTest.getId();
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска1", "Пересекающиеся сабтаска", idEpic, TaskStatus.NEW, "08.06.2024 13:15", "15"));
        Assertions.assertEquals(inMemoryTaskManagerTest.getAllSubTask().size(), 0);
    }

    @Test
    void checkTaskAndSubtaskOverlapTimeIntervalSuccess() {
        inMemoryTaskManagerTest.createTask(new Task("Задача1", "Описание Сабтаски 1", "08.06.2024 12:15", "15"));
        inMemoryTaskManagerTest.createEpic(new Epic("Эпик1", "Описание Эпика1"));
        int idEpic = inMemoryTaskManagerTest.getId();
        inMemoryTaskManagerTest.createSubTask(new SubTask("Сабтаска1", "Не пересекающиеся сабтаска", idEpic, TaskStatus.NEW, "08.06.2024 13:15", "15"));
        Assertions.assertEquals(inMemoryTaskManagerTest.getAllSubTask().size(), 1);
    }
}