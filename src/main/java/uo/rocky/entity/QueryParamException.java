package uo.rocky.entity;

/**
 * TODO
 *
 * @author Rocky Haotian Du
 */
public final class QueryParamException extends RuntimeException {

    /**
     * Constructs a new {@link QueryParamException} object with {@code null} as
     * its detail message. The cause is not initialized, and may subsequently
     * be initialized by a call to {@link #initCause}.
     */
    public QueryParamException() {
        super();
    }

    /**
     * Constructs a new {@link QueryParamException} object with the specified
     * detail message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later
     *                retrieval by {@link #getMessage}.
     */
    public QueryParamException(String message) {
        super(message);
    }
}