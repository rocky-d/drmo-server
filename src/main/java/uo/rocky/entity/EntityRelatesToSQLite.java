package uo.rocky.entity;

public interface EntityRelatesToSQLite {
    static String escapeSingleQuotes(String string) {
        return null == string ? "NULL" : "'" + string.replace("'", "''") + "'";
    }

    boolean insertSQL() throws Exception;  // TODO: redefine exception

    boolean deleteSQL() throws Exception;  // TODO: redefine exception

    boolean updateSQL() throws Exception;  // TODO: redefine exception

//    List<EntityRelatesToSQLite> selectSQLite() throws Exception;  // TODO: redefine exception
}