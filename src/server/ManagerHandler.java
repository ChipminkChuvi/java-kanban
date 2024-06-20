package server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.InMemoryTaskManager;
import taskmanager.TaskManager;
import taskmodel.Epic;
import taskmodel.SubTask;
import taskmodel.Task;
import taskmodel.TaskStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class ManagerHandler extends InMemoryTaskManager implements HttpHandler {

    private TaskManager inMemoryTaskManagerNetwork;
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    private int responseCode;
    private String responseText;

    public void setTaskManager(TaskManager taskManager) {
        this.inMemoryTaskManagerNetwork = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getRequestMethod();
        String[] uri = exchange.getRequestURI().getPath().split("/");
        responseText = "";
        switch (exchange.getRequestMethod()) {
            case "GET":
                List<Task> listToResponse = handlerGet(exchange);
                if (!listToResponse.isEmpty()) {
                    responseText = responseBody(listToResponse);
                } else {
                    responseText = "null";
                }

                break;
            case "POST":
                handlerPost(exchange, uri);
                break;
            case "DELETE":
                handlerDelete(uri);
                break;
            default:
        }
        exchange.sendResponseHeaders(responseCode, 0);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseText.getBytes(StandardCharsets.UTF_8));
        }
    }


    private void handlerPost(HttpExchange exchange, String[] uri) throws IOException {
        int id = 0;
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        if (uri.length == 4) {
            id = Integer.parseInt(uri[3]);
        }
        switch (uri[2]) {
            case "tasks":
                Task task = gson.fromJson(body, Task.class);

                if (uri.length == 3) {
                    inMemoryTaskManagerNetwork.createTask(task);
                } else {
                    task.setId(id);
                    inMemoryTaskManagerNetwork.updateTask(task);
                }

                if (inMemoryTaskManagerNetwork.getStatusCode()) {
                    responseCode = 201;
                } else {
                    responseCode = 406;
                }
                break;
            case "epics":
                Epic epic = gson.fromJson(body, Epic.class);

                if (uri.length == 3) {
                    inMemoryTaskManagerNetwork.createEpic(gson.fromJson(body, Epic.class));
                    ;
                } else {
                    Epic epicToUpdate = inMemoryTaskManagerNetwork.getEpic(id);

                    epicToUpdate.setName(epic.getName());
                    epicToUpdate.setDescription(epic.getDescription());
                    inMemoryTaskManagerNetwork.updateEpic(epicToUpdate);
                }

                if (inMemoryTaskManagerNetwork.getStatusCode()) {
                    responseCode = 201;
                } else {
                    responseCode = 406;
                }
                break;
            case "subtasks":
                SubTask subTask = gson.fromJson(body, SubTask.class);
                if (uri.length == 3) {
                    inMemoryTaskManagerNetwork.createSubTask(subTask);
                } else {
                    subTask.setId(id);
                    inMemoryTaskManagerNetwork.updateSubTask(subTask);
                }

                if (inMemoryTaskManagerNetwork.getStatusCode()) {
                    responseCode = 201;
                } else {
                    responseCode = 406;
                }
                break;
            default:
                responseCode = 404;
                break;
        }

        System.out.println();
    }


    private List<Task> handlerGet(HttpExchange exchange) {
        String[] uri = exchange.getRequestURI().getPath().split("/");
        List<Task> listTask = new ArrayList<>();
        int id = 0;
        if (uri.length == 4 | uri.length == 5) {
            try {
                id = Integer.parseInt(uri[3]);
            } catch (NumberFormatException e) {
                responseCode = 404;
                return null;
            }
        }

        switch (uri[2]) {
            case "tasks":
                if (uri.length == 3) {
                    listTask = inMemoryTaskManagerNetwork.getAllTasks();
                } else if (uri.length == 4) {
                    listTask.add(inMemoryTaskManagerNetwork.getTask(id));
                }
                if (inMemoryTaskManagerNetwork.getStatusCode()) {
                    responseCode = 200;
                } else {
                    responseCode = 404;
                }
                return listTask;
            case "subtasks":
                if (uri.length == 3) {
                    listTask = inMemoryTaskManagerNetwork.getAllSubTask();
                } else if (uri.length == 4) {
                    listTask.add(inMemoryTaskManagerNetwork.getSubTask(id));
                }
                if (inMemoryTaskManagerNetwork.getStatusCode()) {
                    responseCode = 200;
                } else {
                    responseCode = 404;
                }
                return listTask;
            case "epics":
                if (uri.length == 3) {
                    responseCode = 200;
                    listTask = inMemoryTaskManagerNetwork.getAllEpics();
                } else if (uri.length == 4) {
                    listTask.add(inMemoryTaskManagerNetwork.getEpic(id));
                } else if (uri.length == 5 & uri[4].equals("subtasks")) {
                    listTask = inMemoryTaskManagerNetwork.getSubTaskFromEpic(id);
                }

                if (inMemoryTaskManagerNetwork.getStatusCode()) {
                    responseCode = 200;
                } else {
                    responseCode = 404;
                }
                return listTask;
            case "history":
                responseCode = 200;
                return inMemoryTaskManagerNetwork.getHistoryManager().getHistory();
            case "prioritized":
                responseCode = 200;
                listTask.addAll(inMemoryTaskManagerNetwork.getPriorityTaskView());
                return listTask;
        }
        responseCode = 404;
        return null;
    }

    private void handlerDelete(String[] uri) {
        int id = 0;
        if (uri.length == 4) {
            try {
                id = Integer.parseInt(uri[3]);
            } catch (NumberFormatException e) {
                responseCode = 404;
                return;
            }
        }
        switch (uri[2]) {
            case "tasks":
                if (uri.length == 3) {
                    inMemoryTaskManagerNetwork.removeAllTask();
                } else if (uri.length == 4) {
                    inMemoryTaskManagerNetwork.removeTaskById(id);
                }
                if (inMemoryTaskManagerNetwork.getStatusCode()) {
                    responseCode = 200;
                } else {
                    responseCode = 404;
                }
                break;
            case "epics":
                if (uri.length == 3) {
                    inMemoryTaskManagerNetwork.removeAllEpic();
                } else if (uri.length == 4) {
                    inMemoryTaskManagerNetwork.removeEpicById(id);
                }
                if (inMemoryTaskManagerNetwork.getStatusCode()) {
                    responseCode = 200;
                } else {
                    responseCode = 404;
                }
                break;
            case "subtasks":
                if (uri.length == 3) {
                    inMemoryTaskManagerNetwork.removeAllSubTask();
                } else if (uri.length == 4) {
                    inMemoryTaskManagerNetwork.removeSubTaskById(id);
                }
                if (inMemoryTaskManagerNetwork.getStatusCode()) {
                    responseCode = 200;
                } else {
                    responseCode = 404;
                }
                break;
            default:
                responseCode = 404;
                break;
        }
    }

    private String responseBody(List<Task> list) {
        if (list.getFirst() != null) {
            if (list.size() == 1) {
                return gson.toJson(list.getFirst());
            } else {
                return gson.toJson(list);
            }
        }
        return null;
    }
}
