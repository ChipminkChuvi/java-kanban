import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.DurationAdapter;
import server.LocalDateTimeAdapter;
import server.ManagerHandler;
import server.UserListTypeToken;
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

public class HttpTaskManagerGetTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();
    HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

    ManagerHandler managerHandler = new ManagerHandler();

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public HttpTaskManagerGetTest() throws IOException {
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
    public void testGetAllTask() throws IOException, InterruptedException {
        manager.createTask(new Task("Задача1", "Описание Задачи1", "08.06.2024 08:15", "30"));
        manager.createTask(new Task("Задача2", "Описание Задачи2", "08.06.2024 09:15", "30"));
        manager.createTask(new Task("Задача3", "Описание Задачи3", "08.06.2024 10:15", "30"));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/tasks");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        List<Task> taskList = gson.fromJson(response.body(), new UserListTypeToken().getType());
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals(3, taskList.size(), "Некорректное количество задач");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        manager.createTask(new Task("Задача1", "Описание Задачи1", "08.06.2024 08:15", "30"));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/tasks/" + manager.getId());

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Task task = gson.fromJson(response.body(), Task.class);
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Задача1", task.getName(), "Некорректное название таски");
    }

    @Test
    public void testGetAllSubtask() throws IOException, InterruptedException {
        manager.createEpic(new Epic("Эпик", "Описание Эпика"));
        int idEpic = manager.getId();
        manager.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаска1", idEpic, "08.06.2024 08:15", "30"));
        manager.createSubTask(new SubTask("Сабтаска2", "Описание Сабтаска2", idEpic, "08.06.2024 09:15", "30"));
        manager.createSubTask(new SubTask("Сабтаска3", "Описание Сабтаска3", idEpic, "08.06.2024 10:15", "30"));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/subtasks");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        List<Task> taskList = gson.fromJson(response.body(), new UserListTypeToken().getType());
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals(3, taskList.size(), "Некорректное количество задач");
    }

    @Test
    public void testGetSubTaskById() throws IOException, InterruptedException {
        manager.createEpic(new Epic("Эпик", "Описание Эпика"));
        int idEpic = manager.getId();
        manager.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаска1", idEpic, "08.06.2024 08:15", "30"));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/subtasks/" + manager.getId());

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        SubTask task = gson.fromJson(response.body(), SubTask.class);
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Сабтаска1", task.getName(), "Некорректное название таски");
    }

    @Test
    public void testGetAllEpic() throws IOException, InterruptedException {
        manager.createEpic(new Epic("Эпик", "Описание Эпика"));
        manager.createEpic(new Epic("Эпик1", "Описание Эпика1"));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/epics");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        List<Epic> taskList = gson.fromJson(response.body(), new UserListTypeToken().getType());
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals(2, taskList.size(), "Некорректное количество задач");
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        manager.createEpic(new Epic("Эпик", "Описание Эпика"));
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/epics/" + manager.getId());

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Epic epic = gson.fromJson(response.body(), Epic.class);
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Эпик", epic.getName(), "Некорректное название таски");
    }

    @Test
    public void testGetSubtasksByEpicId() throws IOException, InterruptedException {
        manager.createEpic(new Epic("Эпик", "Описание Эпика"));
        int idEpic = manager.getId();
        manager.createSubTask(new SubTask("Сабтаска1", "Описание Сабтаска1", idEpic, "08.06.2024 08:15", "30"));
        manager.createSubTask(new SubTask("Сабтаска2", "Описание Сабтаска2", idEpic, "08.06.2024 09:15", "30"));
        manager.createSubTask(new SubTask("Сабтаска3", "Описание Сабтаска3", idEpic, "08.06.2024 10:15", "30"));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/epics/" + idEpic + "/subtasks");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        List<SubTask> taskList = gson.fromJson(response.body(), new UserListTypeToken().getType());
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals(3, taskList.size(), "Некорректное кол-во сабтасок");
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        manager.createTask(new Task("Задача1", "Описание Задачи1", "08.06.2024 13:15", "30"));
        manager.createTask(new Task("Задача2", "Описание Задачи2", "08.06.2024 14:15", "30"));

        manager.getTask(manager.getId());
        manager.getTask(manager.getId() - 5);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/history");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        List<Task> taskList = gson.fromJson(response.body(), new UserListTypeToken().getType());
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Задача2", taskList.getFirst().getName(), "Некорректное добавление в историю");
        Assertions.assertEquals("Задача1", taskList.getLast().getName(), "Некорректное добавление в историю");
    }

    @Test
    public void testGetPriorityTask() throws IOException, InterruptedException {
        manager.createTask(new Task("Задача1", "Описание Задачи1", "08.06.2024 13:15", "30"));
        manager.createTask(new Task("Задача2", "Описание Задачи2", "08.06.2024 14:15", "30"));
        manager.createTask(new Task("Задача3", "Описание Задачи3", "08.06.2024 10:15", "30"));
        manager.createTask(new Task("Задача4", "Описание Задачи4", "08.06.2024 11:15", "30"));


        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/manage/prioritized");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        List<Task> taskList = gson.fromJson(response.body(), new UserListTypeToken().getType());
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Задача3", taskList.getFirst().getName(), "Некорректное добавление в историю");
        Assertions.assertEquals("Задача2", taskList.getLast().getName(), "Некорректное добавление в историю");
    }
}