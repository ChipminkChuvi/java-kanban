package taskmodel;

public class SubTask extends Task {
    private int idEpic;

    public SubTask(String name, String description, int idEpic) {
        super(name, description);
        this.idEpic = idEpic;
    }
    public SubTask(int id, String name, String description, int idEpic) {
        super(id, name, description);
        this.idEpic = idEpic;
    }
    public SubTask(int id, String name, String description, int idEpic, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
        this.idEpic = idEpic;
    }
    public SubTask(String name, String description, int idEpic, TaskStatus taskStatus) {
        super(name, description, taskStatus);
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
        return  getId() +
                ",SUBTASK," +
                getName() + "," +
                getTaskStatus() + "," +
                getDescription() + "," +
                idEpic;
    }
}
