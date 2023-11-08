package uo.rocky.httphandler;

import java.util.StringJoiner;

public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    DELETE,
    CONNECT,
    OPTIONS,
    TRACE,
    PATCH;

    @Override
    public final String toString() {
        return new StringJoiner(", ", RequestMethod.class.getSimpleName() + "{", "}")
                .add("name='" + name() + "'")
                .add("ordinal=" + ordinal())
                .toString();
    }
}