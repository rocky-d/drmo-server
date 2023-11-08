package uo.rocky.entity;

import java.sql.SQLException;

public interface EntityRelatesToSQL {
    static String escapeSingleQuotes(String string) {
        return null == string ? "NULL" : "'" + string.replace("'", "''") + "'";
    }

    boolean insertSQL() throws SQLException;  // TODO: redefine exception

    boolean deleteSQL() throws SQLException;  // TODO: redefine exception

    boolean updateSQL() throws SQLException;  // TODO: redefine exception
}