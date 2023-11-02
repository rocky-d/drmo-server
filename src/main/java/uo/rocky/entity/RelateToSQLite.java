package uo.rocky.entity;

public interface RelateToSQLite {
    static String escapeQuotes(String string) {
        return null == string ? "NULL" : string.replace("'", "''");
    }

    void insertSQLite() throws Exception;  // TODO: redefine exception

    void deleteSQLite() throws Exception;  // TODO: redefine exception

    void updateSQLite() throws Exception;  // TODO: redefine exception

    void selectSQLite() throws Exception;  // TODO: redefine exception
}