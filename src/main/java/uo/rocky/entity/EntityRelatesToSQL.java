package uo.rocky.entity;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

/**
 * TOWRITE
 *
 * @author Rocky Haotian Du
 */
public interface EntityRelatesToSQL {

    DateTimeFormatter LOCALDATETIME_FORMATTER_SPACE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    static String escapeSingleQuotes(String string) {
        return null == string ? "NULL" : "'" + string.replace("'", "''") + "'";
    }

    boolean insertSQL() throws SQLException;

    boolean deleteSQL() throws SQLException;

    boolean updateSQL() throws SQLException;
}