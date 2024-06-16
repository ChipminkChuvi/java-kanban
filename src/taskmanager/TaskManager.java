package taskmanager;

import taskmodel.Epic;
import taskmodel.SubTask;
import taskmodel.Task;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaskManager<I> {

    int getId();

    Task createTask(Task task);

    Epic createEpic(Epic epic);

    SubTask createSubTask(SubTask subTask);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTask();

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
    Map<Integer, Task> updateTask(Task task);

    Map<Integer, Epic> updateEpic(Epic epic);

    Map<Integer, SubTask> updateSubTask(SubTask subTask);

    List<SubTask> getSubTaskFromEpic(Integer idEpic);

    HistoryManager getHistoryManager();
    void LoadSortSet();

    Set<Task> getPriorityTaskView();

    boolean overlapDateTime(Task task);
}

