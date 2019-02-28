package parsonsolver;

public class UnformedPuzzleException extends Exception {
    public final String message;
    public UnformedPuzzleException(String message) {
        this.message = message;
    }
}
