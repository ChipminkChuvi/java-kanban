import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanager.InMemoryTaskManager;
import taskmodel.Epic;
import taskmodel.SubTask;
import taskmodel.TaskStatus;

class EpicTest {


    @Test
    void equalTwoEpicById() {
        Epic epic = new Epic(5, "Эпик", "Описание эпика");
        Epic epic1 = new Epic(5, "Эпик", "Описание эпика");

        Assertions.assertEquals(epic, epic1, "Задачи не совпадают.");
    }
}