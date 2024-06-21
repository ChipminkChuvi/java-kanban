import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.DurationAdapter;
import server.HttpTaskServer;
import server.LocalDateTimeAdapter;
import server.ManagerHandler;
import taskmanager.InMemoryTaskManager;
import taskmodel.Epic;
import taskmodel.SubTask;
import taskmodel.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskManagerDeleteTest {
    private InMemoryTaskManager manager = new InMemoryTaskManager();
    private HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

    private ManagerHandler managerHandler = new ManagerHandler();

    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public HttpTaskManagerDeleteTest() throws IOException {
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
    public void testDeleteTaskById() throws IOException, InterruptedException {
        manager.createTask(new Task("Задача1", "Описание Задачи1", "08.06.2024 08:15", "30"));
        manager.createTask(new Task("Задача2", "Описание Задачи2", "08.06.2024 09:15", "30"));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/tasks/" + (manager.getId() - 5));
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        Assertions.assertEquals(1, tasksFromManager.size(), "Задача не удалилась");
        Assertions.assertNull(manager.getTask(manager.getId() - 5));
    }

    @Test
    public void testDeleteAllTasks() throws IOException, InterruptedException {
        manager.createTask(new Task("Задача1", "Описание Задачи1", "08.06.2024 08:15", "30"));
        manager.createTask(new Task("Задача2", "Описание Задачи2", "08.06.2024 09:15", "30"));
        manager.createTask(new Task("Задача2", "Описание Задачи2", "08.06.2024 10:15", "30"));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/tasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        Assertions.assertEquals(0, tasksFromManager.size(), "Задачи не удалились");
    }

    @Test
    public void testDeleteEpicById() throws IOException, InterruptedException {
        manager.createEpic(new Epic("Эпик", "Описание Эпика"));
        manager.createEpic(new Epic("Эпик1", "Описание Эпика1"));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/epics/" + (manager.getId() - 5));
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        List<Epic> tasksFromManager = manager.getAllEpics();
        Assertions.assertEquals(1, tasksFromManager.size(), "Задача не удалилась");
        Assertions.assertNull(manager.getEpic(manager.getId() - 5));
    }

    @Test
    public void testDeleteAllEpics() throws IOException, InterruptedException {
        manager.createEpic(new Epic("Эпик", "Описание Эпика"));
        manager.createEpic(new Epic("Эпик1", "Описание Эпика1"));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/epics");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        List<Epic> tasksFromManager = manager.getAllEpics();
        Assertions.assertEquals(0, tasksFromManager.size(), "Задачи не удалились");
    }

    @Test
    public void testDeleteSubTaskById() throws IOException, InterruptedException {
        manager.createEpic(new Epic("Эпик", "Описание Эпика"));
        int idEpic = manager.getId();
        manager.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаска1", idEpic, "08.06.2024 08:15", "30"));
        manager.createSubTask(new SubTask("Сабтаска2", "Описание Сабтаска2", idEpic, "08.06.2024 09:15", "30"));
        manager.createSubTask(new SubTask("Сабтаска3", "Описание Сабтаска3", idEpic, "08.06.2024 10:15", "30"));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/subtasks/" + (manager.getId() - 5));
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        List<SubTask> tasksFromManager = manager.getAllSubTask();
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(2, tasksFromManager.size(), "Задача не удалилась");
        Assertions.assertNull(manager.getTask(manager.getId() - 5));
    }

    @Test
    public void testDeleteAllSubTasks() throws IOException, InterruptedException {
        manager.createEpic(new Epic("Эпик", "Описание Эпика"));
        int idEpic = manager.getId();
        manager.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаска1", idEpic, "08.06.2024 08:15", "30"));
        manager.createSubTask(new SubTask("Сабтаска2", "Описание Сабтаска2", idEpic, "08.06.2024 09:15", "30"));
        manager.createSubTask(new SubTask("Сабтаска3", "Описание Сабтаска3", idEpic, "08.06.2024 10:15", "30"));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/subtasks");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        List<SubTask> tasksFromManager = manager.getAllSubTask();
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(0, tasksFromManager.size(), "Задачи не удалились");
    }

}
