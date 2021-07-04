import java.io.IOException;

public class NoSuchRoomException extends IOException {
    public NoSuchRoomException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
