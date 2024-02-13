import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int id = 0;
    private HashMap<Integer, Task> onlyTask = new HashMap<>();
    private HashMap<Integer, Epic> onlyEpic = new HashMap<>();
    private HashMap<Integer, SubTask> onlySubTask = new HashMap<>();

    public static int generateId() {
        id = id + 5;
        return id;
    }

    public static int getId() {
        return id;
    }

    public Task createTask(Task task) {
        Task task1 = new Task(generateId(), task.name, task.description);
        onlyTask.put(task1.getId(), task1);
        return task1;
    }

    public Epic createEpic(Epic epic) {
        Epic epic1 = new Epic(generateId(), epic.name, epic.description);
        onlyEpic.put(epic1.getId(), epic1);
        return epic1;
    }

    public SubTask createSubTask(SubTask subTask) {
        if (onlyEpic.containsKey(subTask.getIdEpic())) {

            SubTask subTask1 = new SubTask(generateId(), subTask.name, subTask.description, subTask.getIdEpic());
            onlySubTask.put(subTask1.getId(), subTask1);

            Epic epic1 = onlyEpic.get(subTask.getIdEpic());
            epic1.linkedSubTask.add(subTask1.getId());
            onlyEpic.put(subTask.getIdEpic(), epic1);
            return subTask1;
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Integer id : onlyTask.keySet()) {
            allTasks.add(onlyTask.get(id));
        }
        return allTasks;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (Integer id : onlyEpic.keySet()) {
            allEpics.add(onlyEpic.get(id));
        }
        return allEpics;
    }

    public ArrayList<SubTask> getAllSubTask() {
        ArrayList<SubTask> allSubTask = new ArrayList<>();
        for (Integer id : onlySubTask.keySet()) {
            allSubTask.add(onlySubTask.get(id));
        }
        return allSubTask;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void removeAllTask() {
        onlyTask.clear();
    }

    public void removeAllEpic() {
        onlyEpic.clear();
    }

    public void removeAllSubTask() {
        onlySubTask.clear();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void removeTaskById(Integer id) {
        onlyTask.remove(id);
    }

    public void removeEpicById(Integer id) {
        onlyEpic.remove(id);
    }

    public void removeSubTaskById(Integer id) {
        onlySubTask.remove(id);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Task getTask(Integer id) {

        return onlyTask.get(id);
    }

    public Epic getEpic(Integer id) {
        return onlyEpic.get(id);
    }

    public SubTask getSubTask(Integer id) {
        return onlySubTask.get(id);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public HashMap<Integer, Task> updateTask(Task task) {
        for (Integer key : onlyTask.keySet()) {
            if (key == task.getId()) {
                onlyTask.put(task.getId(), task);
                return onlyTask;
            }
        }
        return null;
    }

    public HashMap<Integer, Epic> updateEpic(Epic epic) {
        for (Integer key : onlyEpic.keySet()) {
            if (key == epic.getId()) {
                onlyEpic.put(epic.getId(), epic);
                return onlyEpic;
            }
        }
        return null;
    }

    public HashMap<Integer, SubTask> updateSubTask(SubTask subTask) {
        for (Integer key : onlySubTask.keySet()) {
            if (key == subTask.getId()) {
                onlySubTask.put(subTask.getId(), subTask);

                Epic epic1;
                epic1 = onlyEpic.get(subTask.getIdEpic());
                int countDone = 0;
                int counInNew = 0;
                for (Integer keyArr : epic1.linkedSubTask) {
                    if (onlySubTask.get(keyArr).getTaskStatus() == TaskStatus.NEW) {
                        counInNew++;
                    } else if (onlySubTask.get(keyArr).getTaskStatus() == TaskStatus.DONE) {
                        countDone++;
                    }
                }
                if (countDone == epic1.linkedSubTask.size()) {
                    epic1.setTaskStatus(TaskStatus.DONE);
                    updateEpic(epic1);
                } else if (counInNew == epic1.linkedSubTask.size()) {
                    epic1.setTaskStatus(TaskStatus.NEW);
                    updateEpic(epic1);
                } else {
                    epic1.setTaskStatus(TaskStatus.IN_PROGRESS);
                    updateEpic(epic1);
                }
                return onlySubTask;
            }
            ;
        }
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<SubTask> getSubTaskFromEpic(Integer idEpic) {
        ArrayList<SubTask> subTaskFromEpic = new ArrayList<>();
        Epic epic = onlyEpic.get(idEpic);

        for (Integer i : epic.linkedSubTask) {
            subTaskFromEpic.add(onlySubTask.get(i));
        }
        return subTaskFromEpic;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public String toString() {
        return "TaskManager{" +
                "onlyTask=" + onlyTask +
                ", onlyEpic=" + onlyEpic +
                ", onlySubTask=" + onlySubTask +
                '}';
    }
}
