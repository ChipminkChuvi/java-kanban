package taskmodel;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> linkedSubTask;

    public Epic(String name, String description) {
        super(name, description);
        linkedSubTask = new ArrayList<>();
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
        this.linkedSubTask = new ArrayList<>();
    }

    public Epic(int id, String name, String description, ArrayList<Integer> linkedSubTask) {
        super(id, name, description);
        this.linkedSubTask = linkedSubTask;
    }
    public ArrayList<Integer> getLinkedSubTask() {
        return linkedSubTask;
    }

    public void setLinkedSubTask(ArrayList<Integer> linkedSubTask) {
        this.linkedSubTask = linkedSubTask;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", linkedSubTask=" + linkedSubTask +
                ", taskStatus=" + getTaskStatus() +
                '}';
    }
}
