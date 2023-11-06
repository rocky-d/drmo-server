package uo.rocky.httphandler;

import java.util.StringJoiner;

public enum StatusCode {
    OK(200),
    METHOD_NOT_ALLOWED(405),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public final int code() {
        return code;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StatusCode.class.getSimpleName() + "[", "]")
                .add("name='" + name() + "'")
                .add("ordinal=" + ordinal())
                .add("code=" + code())
                .toString();
    }
}