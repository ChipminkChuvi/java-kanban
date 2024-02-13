import java.util.ArrayList;

public class Epic extends Task {

    public ArrayList<Integer> linkedSubTask;

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


    public Epic() {
    }


    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", linkedSubTask=" + linkedSubTask +
                ", taskStatus=" + getTaskStatus() +
                '}';
    }
}
