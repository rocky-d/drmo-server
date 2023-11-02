package uo.rocky.entity;

public interface RelateToSQLite {
    static String escapeSingleQuotes(String string) {
        return null == string ? "NULL" : string.replace("'", "''");
    }

    static String escapeDoubleQuotes(String string) {
        return null == string ? "NULL" : string.replace("\"", "\"\"");
    }

    static String escapeQuotes(String string) {
        return escapeDoubleQuotes(escapeSingleQuotes(string));
    }

    void insertSQLite() throws Exception;  // TODO: redefine exception

    void deleteSQLite() throws Exception;  // TODO: redefine exception

    void updateSQLite() throws Exception;  // TODO: redefine exception

    void selectSQLite() throws Exception;  // TODO: redefine exception
}