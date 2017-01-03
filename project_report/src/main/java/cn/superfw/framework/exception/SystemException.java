package cn.superfw.framework.exception;

public class SystemException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8955566206274066924L;

    private String errorCode = null;

    public SystemException() {
        super();
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    protected SystemException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SystemException(Throwable cause, String errorCode) {
		super(cause);
		this.errorCode = errorCode;
	}

    public String getErrorCode() {
        return this.errorCode;
    }

}
