package win.minaandyyh.sparrow;

/**
 * Enum for HTTP content media type
 *
 * @author masteryyh
 */
public enum MediaType {
    APPLICATION_JSON("application/json"),

    APPLICATION_JSON_UTF8("application/json; charset=utf-8"),

    APPLICATION_XML("application/xml"),

    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),

    MULTIPART_FORM_DATA("multipart/form-data"),

    TEXT_PLAIN("text/plain"),

    WILDCARD("*/*"),

    NONE("");

    private final String value;

    MediaType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
