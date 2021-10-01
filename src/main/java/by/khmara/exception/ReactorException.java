package by.khmara.exception;

public class ReactorException extends Throwable {
    private Throwable throwable;
    private String message;

    public ReactorException(String message, Throwable throwable) {
        this.throwable = throwable;
        this.message = message;
    }
}
