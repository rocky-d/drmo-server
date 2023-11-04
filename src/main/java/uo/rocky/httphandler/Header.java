package uo.rocky.httphandler;

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

    private final String STANDARD_NAME;

    Header(final String STANDARD_NAME) {
        this.STANDARD_NAME = STANDARD_NAME;
    }

    @Override
    public String toString() {
        return STANDARD_NAME;
    }
}