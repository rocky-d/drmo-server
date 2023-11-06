package uo.rocky.entity;

public interface EntityRelatesToSQLite {
    static String escapeSingleQuotes(String string) {
        return null == string ? "NULL" : "'" + string.replace("'", "''") + "'";
    }

    boolean insertSQLite() throws Exception;  // TODO: redefine exception

    boolean deleteSQLite() throws Exception;  // TODO: redefine exception

    boolean updateSQLite() throws Exception;  // TODO: redefine exception

//    List<EntityRelatesToSQLite> selectSQLite() throws Exception;  // TODO: redefine exception
}