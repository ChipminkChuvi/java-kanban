package taskmanager;

import taskmodel.Epic;
import taskmodel.SubTask;
import taskmodel.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager<I> {

    int getId();
    Task createTask(Task task);

    Epic createEpic(Epic epic);

    SubTask createSubTask(SubTask subTask);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<SubTask> getAllSubTask();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    void removeAllTask();

    void removeAllEpic();

    void removeAllSubTask();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    void removeTaskById(Integer id);

    void removeEpicById(Integer id);

    void removeSubTaskById(Integer id);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    Task getTask(Integer id);

    Epic getEpic(Integer id);

    SubTask getSubTask(Integer id);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    HashMap<Integer, Task> updateTask(Task task);

    HashMap<Integer, Epic> updateEpic(Epic epic);

    HashMap<Integer, SubTask> updateSubTask(SubTask subTask);

    ArrayList<SubTask> getSubTaskFromEpic(Integer idEpic);

     HistoryManager getHistoryManager();
}

