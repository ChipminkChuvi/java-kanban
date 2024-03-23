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
        taskManager.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаски 1, принадлежит эпику1", 15));
        taskManager.createSubTask(new SubTask("Сабтаска2", "Описание Сабтаски 2, принадлежит эпику1", 15));
        taskManager.createSubTask(new SubTask("Сабтаска3", "Описание Сабтаски 3, принадлежит эпику1", 15));

        taskManager.getTask(10);
        taskManager.getEpic(20);
        taskManager.getSubTask(30);
        taskManager.getSubTask(25);
        taskManager.getTask(5);
        taskManager.getEpic(15);
        taskManager.getSubTask(35);
        System.out.println("Вывод истории " + taskManager.getHistoryManager().getHistory());
        taskManager.getTask(10);
        System.out.println("Переписалась запись с id=10'\n'" + taskManager.getHistoryManager().getHistory() + '\n');
        taskManager.getEpic(20);
        System.out.println("Переписалась запись с id=20'\n'" + taskManager.getHistoryManager().getHistory() + '\n');
        taskManager.getSubTask(35);
        System.out.println("Переписалась запись с id=35'\n'" + taskManager.getHistoryManager().getHistory() + '\n');

        taskManager.removeTaskById(5);
        System.out.println("Удалилась задача с id=5'\n'" + taskManager.getHistoryManager().getHistory() + '\n');

        taskManager.removeEpicById(15);
        System.out.println("Удалился Epic с id=15 и три подзадачи внутри эпика с ID=25,30,35'\n'" + taskManager.getHistoryManager().getHistory());
    }

}
