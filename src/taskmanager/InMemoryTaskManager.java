package taskmanager;

import exceptions.OverlapDateTimeException;
import taskmodel.*;

import javax.annotation.processing.SupportedSourceVersion;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private static int id = 0;
    private Map<Integer, Task> onlyTask = new HashMap<>();
    private Map<Integer, Epic> onlyEpic = new HashMap<>();
    private Map<Integer, SubTask> onlySubTask = new HashMap<>();

    private Set<Task> priorityTask = new TreeSet<>((task1, task2) -> {
        if (task1.getStartTime().isAfter(task2.getStartTime())) {
            return 1;
        } else if (task1.getStartTime().isBefore(task2.getStartTime())) {
            return -1;
        } else if (task1.getStartTime().isEqual(task2.getStartTime())) {
            if (task1.getId() == task2.getId()) {
                return 0;
            } else if (task1.getId() > task2.getId()) {
                return 1;
            } else return -1;
        } else {
            return 1;
        }
    });

    private HistoryManager historyManager = Managers.getDefaultHistory();


    protected static void setId(int id) {
        InMemoryTaskManager.id = id;
    }

    protected static int incrementId() {
        id = id + 5;
        return id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Task createTask(Task task) {
        if (!overlapDateTime(task)) {
            Task task1 = new Task(incrementId(), task.getName(), task.getDescription(), task.getTaskStatus(), task.getStartTime(), task.getDuration());
            onlyTask.put(task1.getId(), task1);
            priorityTask.add(task1);
            return task1;
        } else
            return null;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic epic1 = new Epic(incrementId(), epic.getName(), epic.getDescription(), epic.getStartTime(), epic.getDuration());
        onlyEpic.put(epic1.getId(), epic1);
        priorityTask.add(epic1);
        return epic1;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        if (onlyEpic.containsKey(subTask.getIdEpic()) && !overlapDateTime(subTask)) {

            SubTask subTask1 = new SubTask(incrementId(), subTask.getName(), subTask.getDescription(), subTask.getIdEpic(), subTask.getTaskStatus(), subTask.getStartTime(), subTask.getDuration());
            if (subTask1.getId() == subTask1.getIdEpic()) {
                return null;
            }
            onlySubTask.put(subTask1.getId(), subTask1);
            Epic epic1 = onlyEpic.get(subTask.getIdEpic());
            epic1.getLinkedSubTask().add(subTask1.getId());
            calculateTimeAndDurationEpic(subTask.getIdEpic());
            updateStatusEpic(subTask);
            onlyEpic.put(subTask.getIdEpic(), epic1);
            priorityTask.add(subTask1);
            return subTask1;
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        for (Integer id : onlyTask.keySet()) {
            allTasks.add(onlyTask.get(id));
        }
        return allTasks;
    }

    @Override
    public List<Epic> getAllEpics() {
        List<Epic> allEpics = new ArrayList<>();
        for (Integer id : onlyEpic.keySet()) {
            allEpics.add(onlyEpic.get(id));
        }
        return allEpics;
    }

    @Override
    public List<SubTask> getAllSubTask() {
        List<SubTask> allSubTask = new ArrayList<>();
        for (Integer id : onlySubTask.keySet()) {
            allSubTask.add(onlySubTask.get(id));
        }
        return allSubTask;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void removeAllTask() {
        for (Task task : onlyTask.values()) {
            priorityTask.remove(task);
        }
        onlyTask.clear();
    }

    @Override
    public void removeAllEpic() {
        for (Epic epic : onlyEpic.values()) {
            priorityTask.remove(epic);
        }
        onlyEpic.clear();
    }

    @Override
    public void removeAllSubTask() {
        for (SubTask subTask : onlySubTask.values()) {
            priorityTask.remove(subTask);
        }
        onlySubTask.clear();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void removeTaskById(Integer id) {
        priorityTask.remove(onlyTask.get(id));
        onlyTask.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicById(Integer id) {
        Epic epic = onlyEpic.get(id);
        for (Integer i : epic.getLinkedSubTask()) {
            historyManager.remove(i);
        }
        priorityTask.remove(onlyEpic.get(id));
        onlyEpic.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubTaskById(Integer id) {
        Integer idEpic = onlySubTask.get(id).getIdEpic();
        onlyEpic.get(onlySubTask.get(id).getIdEpic()).getLinkedSubTask().remove(id);
        priorityTask.remove(onlySubTask.get(id));
        onlySubTask.remove(id);
        calculateTimeAndDurationEpic(idEpic);
        historyManager.remove(id);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Task getTask(Integer id) {
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
    public Map<Integer, Task> updateTask(Task task) {

        for (Integer key : onlyTask.keySet()) {
            if (key == task.getId()) {
                onlyTask.put(task.getId(), task);
                LoadSortSet();
                return onlyTask;
            }
        }
        return null;
    }

    @Override
    public Map<Integer, Epic> updateEpic(Epic epic) {
        for (Integer key : onlyEpic.keySet()) {
            if (key == epic.getId()) {
                onlyEpic.put(epic.getId(), epic);
                LoadSortSet();
                return onlyEpic;
            }
        }
        return null;
    }

    private void calculateTimeAndDurationEpic(Integer id) {
        if (!onlyEpic.get(id).getLinkedSubTask().isEmpty()) {

            onlyEpic.get(id).setDuration(Duration.ofMinutes(0));

            onlyEpic.get(id).setStartTime(onlySubTask.entrySet().stream()
                    .filter(key -> onlyEpic.containsKey(id))
                    .map(value -> value.getValue().getStartTime())
                    .min(Comparator.comparing(LocalDateTime::getHour)).stream().min(Comparator.comparing(LocalDateTime::getMinute)).get());

            onlyEpic.get(id).setEndTime(onlySubTask.entrySet().stream()
                    .filter(key -> onlyEpic.containsKey(id))
                    .map(value -> value.getValue().getEndTime())
                    .max(Comparator.comparing(LocalDateTime::getHour)).stream().min(Comparator.comparing(LocalDateTime::getMinute)).get());

            onlySubTask.entrySet().stream()
                    .filter(key -> onlyEpic.containsKey(id))
                    .map(value -> {
                        onlyEpic.get(id).setDuration(onlyEpic.get(id).getDuration().plus(value.getValue().getDuration()));
                        return value;
                    })
                    .collect(Collectors.toList());
        }
    }

    private void updateStatusEpic(SubTask subTask) {
        //Если переведу на лябду, то код будет нечитаемым
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
    public Map<Integer, SubTask> updateSubTask(SubTask subTask) {
        for (Integer key : onlySubTask.keySet()) {
            if (key == subTask.getId()) {
                onlySubTask.put(subTask.getId(), subTask);
                calculateTimeAndDurationEpic(subTask.getIdEpic());
                updateStatusEpic(subTask);
                LoadSortSet();
                return onlySubTask;
            }
        }
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public List<SubTask> getSubTaskFromEpic(Integer idEpic) {

        List<SubTask> subTaskFromEpic = onlySubTask.values().stream()
                .filter(subTask -> subTask.getIdEpic() == idEpic)
                .collect(Collectors.toList());
        return subTaskFromEpic;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void LoadSortSet() {
        priorityTask.clear();
        onlyTask.forEach((key, value) -> priorityTask.add(value));
        onlyEpic.forEach((key, value) -> priorityTask.add(value));
        onlySubTask.forEach((key, value) -> priorityTask.add(value));
    }

    @Override
    public Set<Task> getPriorityTaskView() {
        return priorityTask;
    }

    @Override
    public boolean overlapDateTime(Task task) {
        try {
            for (Task taskTree : priorityTask) {
                if (!taskTree.getClass().equals(Epic.class)) {
                    long overlap = Math.min(task.getEndTime().toEpochSecond(ZoneOffset.UTC),
                            taskTree.getEndTime().toEpochSecond(ZoneOffset.UTC)) - Math.max(task.getStartTime().
                            toEpochSecond(ZoneOffset.UTC), taskTree.getStartTime().toEpochSecond(ZoneOffset.UTC));
                    if (overlap >= 0) {
                        throw new OverlapDateTimeException("Задача " + task.getName() + " пересекается с другими " +
                                "задачами. Добавление отменено.");
                    }
                }
            }
        } catch (OverlapDateTimeException e) {
            System.out.println(e.getMessage());
            return true;
        }
        return false;
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
