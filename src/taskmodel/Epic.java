package taskmodel;

import com.sun.jdi.LocalVariable;

import javax.print.attribute.standard.MediaSize;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> linkedSubTask;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        linkedSubTask = new ArrayList<>();
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
        this.linkedSubTask = new ArrayList<>();
    }

    public Epic(String name, String description, TaskStatus taskStatus, String startTime, String duration) {
        super(name, description, taskStatus, startTime, duration);
        this.linkedSubTask = new ArrayList<>();
    }

    public Epic(int id, String name, String description, LocalDateTime startTime, Duration duration) {
        super(id, name, description, startTime, duration);
        this.linkedSubTask = new ArrayList<>();
    }


    public List<Integer> getLinkedSubTask() {
        return linkedSubTask;
    }

    private void setLinkedSubTask(ArrayList<Integer> linkedSubTask) {
        this.linkedSubTask = linkedSubTask;
    }


    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration);
    }


    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return getId() +
                ",EPIC,"
                + getName() + "," +
                getTaskStatus() + "," +
                getDescription() + "," +
                getStartTime().format(formatter) + "," +
                getDuration().toMinutes() + "," +
                getLinkedSubTask();
    }
}
