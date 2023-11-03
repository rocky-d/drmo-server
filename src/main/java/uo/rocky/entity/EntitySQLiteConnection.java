package uo.rocky.entity;

import java.sql.Connection;

public final class EntitySQLiteConnection {
    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        EntitySQLiteConnection.connection = connection;
    }
}