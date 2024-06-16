import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmodel.SubTask;
import taskmodel.TaskStatus;


class SubTaskTest {
    @Test
    void equalTwoSubTaskTaskById() {
        SubTask subTask = new SubTask(5, "Сабтаска1", "Описание Сабтаски 1", 5, TaskStatus.DONE, "08.06.2024 12:15", "15");
        SubTask subTask1 = new SubTask(5, "Сабтаска1", "Описание Сабтаски 1", 5, TaskStatus.DONE, "08.06.2024 12:15", "15");

        Assertions.assertEquals(subTask, subTask1, "Задачи не совпадают.");
    }
}