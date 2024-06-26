package uo.rocky.entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uo.rocky.entity.EntityRelatesToJSON.LOCALDATETIME_FORMATTER_T;
import static uo.rocky.entity.EntityRelatesToSQL.LOCALDATETIME_FORMATTER_SPACE;

/**
 * Manages the connection between the entities and the database.
 * <p>
 * All fields and methods are static.
 *
 * @author Rocky Haotian Du
 */
public final class EntitySQLConnection {

    private static final String QUERY_ALL = "ALL";

    private static Connection connection = null;

    public static synchronized void close() throws SQLException {
        connection.close();
    }

    public static synchronized Connection getConnection() {
        return connection;
    }

    public static synchronized void setConnection(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        EntitySQLConnection.connection = connection;
        createTablesIfNotExists();
    }

    private static synchronized void createTablesIfNotExists() throws SQLException {
        String sql;
        Statement statement = connection.createStatement();

        sql = "CREATE TABLE IF NOT EXISTS user (\n" +
                "    USR_NAME            TEXT     NOT NULL  PRIMARY KEY  UNIQUE,\n" +
                "    USR_HASHEDPASSWORD  INTEGER  NOT NULL,\n" +
                "    USR_EMAIL           TEXT,\n" +
                "    USR_PHONE           TEXT\n" +
                ");\n";
        statement.addBatch(sql);

        sql = "CREATE TABLE IF NOT EXISTS coordinate (\n" +
                "    CDT_ID              INTEGER  NOT NULL  PRIMARY KEY  UNIQUE,\n" +
                "    CDT_LONGITUDE       REAL     NOT NULL,\n" +
                "    CDT_LATITUDE        REAL     NOT NULL,\n" +
                "    CDT_LOCALDATETIME   NUMERIC  NOT NULL,\n" +
                "    CDT_DATETIMEOFFSET  TEXT     NOT NULL,\n" +
                "    CDT_DANGERTYPE      TEXT     NOT NULL,\n" +
                "    CDT_DESCRIPTION     TEXT,\n" +
                "    CDT_USR_NAME        TEXT     NOT NULL,\n" +
                "    FOREIGN KEY (CDT_USR_NAME) REFERENCES user(USR_NAME)\n" +
                ");\n";
        statement.addBatch(sql);

        sql = "CREATE TABLE IF NOT EXISTS comment (\n" +
                "    CMT_ID              INTEGER  NOT NULL  PRIMARY KEY  UNIQUE,\n" +
                "    CMT_CONTENT         TEXT     NOT NULL,\n" +
                "    CMT_LOCALDATETIME   NUMERIC  NOT NULL,\n" +
                "    CMT_DATETIMEOFFSET  TEXT     NOT NULL,\n" +
                "    CMT_CDT_ID          INTEGER  NOT NULL,\n" +
                "    FOREIGN KEY (CMT_CDT_ID) REFERENCES coordinate(CDT_ID)\n" +
                ");\n";
        statement.addBatch(sql);

        statement.executeBatch();
        statement.close();
        connection.commit();
    }

