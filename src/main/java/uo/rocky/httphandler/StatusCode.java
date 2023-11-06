package uo.rocky.httphandler;

import java.util.StringJoiner;

public enum StatusCode {
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    NOT_ACCEPTABLE(406),
    REQUEST_TIMEOUT(408),
    CONFLICT(409),
    GONE(410),
    LENGTH_REQUIRED(411),
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501),
    SERVICE_UNAVAILABLE(503),
    HTTP_VERSION_NOT_SUPPORTED(505);

    private final int call;

    StatusCode(int call) {
        this.call = call;
    }

    public final int call() {
        return call;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StatusCode.class.getSimpleName() + "{", "}")
                .add("name='" + name() + "'")
                .add("ordinal=" + ordinal())
                .add("call=" + call())
                .toString();
    }
}