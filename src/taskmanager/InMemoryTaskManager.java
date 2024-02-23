package taskmanager;

import taskmodel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    private static int id = 0;
    private HashMap<Integer, Task> onlyTask = new HashMap<>();
    private HashMap<Integer, Epic> onlyEpic = new HashMap<>();
    private HashMap<Integer, SubTask> onlySubTask = new HashMap<>();

    private HistoryManager historyManager = Managers.getDefaultHistory();


    private static int setId() {
        id = id + 5;
        return id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Task createTask(Task task) {
        Task task1 = new Task(setId(), task.getName(), task.getDescription());
        onlyTask.put(task1.getId(), task1);
        return task1;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic epic1 = new Epic(setId(), epic.getName(), epic.getDescription());
        onlyEpic.put(epic1.getId(), epic1);
        return epic1;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        if (onlyEpic.containsKey(subTask.getIdEpic())) {

            SubTask subTask1 = new SubTask(setId(), subTask.getName(), subTask.getDescription(), subTask.getIdEpic());
            if (subTask1.getId() == subTask1.getIdEpic()){
                return null;
            }

            onlySubTask.put(subTask1.getId(), subTask1);

            Epic epic1 = onlyEpic.get(subTask.getIdEpic());
            epic1.getLinkedSubTask().add(subTask1.getId());
            onlyEpic.put(subTask.getIdEpic(), epic1);
            return subTask1;
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Integer id : onlyTask.keySet()) {
            allTasks.add(onlyTask.get(id));
        }
        return allTasks;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (Integer id : onlyEpic.keySet()) {
            allEpics.add(onlyEpic.get(id));
        }
        return allEpics;
    }

    @Override
    public ArrayList<SubTask> getAllSubTask() {
        ArrayList<SubTask> allSubTask = new ArrayList<>();
        for (Integer id : onlySubTask.keySet()) {
            allSubTask.add(onlySubTask.get(id));
        }
        return allSubTask;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void removeAllTask() {
        onlyTask.clear();
    }

    @Override
    public void removeAllEpic() {
        onlyEpic.clear();
    }

    @Override
    public void removeAllSubTask() {
        onlySubTask.clear();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void removeTaskById(Integer id) {
        onlyTask.remove(id);
    }

    @Override
    public void removeEpicById(Integer id) {
        onlyEpic.remove(id);
    }

    @Override
    public void removeSubTaskById(Integer id) {
        SubTask subTask1 = onlySubTask.get(id);
        Epic newEpic = onlyEpic.get(subTask1.getIdEpic());
        newEpic.getLinkedSubTask().remove(id);
        onlyEpic.put(subTask1.getIdEpic(),newEpic);
        onlySubTask.remove(id);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Task getTask(Integer id) {;
        historyManager.add(onlyTask.get(id));
        return onlyTask.get(id);
    }

    @Override
    public Epic getEpic(Integer id) {
        historyManager.add(onlyEpic.get(id));
        return onlyEpic.get(id);
    }

    @Override
    public SubTask getSubTask(Integer id) {
        historyManager.add(onlySubTask.get(id));
        return onlySubTask.get(id);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public HashMap<Integer, Task> updateTask(Task task) {
        for (Integer key : onlyTask.keySet()) {
            if (key == task.getId()) {
                onlyTask.put(task.getId(), task);
                return onlyTask;
            }
        }
        return null;
    }

    @Override
    public HashMap<Integer, Epic> updateEpic(Epic epic) {
        for (Integer key : onlyEpic.keySet()) {
            if (key == epic.getId()) {
                onlyEpic.put(epic.getId(), epic);
                return onlyEpic;
            }
        }
        return null;
    }

    private void updateStatusEpic(SubTask subTask) {
        Epic epic1;
        epic1 = onlyEpic.get(subTask.getIdEpic());
        int countDone = 0;
        int counInNew = 0;
        for (Integer keyArr : epic1.getLinkedSubTask()) {
            if (onlySubTask.get(keyArr).getTaskStatus() == TaskStatus.NEW) {
                counInNew++;
            } else if (onlySubTask.get(keyArr).getTaskStatus() == TaskStatus.DONE) {
                countDone++;
            }
        }
        if (countDone == epic1.getLinkedSubTask().size()) {
            epic1.setTaskStatus(TaskStatus.DONE);
            updateEpic(epic1);
        } else if (counInNew == epic1.getLinkedSubTask().size()) {
            epic1.setTaskStatus(TaskStatus.NEW);
            updateEpic(epic1);
        } else {
            epic1.setTaskStatus(TaskStatus.IN_PROGRESS);
            updateEpic(epic1);
        }
    }


    @Override
    public HashMap<Integer, SubTask> updateSubTask(SubTask subTask) {
        for (Integer key : onlySubTask.keySet()) {
            if (key == subTask.getId()) {
                onlySubTask.put(subTask.getId(), subTask);
                updateStatusEpic(subTask);
                return onlySubTask;
            }
        }
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ArrayList<SubTask> getSubTaskFromEpic(Integer idEpic) {
        ArrayList<SubTask> subTaskFromEpic = new ArrayList<>();
        Epic epic = onlyEpic.get(idEpic);

        for (Integer i : epic.getLinkedSubTask()) {
            subTaskFromEpic.add(onlySubTask.get(i));
        }
        return subTaskFromEpic;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public String toString() {
        return "taskmanager.TaskManager{" +
                "onlyTask=" + onlyTask +
                ", onlyEpic=" + onlyEpic +
                ", onlySubTask=" + onlySubTask +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) object;
        return Objects.equals(onlyTask, that.onlyTask) && Objects.equals(onlyEpic, that.onlyEpic) && Objects.equals(onlySubTask, that.onlySubTask) && Objects.equals(historyManager, that.historyManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(onlyTask, onlyEpic, onlySubTask, historyManager);
    }
}
