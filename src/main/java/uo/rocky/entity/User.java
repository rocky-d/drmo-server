package uo.rocky.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public final class User extends EntityBase implements EntityRelatesToJSON, EntityRelatesToSQLite {
    private static Connection connection = null;

    private String name;
    private int hashedpassword;
    private String email;
    private String phone;

    public User(String name, int hashedpassword, String email, String phone) {
        this.name = name;
        this.hashedpassword = hashedpassword;
        this.email = email;
        this.phone = phone;
    }

    public static User valueOf(JSONObject jsonObject) throws JSONException {
        return new User(
                jsonObject.getString("username"),
                jsonObject.getString("password").hashCode(),
                jsonObject.has("email") ? jsonObject.getString("email") : null,
                jsonObject.has("phone") ? jsonObject.getString("phone") : null
        );
    }

    static Connection getConnection() {
        return connection;
    }

    static void setConnection(Connection connection) {
        User.connection = connection;
    }

    public static synchronized List<User> selectSQLite(Map<String, String> params) throws Exception {
        return EntitySQLiteConnection.selectUser(params);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHashedpassword() {
        return hashedpassword;
    }

    public void setHashedpassword(int hashedpassword) {
        this.hashedpassword = hashedpassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("name=" + (null == name ? "null" : "'" + name + "'"))
                .add("hashedpassword=" + hashedpassword)
                .add("email=" + (null == email ? "null" : "'" + email + "'"))
                .add("phone=" + (null == phone ? "null" : "'" + phone + "'"))
                .toString();
    }

    @Override
    public String toJSONString() {
        return new StringJoiner(",", "{", "}")
                .add("\"username\"=" + EntityRelatesToJSON.escapeDoubleQuotes(name))
                .add("\"hashedpassword\"=\"" + hashedpassword + "\"")
                .add("\"email\"=" + EntityRelatesToJSON.escapeDoubleQuotes(email))
                .add("\"phone\"=" + EntityRelatesToJSON.escapeDoubleQuotes(phone))
                .toString();
    }

    @Override
    public synchronized boolean insertSQLite() throws Exception {
        // TODO
//        Class.forName("org.sqlite.JDBC");

        String query = String.format("INSERT INTO user" +
                        " (USR_NAME,USR_HASHEDPASSWORD,USR_EMAIL,USR_PHONE)" +
                        " VALUES (%s,%s,%s,%s);",
                EntityRelatesToSQLite.escapeSingleQuotes(name),
                hashedpassword,
                EntityRelatesToSQLite.escapeSingleQuotes(email),
                EntityRelatesToSQLite.escapeSingleQuotes(phone)
        );
        System.out.println(query);

        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
        connection.commit();

        return true;
    }

    @Override
    public synchronized boolean deleteSQLite() throws Exception {
        // TODO
        return true;
    }

    @Override
    public synchronized boolean updateSQLite() throws Exception {
        // TODO
        return true;
    }
}