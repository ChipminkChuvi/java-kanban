package taskmanager;


public class Managers {

    public static TaskManager getDefault() {
        TaskManager taskManager = new InMemoryTaskManager();
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }

    public static FileBackedTaskManager getDefaultFileBacked() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("Backup.csv");
        return fileBackedTaskManager;
    }
}
