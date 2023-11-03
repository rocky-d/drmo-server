package uo.rocky.entity;

import java.sql.Connection;

public interface RelatesToSQLite {
    static String escapeSingleQuotes(String string) {
        return null == string ? "NULL" : string.replace("'", "''");
    }

    static String escapeDoubleQuotes(String string) {
        return null == string ? "NULL" : string.replace("\"", "\"\"");
    }

    static String escapeQuotes(String string) {
        return escapeDoubleQuotes(escapeSingleQuotes(string));
    }

    void insertSQLite(Connection connection) throws Exception;  // TODO: redefine exception

    void deleteSQLite(Connection connection) throws Exception;  // TODO: redefine exception

    void updateSQLite(Connection connection) throws Exception;  // TODO: redefine exception

    void selectSQLite(Connection connection) throws Exception;  // TODO: redefine exception
}