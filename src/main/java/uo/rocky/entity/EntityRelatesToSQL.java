package uo.rocky.entity;

import java.sql.SQLException;

public interface EntityRelatesToSQL {
    static String escapeSingleQuotes(String string) {
        return null == string ? "NULL" : "'" + string.replace("'", "''") + "'";
    }

    boolean insertSQL() throws SQLException;

    boolean deleteSQL() throws SQLException;

    boolean updateSQL() throws SQLException;
}