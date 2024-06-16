package taskmodel;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int idEpic;

    public SubTask(String name, String description, int idEpic, String startTime, String duration) {
        super(name, description, startTime, duration);
        this.idEpic = idEpic;
    }

    public SubTask(String name, String description, int idEpic) {
        super(name, description);
        this.idEpic = idEpic;
        setStartTime(LocalDateTime.now());
        setDuration(Duration.ofMinutes(15));
    }

    public SubTask(int id, String name, String description, int idEpic, LocalDateTime startTime, Duration duration) {
        super(id, name, description, startTime, duration);
        this.idEpic = idEpic;
    }


    public SubTask(int id, String name, String description, int idEpic, TaskStatus taskStatus, String startTime, String duration) {
        super(id, name, description, taskStatus, startTime, duration);
        this.idEpic = idEpic;
    }

    public SubTask(String name, String description, int idEpic, TaskStatus taskStatus, String startTime, String duration) {
        super(name, description, taskStatus, startTime, duration);
        this.idEpic = idEpic;
    }

    public SubTask(int id, String name, String description, int idEpic, TaskStatus taskStatus, LocalDateTime startTime, Duration duration) {
        super(id, name, description, taskStatus, startTime, duration);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    private void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }


    @Override
    public String toString() {
        return getId() +
                ",SUBTASK," +
                getName() + "," +
                getTaskStatus() + "," +
                getDescription() + "," +
                idEpic + "," +
                getStartTime().format(formatter) + "," +
                getDuration().toMinutes();
    }
}
