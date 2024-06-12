package exceptions;

public class ManagerSaveException extends RuntimeException {

    private final String nameFile;


    public ManagerSaveException(final String message, String nameFile) {
        super(message);
        this.nameFile = nameFile;
    }

    public String getDetailMessage() {
        return getMessage() + nameFile;
    }
}
