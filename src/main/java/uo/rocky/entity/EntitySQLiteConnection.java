package uo.rocky.entity;

import java.sql.Connection;

public final class EntitySQLiteConnection {
    private static Connection connection = null;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        EntitySQLiteConnection.connection = connection;
    }

    public static void setConnectionForAllEntities(Connection connection) {
        Comment.setConnection(connection);
        Coordinate.setConnection(connection);
        User.setConnection(connection);
    }
}