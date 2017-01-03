package cn.superfw.framework.exception;

public class BrowseException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8955566206274066924L;


    public BrowseException() {
        super();
    }

    public BrowseException(String message) {
        super(message);
    }

    public BrowseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrowseException(Throwable cause) {
        super(cause);
    }

    protected BrowseException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
