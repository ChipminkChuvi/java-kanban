import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import server.DurationAdapter;
import server.LocalDateTimeAdapter;
import server.ManagerHandler;
import taskmanager.InMemoryTaskManager;
import taskmodel.Epic;
import taskmodel.SubTask;
import taskmodel.Task;
import taskmodel.TaskStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskManagerPostTest {
    private InMemoryTaskManager manager = new InMemoryTaskManager();
    private HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

    private ManagerHandler managerHandler = new ManagerHandler();

    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public HttpTaskManagerPostTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        managerHandler.setTaskManager(manager);
        manager.removeAllTask();
        manager.removeAllEpic();
        manager.removeAllSubTask();
        httpServer.createContext("/manage", managerHandler);
        httpServer.start();
    }

    @AfterEach
    public void shutDown() {
        httpServer.stop(1);
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {

        Task task = new Task("Задача1", "Описание Задачи1", "08.06.2024 11:15", "30");
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/manage/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        Assertions.assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Assertions.assertEquals("Задача1", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик1", "Описание Эпик1");
        String taskJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/manage/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        List<Epic> tasksFromManager = manager.getAllEpics();
        Assertions.assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Assertions.assertEquals("Эпик1", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        manager.createEpic(new Epic("Эпик1", "Описание Эпик1"));
        SubTask subTask = new SubTask("Сабтаска1", "Описание Сабтаски 1", manager.getId(), "08.06.2024 12:15", "30");
        String taskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/manage/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        List<SubTask> tasksFromManager = manager.getAllSubTask();
        Assertions.assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Assertions.assertEquals("Сабтаска1", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {

        manager.createTask(new Task("Задача1", "Описание Задачи1", "08.06.2024 15:15", "30"));
        int id = manager.getId();
        Task task = new Task(id, "Задача1", "Описание Задачи1", TaskStatus.IN_PROGRESS, "08.06.2024 11:15", "30");
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/manage/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        Assertions.assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Assertions.assertEquals("Задача1", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, tasksFromManager.getFirst().getTaskStatus(), "Некорректный статус задачи");
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {
        manager.createEpic(new Epic("Эпик1", "Описание эпика1"));
        int idEpic = manager.getId();

        manager.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаски 1", idEpic, "08.06.2024 12:15", "30"));
        manager.createSubTask(new SubTask("Сабтаска2", "Описание Сабтаски 2", idEpic, "08.06.2024 13:15", "30"));
        manager.createSubTask(new SubTask("Сабтаска3", "Описание Сабтаски 3", idEpic, "08.06.2024 14:15", "30"));
        manager.updateSubTask(new SubTask(manager.getId(), "Сабтаска3", "Описание Сабтаски 3", idEpic, TaskStatus.DONE, "08.06.2024 14:15", "30"));

        Epic epic = new Epic("Эпик1", "Изменения в Эпике1");

        String taskJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/manage/epics/" + idEpic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        List<Epic> tasksFromManager = manager.getAllEpics();
        Assertions.assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Assertions.assertEquals(3, tasksFromManager.getFirst().getLinkedSubTask().size());
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, tasksFromManager.getFirst().getTaskStatus());
        Assertions.assertEquals("Изменения в Эпике1", tasksFromManager.getFirst().getDescription(), "Некорректное описание задачи");
    }

    @Test
    public void testUpdateSubTask() throws IOException, InterruptedException {
        manager.createEpic(new Epic("Эпик1", "Описание эпика1"));
        int idEpic = manager.getId();
        manager.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаски 1", idEpic, "08.06.2024 16:15", "30"));

        SubTask subTask = new SubTask("Сабтаска1", "Обновление сабтаски 1", idEpic, TaskStatus.IN_PROGRESS, "08.06.2024 16:15", "30");
        String taskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/manage/subtasks/" + manager.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        List<SubTask> tasksFromManager = manager.getAllSubTask();
        Assertions.assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Assertions.assertEquals("Обновление сабтаски 1", tasksFromManager.getFirst().getDescription(), "Некорректное описание задачи");
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, tasksFromManager.getFirst().getTaskStatus(), "Некорректный статус задачи");
    }
}
