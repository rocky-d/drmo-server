package uo.rocky.entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Manages the connection between the entities and the database.
 * <p>
 * All fields and methods are static.
 *
 * @author Rocky Haotian Du
 */
public final class EntityDBConnection {

    private static final String NO_QUERT_KEY = "ALL";

    private static Connection connection = null;

    public static synchronized void close() throws SQLException {
        connection.close();
    }

    public static synchronized Connection getConnection() {
        return connection;
    }

    public static synchronized void setConnection(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        EntityDBConnection.connection = connection;
        createTablesIfNotExists();
    }

    private static synchronized void createTablesIfNotExists() throws SQLException {
        String sql;
        Statement statement = connection.createStatement();

        sql = "CREATE TABLE IF NOT EXISTS user (\n" +
                "    USR_NAME TEXT PRIMARY KEY NOT NULL UNIQUE,\n" +
                "    USR_HASHEDPASSWORD INTEGER NOT NULL,\n" +
                "    USR_EMAIL TEXT,\n" +
                "    USR_PHONE TEXT\n" +
                ");\n";
        statement.addBatch(sql);

        sql = "CREATE TABLE IF NOT EXISTS coordinate (\n" +
                "    CDT_ID INTEGER PRIMARY KEY NOT NULL UNIQUE,\n" +
                "    CDT_LONGITUDE REAL NOT NULL,\n" +
                "    CDT_LATITUDE REAL NOT NULL,\n" +
                "    CDT_LOCALDATETIME NUMERIC NOT NULL,\n" +
                "    CDT_DATETIMEOFFSET TEXT NOT NULL,\n" +
                "    CDT_DANGERTYPE TEXT NOT NULL,\n" +
                "    CDT_DESCRIPTION TEXT,\n" +
                "    CDT_USR_NAME TEXT NOT NULL,\n" +
                "    FOREIGN KEY (CDT_USR_NAME) REFERENCES user(USR_NAME)\n" +
                ");\n";
        statement.addBatch(sql);

        sql = "CREATE TABLE IF NOT EXISTS comment (\n" +
                "    CMT_ID INTEGER PRIMARY KEY NOT NULL UNIQUE,\n" +
                "    CMT_CONTENT TEXT NOT NULL,\n" +
                "    CMT_LOCALDATETIME NUMERIC NOT NULL,\n" +
                "    CMT_DATETIMEOFFSET TEXT NOT NULL,\n" +
                "    CMT_CDT_ID INTEGER NOT NULL,\n" +
                "    FOREIGN KEY (CMT_CDT_ID) REFERENCES coordinate(CDT_ID)\n" +
                ");\n";
        statement.addBatch(sql);

        statement.executeBatch();
        statement.close();
        connection.commit();
    }

    static synchronized List<User> selectUsers(Map<String, String> params) throws SQLException {
        String sql;
        switch (params.getOrDefault("QUERY", NO_QUERT_KEY).toUpperCase()) {
            case "USERNAME":
                sql = "SELECT * FROM user WHERE USR_NAME = " + EntityRelatesToSQL.escapeSingleQuotes(params.get("USERNAME")) + ";";
                break;
            case "HASHEDPASSWORD":
                sql = "SELECT * FROM user WHERE USR_HASHEDPASSWORD = " + params.get("HASHEDPASSWORD") + ";";
                break;
            case "EMAIL":
                sql = "SELECT * FROM user WHERE USR_EMAIL = " + EntityRelatesToSQL.escapeSingleQuotes(params.get("EMAIL")) + ";";
                break;
            case "PHONE":
                sql = "SELECT * FROM user WHERE USR_PHONE = " + EntityRelatesToSQL.escapeSingleQuotes(params.get("PHONE")) + ";";
                break;
            case NO_QUERT_KEY:
                sql = "SELECT * FROM user;";
                break;
            default:
                sql = null;
                break;
        }

        List<User> results;
        if (null == sql) {
            results = null;
        } else {
            results = new ArrayList<>();
//            System.out.println(sql);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                results.add(User.valueOf(resultSet));
            }
            resultSet.close();
            statement.close();
            connection.commit();
        }
        return results;
    }

    static synchronized List<Coordinate> selectCoordinates(Map<String, String> params) throws SQLException {
        String sql;
        switch (params.getOrDefault("QUERY", NO_QUERT_KEY).toUpperCase()) {
            case "ID":
                sql = "SELECT * FROM coordinate WHERE CDT_ID = " + params.get("ID") + ";";
                break;
            case "LOCATION":
                sql = "SELECT * FROM coordinate WHERE " + params.get("DOWNLONGITUDE") + " <= CDT_LONGITUDE AND CDT_LONGITUDE <= " + params.get("UPLONGITUDE") + " AND " + params.get("DOWNLATITUDE") + " <= CDT_LATITUDE AND CDT_LATITUDE <= " + params.get("UPLATITUDE") + ";";
                break;
            case "SENT":
                sql = "SELECT * FROM coordinate WHERE " + params.getOrDefault("DOWNDATETIME", "0000-00-00 00:00:00.000") + ";";
                break;
            case "USER":
                sql = "SELECT * FROM coordinate WHERE CDT_USR_NAME = " + EntityRelatesToSQL.escapeSingleQuotes(params.get("USERNAME")) + ";";
                break;
            case NO_QUERT_KEY:
                sql = "SELECT * FROM coordinate;";
                break;
            default:
                sql = null;
                break;
        }

        List<Coordinate> results;
        if (null == sql) {
            results = null;
        } else {
            results = new ArrayList<>();
//            System.out.println(sql);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                results.add(Coordinate.valueOf(resultSet));
            }
            resultSet.close();
            statement.close();
            connection.commit();
        }
        return results;
    }

    static synchronized List<Comment> selectComments(Map<String, String> params) throws SQLException {
        String sql;
        switch (params.getOrDefault("QUERY", NO_QUERT_KEY).toUpperCase()) {
            case "COMMENTID":
                sql = "SELECT * FROM comment WHERE CMT_ID = " + params.get("COMMENTID") + ";";
                break;
            case "SENT":
                sql = ";";
                break;
            case "ID":
                sql = "SELECT * FROM comment WHERE CMT_CDT_ID = " + params.get("ID") + ";";
                break;
            case NO_QUERT_KEY:
                sql = "SELECT * FROM comment;";
                break;
            default:
                sql = null;
                break;
        }

        List<Comment> results;
        if (null == sql) {
            results = null;
        } else {
            results = new ArrayList<>();
//            System.out.println(sql);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                results.add(Comment.valueOf(resultSet));
            }
            resultSet.close();
            statement.close();
            connection.commit();
        }
        return results;
    }
}