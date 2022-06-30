package Technical;

public class SrvrDownException extends RuntimeException {
    public SrvrDownException(String errorMessage) {
        super(errorMessage);
    }
}