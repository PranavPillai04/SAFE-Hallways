import java.io.IOException;

public class NoMorePeriodsException extends IOException {
    public NoMorePeriodsException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
