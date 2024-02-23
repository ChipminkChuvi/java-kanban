package taskmanager;

import javax.net.ssl.HostnameVerifier;

public class Managers {

    public static TaskManager getDefault(){
        TaskManager taskManager = new InMemoryTaskManager();
        return taskManager;
    }

    public static HistoryManager getDefaultHistory (){
        HistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }
}
