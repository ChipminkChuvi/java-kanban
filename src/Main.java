import taskmanager.FileBackedTaskManager;
import taskmanager.InMemoryTaskManager;
import taskmanager.Managers;
import taskmanager.TaskManager;
import taskmodel.*;

import javax.swing.plaf.TableHeaderUI;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileBacked();
        fileBackedTaskManager.loadFromFile();

        TaskManager taskManager = Managers.getDefault();


        taskManager.createTask(new Task("Задача1", "Описание Задачи1", "08.06.2024 12:15", "30"));
        taskManager.createTask(new Task("Задача2", "Описание Задачи2", "08.06.2024 16:15", "30"));

        System.out.println(taskManager.getPriorityTaskView());

        taskManager.createEpic(new Epic("Эпик1", "Описание Эпика1"));
        taskManager.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаски 1", 15, "08.06.2024 15:20", "15"));
        System.out.println("Сообщение о пересечении");
        taskManager.createSubTask(new SubTask("Сабтаска2", "Описание Сабтаски 2", 15, "08.06.2024 16:20", "15"));
        taskManager.createSubTask(new SubTask("Сабтаска3", "Описание Сабтаски 3", 15, "08.06.2024 17:20", "15"));

        System.out.println(taskManager.getPriorityTaskView());
    }

    public static void createTestTask(FileBackedTaskManager fileBackedTaskManager) {
        fileBackedTaskManager.createTask(new Task("Задача1", "Описание Задачи1", "08.06.2024 11:15", "30"));
        fileBackedTaskManager.createTask(new Task("Задача2", "Описание Задачи2", "08.06.2024 13:15", "30"));
        fileBackedTaskManager.createEpic(new Epic("Эпик1", "Описание Эпика1"));
        fileBackedTaskManager.createEpic(new Epic("Эпик2", "Описание Эпика2"));
        fileBackedTaskManager.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаски 1", 15, "08.06.2024 12:15", "30"));
        fileBackedTaskManager.createSubTask(new SubTask("Сабтаска2", "Описание Сабтаски 2", 15, "08.06.2024 15:20", "30"));
        fileBackedTaskManager.createSubTask(new SubTask("Сабтаска3", "Описание Сабтаски 3", 20, "08.06.2024 16:25", "30"));
        fileBackedTaskManager.createTask(new Task("Задача3", "Описание Задачи3", "08.06.2024 17:15", "60"));
        fileBackedTaskManager.updateTask(new Task(5, "Задача1", "Обновление задачи1", TaskStatus.IN_PROGRESS, "08.06.2024 11:15", "30"));
        fileBackedTaskManager.updateSubTask(new SubTask(25, "Сабтаска1", "Обновление сабтаски1", 15, TaskStatus.DONE, "08.06.2024 12:15", "30"));
        System.out.println();
    }

}
