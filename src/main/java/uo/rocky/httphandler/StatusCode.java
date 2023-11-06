package uo.rocky.httphandler;

import java.util.StringJoiner;

public enum StatusCode {
    CONTINUE(100, "Continue"),
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NO_CONTENT(204, "No Content"),
    MULTIPLE_CHOICES(300, "Multiple Choices"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    REQUEST_TIMEOUT(408, "Request Timeout"),
    CONFLICT(409, "Conflict"),
    GONE(410, "Gone"),
    LENGTH_REQUIRED(411, "Length Required"),
//    CONTENT_TOO_LARGE(413, "Content Too Large"),
//    URI_TOO_LONG(414, "URI Too Long"),
//    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");

    private final int code;
    private final String prompt;

    StatusCode(final int code, final String prompt) {
        this.code = code;
        this.prompt = prompt;
    }

    public final int code() {
        return code;
    }

    public final String prompt() {
        return prompt;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StatusCode.class.getSimpleName() + "{", "}")
                .add("name='" + name() + "'")
                .add("ordinal=" + ordinal())
                .add("code=" + code())
                .add("prompt='" + prompt() + "'")
                .toString();
    }
}