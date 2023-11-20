package uo.rocky.entity;

import java.sql.Connection;

/**
 * TODO
 * <p>
 * Implements {@link EntityRelatesToJSON}, {@link EntityRelatesToSQL}.
 *
 * @author Rocky Haotian Du
 */
public abstract class EntityBase implements EntityRelatesToJSON, EntityRelatesToSQL {

    protected static Connection getConnection() {
        return EntityDBConnection.getConnection();
    }
}