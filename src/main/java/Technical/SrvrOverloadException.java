package Technical;


public class SrvrOverloadException extends RuntimeException {
    public SrvrOverloadException(String errorMessage) {
        super(errorMessage);
    }
}