package by.khmara.exception;

public class MovieException extends Throwable {
    private Throwable throwable;
    private String message;

    public MovieException(Throwable throwable, String message) {
        this.throwable = throwable;
        this.message = message;
    }
}
