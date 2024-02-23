import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmodel.Epic;
import taskmodel.Task;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void equalTwoEpicById() {
        Epic epic = new Epic(5, "Эпик", "Описание эпика");
        Epic epic1 = new Epic(5, "Эпик", "Описание эпика");

        Assertions.assertEquals(epic, epic1, "Задачи не совпадают.");
    }
}