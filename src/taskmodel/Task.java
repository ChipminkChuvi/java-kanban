package taskmodel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private TaskStatus taskStatus = TaskStatus.NEW;

    private Duration duration = Duration.ofMinutes(15);
    private LocalDateTime startTime = LocalDateTime.now();

    public Task() {
    }

    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String description, String startTime, String duration) {
        this.name = name;
        this.description = description;
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.ofMinutes(Integer.parseInt(duration));
    }

    public Task(int id, String name, String description, TaskStatus taskStatus, String startTime, String duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.ofMinutes(Integer.parseInt(duration));
    }

    public Task(int id, String name, String description, TaskStatus taskStatus, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(15);
    }

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(15);
    }

    public Task(int id, String name, String description, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, TaskStatus taskStatus, String startTime, String duration) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.ofMinutes(Integer.parseInt(duration));
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Duration getDuration() {
        if (duration == null) {
            return Duration.ofMinutes(0);
        }
        return duration;
    }

    public LocalDateTime getStartTime() {
        if (startTime == null) {
            return LocalDateTime.now();
        }
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (duration == null) {
            return startTime.plus(Duration.ofMinutes(0));
        } else {
            return startTime.plus(duration);
        }
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return
                id +
                        ",TASK," +
                        name + "," +
                        taskStatus + "," +
                        description + "," +
                        startTime.format(formatter) + "," +
                        duration.toMinutes();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && taskStatus == task.taskStatus && Objects.equals(duration, task.duration) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, taskStatus, duration, startTime);
    }
}

