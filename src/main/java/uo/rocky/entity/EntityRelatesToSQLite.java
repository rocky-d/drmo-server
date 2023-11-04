package uo.rocky.entity;

public interface EntityRelatesToSQLite {
    static String escapeSingleQuotes(String string) {
        return null == string ? "NULL" : string.replace("'", "''");
    }

    static String escapeDoubleQuotes(String string) {
        return null == string ? "NULL" : string.replace("\"", "\"\"");
    }

    static String escapeQuotes(String string) {
        return escapeDoubleQuotes(escapeSingleQuotes(string));
    }

    boolean insertSQLite() throws Exception;  // TODO: redefine exception

    boolean deleteSQLite() throws Exception;  // TODO: redefine exception

    boolean updateSQLite() throws Exception;  // TODO: redefine exception

    EntityRelatesToSQLite[] selectSQLite() throws Exception;  // TODO: redefine exception
}