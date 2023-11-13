package uo.rocky.entity;

import java.sql.Connection;
import java.time.format.DateTimeFormatter;

public abstract class EntityBase implements EntityRelatesToJSON, EntityRelatesToSQL {
    public static final DateTimeFormatter LOCALDATETIME_FORMATTER_T = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    public static final DateTimeFormatter LOCALDATETIME_FORMATTER_SPACE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static Connection connection = null;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        EntityBase.connection = connection;
    }
}