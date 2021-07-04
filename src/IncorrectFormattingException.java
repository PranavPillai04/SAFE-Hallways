import java.io.IOException;

public class IncorrectFormattingException extends IOException {
    public IncorrectFormattingException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
