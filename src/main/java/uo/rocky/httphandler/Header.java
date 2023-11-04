package uo.rocky.httphandler;

import java.util.StringJoiner;

public enum Header {
    ALLOW("Allow"),
    CONTENT_ENCODING("Content-Encoding"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    DATE("Date"),
    EXPIRES("Expires"),
    LAST_MODIFIED("Last-Modified"),
    LOCATION("Location"),
    REFRESH("Refresh"),
    SERVER("Server"),
    SET_COOKIE("Set-Cookie"),
    WWW_AUTHENTICATE("WWW-Authenticate");

    private final String call;

    Header(final String call) {
        this.call = call;
    }

    public final String call() {
        return call;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Header.class.getSimpleName() + "[", "]")
                .add("name='" + name() + "'")
                .add("ordinal=" + ordinal())
                .add("call='" + call() + "'")
                .toString();
    }
}