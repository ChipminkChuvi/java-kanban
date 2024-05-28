package taskmanager;

import exceptions.ManagerSaveException;
import taskmodel.Epic;
import taskmodel.SubTask;
import taskmodel.Task;
import taskmodel.TaskStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private String fileName;

    public FileBackedTaskManager(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8))) {
            bufferedWriter.write("id,type,name,status,description,epic" + "\n");
            for (Task task : getAllTasks()) {
                bufferedWriter.write(task.toString() + "\n");
            }
            for (Epic epic : getAllEpics()) {
                bufferedWriter.write(epic.toString() + "\n");
            }
            for (SubTask subTask : getAllSubTask()) {
                bufferedWriter.write(subTask.toString() + "\n");
            }
        } catch (IOException e) {
            RuntimeException exception = new ManagerSaveException("Файла " + fileName + " не существует", fileName);
            System.out.println(exception.getMessage());
        }

    }

    public void loadFromFile() {
        super.removeAllTask();
        super.removeAllEpic();
        super.removeAllSubTask();

        int id = 0;
        int counterId = 0;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();

                String[] lineSplit = line.split(",");

                if (!lineSplit[0].equals("id")) {
                    id = Integer.parseInt(lineSplit[0]);
                    setId(id - 5);
                }

                if (lineSplit[1].equals("TASK")) {
                    Task task = new Task(lineSplit[2], lineSplit[4], taskStatusFromString(lineSplit[3]));
                    super.createTask(task);
                } else if (lineSplit[1].equals("EPIC")) {
                    Epic epic = new Epic(lineSplit[2], lineSplit[4], taskStatusFromString(lineSplit[3]));
                    super.createEpic(epic);
                } else if (lineSplit[1].equals("SUBTASK")) {
                    SubTask subTask = new SubTask(lineSplit[2], lineSplit[4], Integer.parseInt(lineSplit[5]), taskStatusFromString(lineSplit[3]));
                    super.createSubTask(subTask);
                }

                if (counterId < getId()) {
                    counterId = getId();
                }
            }
            setId(counterId);

        } catch (IOException e) {
            RuntimeException exception = new ManagerSaveException("Файла " + fileName + " не существует", fileName);
            System.out.println(exception.getMessage());
        }
    }

    private TaskStatus taskStatusFromString(String status) {
        if (status.equals("NEW")) {
            return TaskStatus.NEW;
        } else if (status.equals("IN_PROGRESS")) {
            return TaskStatus.IN_PROGRESS;
        } else if (status.equals("DONE")) {
            return TaskStatus.DONE;
        }
        return null;
    }


    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
        return subTask;
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeAllSubTask() {
        super.removeAllSubTask();
        save();
    }

    @Override
    public void removeTaskById(Integer id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(Integer id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubTaskById(Integer id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public Map<Integer, Task> updateTask(Task task) {
        Map<Integer, Task> map = super.updateTask(task);
        save();
        return map;
    }

    @Override
    public Map<Integer, Epic> updateEpic(Epic epic) {
        Map<Integer, Epic> map = super.updateEpic(epic);
        save();
        return map;
    }

    @Override
    public Map<Integer, SubTask> updateSubTask(SubTask subTask) {
        Map<Integer, SubTask> map = super.updateSubTask(subTask);
        save();
        return map;
    }

    @Override
    public List<SubTask> getSubTaskFromEpic(Integer idEpic) {
        List<SubTask> list = super.getSubTaskFromEpic(idEpic);
        save();
        return list;
    }

    @Override
    public List<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public List<Epic> getAllEpics() {
        return super.getAllEpics();
    }

    @Override
    public List<SubTask> getAllSubTask() {
        return super.getAllSubTask();
    }

    @Override
    public Task getTask(Integer id) {
        return super.getTask(id);
    }

    @Override
    public Epic getEpic(Integer id) {
        return super.getEpic(id);
    }

    @Override
    public SubTask getSubTask(Integer id) {
        return super.getSubTask(id);
    }

    @Override
    public HistoryManager getHistoryManager() {
        return super.getHistoryManager();
    }
}

