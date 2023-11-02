package programming3.rocky.entity;

public interface WithSQLite {
    void insertWithSQLite() throws Exception;

    void deleteWithSQLite() throws Exception;

    void updateWithSQLite() throws Exception;

    void selectWithSQLite() throws Exception;
}