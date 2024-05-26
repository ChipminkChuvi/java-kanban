import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import taskmanager.FileBackedTaskManager;
import taskmodel.Epic;
import taskmodel.SubTask;
import taskmodel.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FileBackedTaskManagerTest {

    private FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("test.csv");
    private static File file;

    private static File dir = new File(System.getProperty("user.dir"));

    @BeforeAll
    public static void beforeAll() {
        try {
            file = File.createTempFile("test", ".csv", dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void AfterAll() {
        file.deleteOnExit();
    }

    @Test
    void checkEmptyFileForRead() {
        fileBackedTaskManager.setFileName(file.getName());
        fileBackedTaskManager.loadFromFile();
        Assertions.assertEquals(0, fileBackedTaskManager.getAllTasks().size(), "Файл содержит записи");
        Assertions.assertEquals(0, fileBackedTaskManager.getAllEpics().size(), "Файл содержит записи");
        Assertions.assertEquals(0, fileBackedTaskManager.getAllSubTask().size(), "Файл содержит записи");

    }

    @Test
    void checkEmptyFileForWrite() {
        fileBackedTaskManager.setFileName(file.getName());
        fileBackedTaskManager.createTask(new Task("Задача1", "Описание Задачи1"));
        fileBackedTaskManager.createEpic(new Epic("Эпик1", "Описание Эпика1"));
        fileBackedTaskManager.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаски 1", 10));
        Assertions.assertEquals(1, fileBackedTaskManager.getAllTasks().size());
        Assertions.assertEquals(1, fileBackedTaskManager.getAllEpics().size());
        Assertions.assertEquals(1, fileBackedTaskManager.getAllSubTask().size());
        fileBackedTaskManager.loadFromFile();
        Assertions.assertEquals(1, fileBackedTaskManager.getAllTasks().size(), "Произошла ошибка загрузки");
        Assertions.assertEquals(1, fileBackedTaskManager.getAllEpics().size(), "Произошла ошибка загрузки");
        Assertions.assertEquals(1, fileBackedTaskManager.getAllSubTask().size(), "Произошла ошибка загрузки");
    }
}
