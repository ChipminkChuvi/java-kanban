package server;

import com.sun.net.httpserver.HttpServer;
import taskmanager.Managers;
import taskmanager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        TaskManager inMemoryTaskManagerNetwork = Managers.getDefault();
        ManagerHandler managerHandler = new ManagerHandler();

        managerHandler.setTaskManager(inMemoryTaskManagerNetwork);

        httpServer.createContext("/manage", managerHandler);

        httpServer.start();

    }
}
