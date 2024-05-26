import taskmanager.FileBackedTaskManager;
import taskmanager.InMemoryTaskManager;
import taskmanager.Managers;
import taskmanager.TaskManager;
import taskmodel.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileBacked();
        fileBackedTaskManager.loadFromFile();

        fileBackedTaskManager.createTask(new Task("Задача4", "Описание Задачи4"));
        fileBackedTaskManager.createEpic(new Epic("Эпик3", "Описание Эпика4"));
        fileBackedTaskManager.createSubTask(new SubTask("Сабтаска4", "Описание Сабтаски 4", 20));


//        fileBackedTaskManager.createTask(new Task("Задача1", "Описание Задачи1"));
//        fileBackedTaskManager.createTask(new Task("Задача2", "Описание Задачи2"));
//        fileBackedTaskManager.createEpic(new Epic("Эпик1", "Описание Эпика1"));
//        fileBackedTaskManager.createEpic(new Epic("Эпик2", "Описание Эпика2"));
//        fileBackedTaskManager.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаски 1", 15));
//        fileBackedTaskManager.createSubTask(new SubTask("Сабтаска2", "Описание Сабтаски 2", 15));
//        fileBackedTaskManager.createSubTask(new SubTask("Сабтаска3", "Описание Сабтаски 3", 20));
//
//        fileBackedTaskManager.createTask(new Task("Задача3", "Описание Задачи3"));
//
//        fileBackedTaskManager.updateTask(new Task(10,"Задача1","Обновление задачи1",TaskStatus.IN_PROGRESS));
//        fileBackedTaskManager.updateSubTask(new SubTask(25,"Сабтаска1","Обновление сабтаски1",15,TaskStatus.DONE));
//        fileBackedTaskManager.updateSubTask(new SubTask(30,"Сабтаска2","Обновление сабтаски2",15,TaskStatus.DONE));
    }

}
