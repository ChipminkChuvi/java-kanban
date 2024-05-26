package taskmodel;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> linkedSubTask;

    public Epic(String name, String description) {
        super(name, description);
        linkedSubTask = new ArrayList<>();
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
        this.linkedSubTask = new ArrayList<>();
    }

    public Epic(String name, String description, TaskStatus taskStatus) {
        super(name, description,taskStatus);
    }

    public Epic(int id, String name, String description, ArrayList<Integer> linkedSubTask) {
        super(id, name, description);
        this.linkedSubTask = linkedSubTask;
    }


    public List<Integer> getLinkedSubTask() {
        return linkedSubTask;
    }

    private void setLinkedSubTask(ArrayList<Integer> linkedSubTask) {
        this.linkedSubTask = linkedSubTask;
    }

    @Override
    public String toString() {
        return  getId() +
                ",EPIC,"
                + getName() + "," +
                getTaskStatus() + "," +
                getDescription();
    }
}
