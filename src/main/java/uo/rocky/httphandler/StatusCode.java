package uo.rocky.httphandler;

import java.util.StringJoiner;

public enum StatusCode {
    OK(200),
    METHOD_NOT_ALLOWED(405),
    INTERNAL_SERVER_ERROR(500);

    private final int call;

    StatusCode(int call) {
        this.call = call;
    }

    public final int call() {
        return call;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StatusCode.class.getSimpleName() + "[", "]")
                .add("name='" + name() + "'")
                .add("ordinal=" + ordinal())
                .add("call=" + call())
                .toString();
    }
}