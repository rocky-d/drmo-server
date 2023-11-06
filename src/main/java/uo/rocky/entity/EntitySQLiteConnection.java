package uo.rocky.entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class EntitySQLiteConnection {
    private static Connection connection = null;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        EntitySQLiteConnection.connection = connection;
        setConnectionForAllEntities(connection);
    }

    private static void setConnectionForAllEntities(Connection connection) {
        Comment.setConnection(connection);
        Coordinate.setConnection(connection);
        User.setConnection(connection);
    }

    static synchronized List<Comment> selectComment(Map<String, String> params) throws Exception {
        String query;
        Statement statement;
        ResultSet resultSet;
        List<Comment> results;
        switch (params.getOrDefault("QUERY", "QUERY KEY NOT FOUND").toUpperCase()) {
            case "COMMENTID":
                query = ";";
                System.out.println(query);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
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
                query = ";";
                System.out.println(query);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
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
                query = ";";
                System.out.println(query);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
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
            case "QUERY KEY NOT FOUND":
                results = null;
                break;
            default:
                results = null;
                break;
        }
        return results;
    }

    static synchronized List<Coordinate> selectCoordinate(Map<String, String> params) throws Exception {
        String query;
        Statement statement;
        ResultSet resultSet;
        List<Coordinate> results;
        switch (params.getOrDefault("QUERY", "QUERY KEY NOT FOUND").toUpperCase()) {
            case "ID":
                query = "SELECT * FROM coordinate WHERE CDT_ID = " + params.get("ID") + ";";
                System.out.println(query);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
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
                query = ";";
                System.out.println(query);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
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
                query = ";";
                System.out.println(query);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
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
                query = ";";
                System.out.println(query);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
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
            case "QUERY KEY NOT FOUND":
                results = null;
                break;
            default:
                results = null;
                break;
        }
        return results;
    }

    static synchronized List<User> selectUser(Map<String, String> params) throws Exception {
        String query;
        Statement statement;
        ResultSet resultSet;
        List<User> results;
        switch (params.getOrDefault("QUERY", "QUERY KEY NOT FOUND").toUpperCase()) {
            case "USERNAME":
                query = "SELECT * FROM user WHERE USR_NAME = '" +
                        EntityRelatesToSQLite.escapeSingleQuotes(params.get("USERNAME")) +
                        "';";
                System.out.println(query);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
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
                query = ";";
                System.out.println(query);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
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
                query = ";";
                System.out.println(query);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
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
            case "QUERY KEY NOT FOUND":
                results = null;
                break;
            default:
                results = null;
                break;
        }
        return results;
    }
}