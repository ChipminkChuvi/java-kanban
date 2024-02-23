import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanager.InMemoryTaskManager;
import taskmanager.TaskManager;
import taskmodel.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static taskmodel.TaskStatus.NEW;

class TaskTest {

    @Test
    void equalTwoTaskById() {

        Task task = new Task(5,"Задача", "Описание Задачи");
        Task task1 = new Task(5,"Задача", "Описание Задачи");

        Assertions.assertEquals(task,task1,"Задачи не совпадают.");
    }

}