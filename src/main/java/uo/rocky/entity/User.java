package uo.rocky.entity;

import org.json.JSONArray;
import org.json.JSONObject;
import uo.rocky.UserAuthenticator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO
 * <p>
 * Extends {@link EntityBase}.
 *
 * @author Rocky Haotian Du
 */
public final class User extends EntityBase {

    private String name;
    private long hashedpassword;
    private String email;
    private String phone;

    public User(String name, long hashedpassword, String email, String phone) {
        this.name = name;
        this.hashedpassword = hashedpassword;
        this.email = email;
        this.phone = phone;
    }

    public static User valueOf(JSONObject jsonObject) {
        return new User(
                jsonObject.getString("username"),
                UserAuthenticator.hashPassword(jsonObject.getString("password")),
                jsonObject.has("email") ? jsonObject.getString("email") : null,
                jsonObject.has("phone") ? jsonObject.getString("phone") : null
        );
    }

    public static User valueOf(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getString("USR_NAME"),
                resultSet.getLong("USR_HASHEDPASSWORD"),
                resultSet.getString("USR_EMAIL"),
                resultSet.getString("USR_PHONE")
        );
    }

    public static boolean insertUser(User user) throws SQLException {
        return user.insertSQL();
    }

    public static boolean deleteUser() throws SQLException {
        return false;
    }

    public static boolean updateUser() throws SQLException {
        return false;
    }

    public static List<User> selectUserList(Map<String, String> params) throws SQLException {
        return EntityDBConnection.selectUsers(params);
    }

    public static String selectUserJSONString(Map<String, String> params) throws SQLException {
        return EntityDBConnection.selectUsers(params).stream().map(User::toJSONString).collect(Collectors.joining(",", "[", "]"));
    }

    public static JSONArray selectUserJSONArray(Map<String, String> params) throws SQLException {
        return EntityDBConnection.selectUsers(params).stream().map(User::toJSONObject).collect(JSONArray::new, JSONArray::put, JSONArray::put);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getHashedpassword() {
        return hashedpassword;
    }

    public void setHashedpassword(long hashedpassword) {
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
        return new StringJoiner(", ", User.class.getSimpleName() + "{", "}")
                .add("name=" + (null == name ? "null" : "'" + name + "'"))
                .add("hashedpassword=" + hashedpassword)
                .add("email=" + (null == email ? "null" : "'" + email + "'"))
                .add("phone=" + (null == phone ? "null" : "'" + phone + "'"))
                .toString();
    }

    @Override
    public String toJSONString() {
        return new StringJoiner(",", "{", "}")
                .add("\"username\":" + EntityRelatesToJSON.escapeDoubleQuotes(name))
                .add("\"hashedpassword\":\"" + hashedpassword + "\"")
                .add("\"email\":" + EntityRelatesToJSON.escapeDoubleQuotes(email))
                .add("\"phone\":" + EntityRelatesToJSON.escapeDoubleQuotes(phone))
                .toString();
    }

    @Override
    public synchronized boolean insertSQL() throws SQLException {
        if (!EntityDBConnection.selectUsers(Stream.of(new String[]{"QUERY", "USERNAME"}, new String[]{"USERNAME", name}).collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]))).isEmpty()) {
            return false;
        }

        String sql = String.format("INSERT INTO user" +
                        " (USR_NAME,USR_HASHEDPASSWORD,USR_EMAIL,USR_PHONE)" +
                        " VALUES (%s,%s,%s,%s);",
                EntityRelatesToSQL.escapeSingleQuotes(name),
                hashedpassword,
                EntityRelatesToSQL.escapeSingleQuotes(email),
                EntityRelatesToSQL.escapeSingleQuotes(phone)
        );
//        System.out.println(sql);

        Statement statement = getConnection().createStatement();
        statement.executeUpdate(sql);
        statement.close();
        getConnection().commit();
        return true;
    }

    @Override
    public synchronized boolean deleteSQL() throws SQLException {
        return false;
    }

    @Override
    public synchronized boolean updateSQL() throws SQLException {
        return false;
    }
}