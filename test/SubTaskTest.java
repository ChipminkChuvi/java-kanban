import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmodel.SubTask;
import taskmodel.Task;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    @Test
    void equalTwoSubTaskTaskById() {
        SubTask subTask = new SubTask(5,"Сабтаска","Описание сабтаски",5);
        SubTask subTask1 = new SubTask(5,"Сабтаска","Описание сабтаски",5);

        Assertions.assertEquals(subTask, subTask1, "Задачи не совпадают.");
    }
}