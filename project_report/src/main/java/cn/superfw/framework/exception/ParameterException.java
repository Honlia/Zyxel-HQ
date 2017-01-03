package cn.superfw.framework.exception;

public class ParameterException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6747885787363681622L;

    public ParameterException() {
        super();
    }

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterException(Throwable cause) {
        super(cause);
    }

    protected ParameterException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
