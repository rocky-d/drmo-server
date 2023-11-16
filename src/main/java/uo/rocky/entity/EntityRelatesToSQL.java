package uo.rocky.entity;

import java.sql.SQLException;

/**
 * TODO
 *
 * @author Rocky Haotian Du
 */
public interface EntityRelatesToSQL {

    static String escapeSingleQuotes(String string) {
        return null == string ? "NULL" : "'" + string.replace("'", "''") + "'";
    }

    boolean insertSQL() throws SQLException;

    boolean deleteSQL() throws SQLException;

    boolean updateSQL() throws SQLException;
}