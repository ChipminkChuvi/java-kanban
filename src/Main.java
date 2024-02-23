import taskmanager.InMemoryTaskManager;
import taskmanager.Managers;
import taskmanager.TaskManager;
import taskmodel.*;
public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");


        TaskManager taskManager = Managers.getDefault();

        taskManager = new InMemoryTaskManager();
        //Создание Задач/Эпиков/Сабтасок
        taskManager.createTask(new Task("Задача1", "Описание Задачи1"));

        taskManager.createTask(new Task("Задача2", "Описание Задачи2"));

        taskManager.createEpic(new Epic("Эпик1", "Описание Эпика1"));
        taskManager.createEpic(new Epic("Эпик2", "Описание Эпика2"));

        taskManager.createSubTask(new SubTask("Сабтаска1","Описание Сабтаски 1, принадлежит эпику1",15));
        taskManager.createSubTask(new SubTask("Сабтаска2","Описание Сабтаски 2, принадлежит эпику2",20));
        taskManager.createSubTask(new SubTask("Сабтаска3","Описание Сабтаски 3, принадлежит эпику2",20));
        System.out.println("Вывод всех Задач/Эпиков/Сабтасок");
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTask());
        //Изменение статусов Задач и сабтасок
        taskManager.updateTask(new Task(5,"Задача1","Обновление задачи1",TaskStatus.IN_PROGRESS));
        taskManager.updateTask(new Task(10,"Задача2","Обновление задачи2",TaskStatus.DONE));
        taskManager.updateSubTask(new SubTask(25,"Сабтаска1","Обновление сабтаски1",15,TaskStatus.DONE));
        taskManager.updateSubTask(new SubTask(30,"Сабтаска2","Обновление сабтаски2",20,TaskStatus.DONE));
        taskManager.updateSubTask(new SubTask(35,"Сабтаска3","Обновление сабтаски3",20,TaskStatus.IN_PROGRESS));
        System.out.println();
        System.out.println("Вывод задач с измененным статусом");
        //Вывод задач с измененным статусом
        System.out.println(taskManager.getTask(5));
        System.out.println(taskManager.getTask(10));
        System.out.println();
        System.out.println("Вывод сабтасок с измененным статусом");
        //Вывод сабтасок с измененным статусом
        System.out.println(taskManager.getSubTask(25));
        System.out.println(taskManager.getSubTask(30));
        System.out.println();
        System.out.println("Вывод эпиков с измененным статусом");
        //Вывод эпиков с измененным статусом
        System.out.println(taskManager.getEpic(15));
        System.out.println(taskManager.getEpic(20));
////////////////////////////////////////////////
        System.out.println(taskManager.getEpic(15));
        System.out.println(taskManager.getEpic(20));
        System.out.println(taskManager.getSubTask(25));
        System.out.println(taskManager.getSubTask(30));
        System.out.println(taskManager.getEpic(15));
        System.out.println(taskManager.getTask(10));

        System.out.println(taskManager.getHistoryManager().getHistory());

        //Удаление задачи и сабтаски
        taskManager.removeTaskById(5);
        taskManager.removeSubTaskById(30);
        System.out.println();
        System.out.println("Вывод всех тасок/сабтасок после удаление а также эпика у которого из массива удалилась сабтаска");
        //Вывод всех тасок/сабтасок после удаление а также эпика у которого из массива удалилась сабтаска
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubTask());
        System.out.println(taskManager.getEpic(20));


    }

}