    static synchronized List<User> selectUsers(Map<String, String> params) throws SQLException, QueryParamException {
        String sql;
        switch (params.getOrDefault("QUERY", QUERY_ALL).toUpperCase()) {
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
            case QUERY_ALL:
                sql = "SELECT * FROM user;";
                break;
            default:
                throw new QueryParamException("Invalid query parameter \"" + params.get("QUERY") + "\" (only \"USERNAME\", \"HASHEDPASSWORD\", \"EMAIL\", \"PHONE\", or \"" + QUERY_ALL + "\" cAsE-InSeNsItIvElY supported as a valid query parameter)");
        }

        List<User> results = new ArrayList<>();
//        System.out.println(sql);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            results.add(User.valueOf(resultSet));
        }
        resultSet.close();
        statement.close();
        connection.commit();
        return results;
    }

    static synchronized List<Coordinate> selectCoordinates(Map<String, String> params) throws SQLException, QueryParamException {
        String sql;
        switch (params.getOrDefault("QUERY", QUERY_ALL).toUpperCase()) {
            case "ID":
                sql = "SELECT * FROM coordinate WHERE CDT_ID = " + params.get("ID") + ";";
                break;
            case "LOCATION":
                sql = "SELECT * FROM coordinate WHERE CDT_LONGITUDE BETWEEN " + params.getOrDefault("DOWNLONGITUDE", String.valueOf(-180D)) + " AND " + params.getOrDefault("UPLONGITUDE", String.valueOf(+180D)) + " AND CDT_LATITUDE BETWEEN " + params.getOrDefault("DOWNLATITUDE", String.valueOf(-90D)) + " AND " + params.getOrDefault("UPLATITUDE", String.valueOf(+90D)) + ";";
                break;
            case "SENT":
                sql = "SELECT * FROM coordinate WHERE CDT_LOCALDATETIME BETWEEN " + EntityRelatesToSQL.escapeSingleQuotes(LocalDateTime.parse(params.getOrDefault("DOWNSENT", "0001-01-01T00:00:00.000Z").substring(0, 23), LOCALDATETIME_FORMATTER_T).format(LOCALDATETIME_FORMATTER_SPACE)) + " AND " + EntityRelatesToSQL.escapeSingleQuotes(LocalDateTime.parse(params.getOrDefault("UPSENT", "9999-12-31T23:59:59.999Z").substring(0, 23), LOCALDATETIME_FORMATTER_T).format(LOCALDATETIME_FORMATTER_SPACE)) + ";";
                break;
            case "USER":
                sql = "SELECT * FROM coordinate WHERE CDT_USR_NAME = " + EntityRelatesToSQL.escapeSingleQuotes(params.get("USERNAME")) + ";";
                break;
            case QUERY_ALL:
                sql = "SELECT * FROM coordinate;";
                break;
            default:
                throw new QueryParamException("Invalid query parameter \"" + params.get("QUERY") + "\" (only \"ID\", \"LOCATION\", \"SENT\", \"USER\", or \"" + QUERY_ALL + "\" cAsE-InSeNsItIvElY supported as a valid query parameter)");
        }

        List<Coordinate> results = new ArrayList<>();
//        System.out.println(sql);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            results.add(Coordinate.valueOf(resultSet));
        }
        resultSet.close();
        statement.close();
        connection.commit();
        return results;
    }

    static synchronized List<Comment> selectComments(Map<String, String> params) throws SQLException, QueryParamException {
        String sql;
        switch (params.getOrDefault("QUERY", QUERY_ALL).toUpperCase()) {
            case "COMMENTID":
                sql = "SELECT * FROM comment WHERE CMT_ID = " + params.get("COMMENTID") + ";";
                break;
            case "SENT":
                sql = "SELECT * FROM comment WHERE CMT_LOCALDATETIME BETWEEN " + EntityRelatesToSQL.escapeSingleQuotes(LocalDateTime.parse(params.getOrDefault("DOWNSENT", "0001-01-01T00:00:00.000Z").substring(0, 23), LOCALDATETIME_FORMATTER_T).format(LOCALDATETIME_FORMATTER_SPACE)) + " AND " + EntityRelatesToSQL.escapeSingleQuotes(LocalDateTime.parse(params.getOrDefault("UPSENT", "9999-12-31T23:59:59.999Z").substring(0, 23), LOCALDATETIME_FORMATTER_T).format(LOCALDATETIME_FORMATTER_SPACE)) + ";";
                break;
            case "ID":
                sql = "SELECT * FROM comment WHERE CMT_CDT_ID = " + params.get("ID") + ";";
                break;
            case QUERY_ALL:
                sql = "SELECT * FROM comment;";
                break;
            default:
                throw new QueryParamException("Invalid query parameter \"" + params.get("QUERY") + "\" (only \"COMMENTID\", \"SENT\", \"ID\", or \"" + QUERY_ALL + "\" cAsE-InSeNsItIvElY supported as a valid query parameter)");
        }

        List<Comment> results = new ArrayList<>();
//        System.out.println(sql);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            results.add(Comment.valueOf(resultSet));
        }
        resultSet.close();
        statement.close();
        connection.commit();
        return results;
    }
}