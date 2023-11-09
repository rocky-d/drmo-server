package uo.rocky.entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class EntityDBConnection {
    private static final String NO_QUERT_KEY = "ALL";
    private static Connection connection = null;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) throws SQLException {
        EntityDBConnection.connection = connection;

        setConnectionForAllEntities();

        EntityDBConnection.connection.setAutoCommit(false);
        createTablesIfNotExists();
    }

    private static void setConnectionForAllEntities() {
        Comment.setConnection(connection);
        Coordinate.setConnection(connection);
        User.setConnection(connection);
    }

    private static void createTablesIfNotExists() throws SQLException {
        Statement statement = connection.createStatement();
        String createTableSQL;

        createTableSQL = "CREATE TABLE IF NOT EXISTS user (\n" +
                "    USR_NAME TEXT PRIMARY KEY NOT NULL UNIQUE,\n" +
                "    USR_HASHEDPASSWORD INTEGER NOT NULL,\n" +
                "    USR_EMAIL TEXT,\n" +
                "    USR_PHONE TEXT\n" +
                ");\n";
        statement.addBatch(createTableSQL);

        createTableSQL = "CREATE TABLE IF NOT EXISTS coordinate (\n" +
                "    CDT_ID INTEGER PRIMARY KEY NOT NULL UNIQUE,\n" +
                "    CDT_LONGITUDE REAL NOT NULL,\n" +
                "    CDT_LATITUDE REAL NOT NULL,\n" +
                "    CDT_DATETIME NUMERIC NOT NULL,\n" +
                "    CDT_DANGERTYPE TEXT NOT NULL,\n" +
                "    CDT_DESCRIPTION TEXT,\n" +
                "    CDT_USR_NAME TEXT NOT NULL,\n" +
                "    FOREIGN KEY (CDT_USR_NAME) REFERENCES user(USR_NAME)\n" +
                ");\n";
        statement.addBatch(createTableSQL);

        createTableSQL = "CREATE TABLE IF NOT EXISTS comment (\n" +
                "    CMT_ID INTEGER PRIMARY KEY NOT NULL UNIQUE,\n" +
                "    CMT_CONTENT TEXT NOT NULL,\n" +
                "    CMT_DATETIME NUMERIC NOT NULL,\n" +
                "    CMT_CDT_ID INTEGER NOT NULL,\n" +
                "    FOREIGN KEY (CMT_CDT_ID) REFERENCES coordinate(CDT_ID)\n" +
                ");\n";
        statement.addBatch(createTableSQL);

        statement.executeBatch();
        statement.close();
        connection.commit();
    }

    static synchronized List<Comment> selectComments(Map<String, String> params) throws SQLException {
        String sql;
        Statement statement;
        ResultSet resultSet;
        List<Comment> results;
        switch (params.getOrDefault("QUERY", NO_QUERT_KEY).toUpperCase()) {
            case "COMMENTID":
                sql = "SELECT * FROM comment WHERE CMT_ID = " + params.get("COMMENTID") + ";";
                System.out.println(sql);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(new Comment(
                            resultSet.getLong("CMT_ID"),
                            resultSet.getString("CMT_CONTENT"),
                            resultSet.getString("CMT_DATETIME"),
                            resultSet.getLong("CMT_CDT_ID")
                    ));
                }
                resultSet.close();
                statement.close();
                connection.commit();
                break;
            case "TIME":
                sql = ";";
                System.out.println(sql);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(new Comment(
                            resultSet.getLong("CMT_ID"),
                            resultSet.getString("CMT_CONTENT"),
                            resultSet.getString("CMT_DATETIME"),
                            resultSet.getLong("CMT_CDT_ID")
                    ));
                }
                resultSet.close();
                statement.close();
                connection.commit();
                break;
            case "ID":
                sql = "SELECT * FROM comment WHERE CMT_CDT_ID = " + params.get("ID") + ";";
                System.out.println(sql);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(new Comment(
                            resultSet.getLong("CMT_ID"),
                            resultSet.getString("CMT_CONTENT"),
                            resultSet.getString("CMT_DATETIME"),
                            resultSet.getLong("CMT_CDT_ID")
                    ));
                }
                resultSet.close();
                statement.close();
                connection.commit();
                break;
            case NO_QUERT_KEY:
                sql = "SELECT * FROM comment;";
                System.out.println(sql);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(new Comment(
                            resultSet.getLong("CMT_ID"),
                            resultSet.getString("CMT_CONTENT"),
                            resultSet.getString("CMT_DATETIME"),
                            resultSet.getLong("CMT_CDT_ID")
                    ));
                }
                resultSet.close();
                statement.close();
                connection.commit();
                break;
            default:
                results = null;
                break;
        }
        return results;
    }

    static synchronized List<Coordinate> selectCoordinates(Map<String, String> params) throws SQLException {
        String sql;
        Statement statement;
        ResultSet resultSet;
        List<Coordinate> results;
        switch (params.getOrDefault("QUERY", NO_QUERT_KEY).toUpperCase()) {
            case "ID":
                sql = "SELECT * FROM coordinate WHERE CDT_ID = " + params.get("ID") + ";";
                System.out.println(sql);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(new Coordinate(
                            resultSet.getLong("CDT_ID"),
                            resultSet.getDouble("CDT_LONGITUDE"),
                            resultSet.getDouble("CDT_LATITUDE"),
                            resultSet.getString("CDT_DATETIME"),
                            null == resultSet.getString("CDT_DANGERTYPE") ?
                                    null : Coordinate.Dangertype.valueOf(resultSet.getString("CDT_DANGERTYPE")),
                            resultSet.getString("CDT_DESCRIPTION"),
                            resultSet.getString("CDT_USR_NAME")
                    ));
                }
                resultSet.close();
                statement.close();
                connection.commit();
                break;
            case "LOCATION":
                sql = "SELECT * FROM coordinate WHERE " + params.get("DOWNLONGITUDE") + " <= CDT_LONGITUDE AND CDT_LONGITUDE <= " + params.get("UPLONGITUDE") +
                        " AND " + params.get("DOWNLATITUDE") + " <= CDT_LATITUDE AND CDT_LATITUDE <= " + params.get("UPLATITUDE") + ";";
                System.out.println(sql);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(new Coordinate(
                            resultSet.getLong("CDT_ID"),
                            resultSet.getDouble("CDT_LONGITUDE"),
                            resultSet.getDouble("CDT_LATITUDE"),
                            resultSet.getString("CDT_DATETIME"),
                            null == resultSet.getString("CDT_DANGERTYPE") ?
                                    null : Coordinate.Dangertype.valueOf(resultSet.getString("CDT_DANGERTYPE")),
                            resultSet.getString("CDT_DESCRIPTION"),
                            resultSet.getString("CDT_USR_NAME")
                    ));
                }
                resultSet.close();
                statement.close();
                connection.commit();
                break;
            case "TIME":
                sql = ";";
                System.out.println(sql);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(new Coordinate(
                            resultSet.getLong("CDT_ID"),
                            resultSet.getDouble("CDT_LONGITUDE"),
                            resultSet.getDouble("CDT_LATITUDE"),
                            resultSet.getString("CDT_DATETIME"),
                            null == resultSet.getString("CDT_DANGERTYPE") ?
                                    null : Coordinate.Dangertype.valueOf(resultSet.getString("CDT_DANGERTYPE")),
                            resultSet.getString("CDT_DESCRIPTION"),
                            resultSet.getString("CDT_USR_NAME")
                    ));
                }
                resultSet.close();
                statement.close();
                connection.commit();
                break;
            case "USER":
                sql = "SELECT * FROM coordinate WHERE CDT_USR_NAME = " +
                        EntityRelatesToSQL.escapeSingleQuotes(params.get("USERNAME")) + ";";
                System.out.println(sql);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(new Coordinate(
                            resultSet.getLong("CDT_ID"),
                            resultSet.getDouble("CDT_LONGITUDE"),
                            resultSet.getDouble("CDT_LATITUDE"),
                            resultSet.getString("CDT_DATETIME"),
                            null == resultSet.getString("CDT_DANGERTYPE") ?
                                    null : Coordinate.Dangertype.valueOf(resultSet.getString("CDT_DANGERTYPE")),
                            resultSet.getString("CDT_DESCRIPTION"),
                            resultSet.getString("CDT_USR_NAME")
                    ));
                }
                resultSet.close();
                statement.close();
                connection.commit();
                break;
            case NO_QUERT_KEY:
                sql = "SELECT * FROM coordinate;";
                System.out.println(sql);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(new Coordinate(
                            resultSet.getLong("CDT_ID"),
                            resultSet.getDouble("CDT_LONGITUDE"),
                            resultSet.getDouble("CDT_LATITUDE"),
                            resultSet.getString("CDT_DATETIME"),
                            null == resultSet.getString("CDT_DANGERTYPE") ?
                                    null : Coordinate.Dangertype.valueOf(resultSet.getString("CDT_DANGERTYPE")),
                            resultSet.getString("CDT_DESCRIPTION"),
                            resultSet.getString("CDT_USR_NAME")
                    ));
                }
                resultSet.close();
                statement.close();
                connection.commit();
                break;
            default:
                results = null;
                break;
        }
        return results;
    }

    static synchronized List<User> selectUsers(Map<String, String> params) throws SQLException {
        String sql;
        Statement statement;
        ResultSet resultSet;
        List<User> results;
        switch (params.getOrDefault("QUERY", NO_QUERT_KEY).toUpperCase()) {
            case "USERNAME":
                sql = "SELECT * FROM user WHERE USR_NAME = " +
                        EntityRelatesToSQL.escapeSingleQuotes(params.get("USERNAME")) + ";";
                System.out.println(sql);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(new User(
                            resultSet.getString("USR_NAME"),
                            resultSet.getInt("USR_HASHEDPASSWORD"),
                            resultSet.getString("USR_EMAIL"),
                            resultSet.getString("USR_PHONE")
                    ));
                }
                resultSet.close();
                statement.close();
                connection.commit();
                break;
            case "HASHEDPASSWORD":
                sql = "SELECT * FROM user WHERE USR_HASHEDPASSWORD = " + params.get("HASHEDPASSWORD") + ";";
                System.out.println(sql);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(new User(
                            resultSet.getString("USR_NAME"),
                            resultSet.getInt("USR_HASHEDPASSWORD"),
                            resultSet.getString("USR_EMAIL"),
                            resultSet.getString("USR_PHONE")
                    ));
                }
                resultSet.close();
                statement.close();
                connection.commit();
                break;
            case "EMAIL":
                sql = "SELECT * FROM user WHERE USR_EMAIL = " +
                        EntityRelatesToSQL.escapeSingleQuotes(params.get("EMAIL")) + ";";
                System.out.println(sql);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(new User(
                            resultSet.getString("USR_NAME"),
                            resultSet.getInt("USR_HASHEDPASSWORD"),
                            resultSet.getString("USR_EMAIL"),
                            resultSet.getString("USR_PHONE")
                    ));
                }
                resultSet.close();
                statement.close();
                connection.commit();
                break;
            case "PHONE":
                sql = "SELECT * FROM user WHERE USR_PHONE = " +
                        EntityRelatesToSQL.escapeSingleQuotes(params.get("PHONE")) + ";";
                System.out.println(sql);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(new User(
                            resultSet.getString("USR_NAME"),
                            resultSet.getInt("USR_HASHEDPASSWORD"),
                            resultSet.getString("USR_EMAIL"),
                            resultSet.getString("USR_PHONE")
                    ));
                }
                resultSet.close();
                statement.close();
                connection.commit();
                break;
            case NO_QUERT_KEY:
                sql = "SELECT * FROM user;";
                System.out.println(sql);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(new User(
                            resultSet.getString("USR_NAME"),
                            resultSet.getInt("USR_HASHEDPASSWORD"),
                            resultSet.getString("USR_EMAIL"),
                            resultSet.getString("USR_PHONE")
                    ));
                }
                resultSet.close();
                statement.close();
                connection.commit();
                break;
            default:
                results = null;
                break;
        }
        return results;
    }
}