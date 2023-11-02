package programming3.rocky.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringJoiner;

public final class User implements RelateToJSON, RelateToSQLite {
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

    public User(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString("username");
        hashedpassword = jsonObject.getString("password").hashCode();
        email = jsonObject.has("email") ? jsonObject.getString("email") : null;
        phone = jsonObject.has("phone") ? jsonObject.getString("phone") : null;
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
                .add("name='" + name + "'")
                .add("hashedpassword=" + hashedpassword)
                .add("email='" + email + "'")
                .add("phone='" + phone + "'")
                .toString();
    }

    @Override
    public String toJSONString() {
        return new StringJoiner(",", "{", "}")
                .toString();  // TODO
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        return RelateToJSON.super.toJSONObject();
    }

    @Override
    public void insertWithSQLite() throws Exception {
        // TODO
    }

    @Override
    public void deleteWithSQLite() throws Exception {
        // TODO
    }

    @Override
    public void updateWithSQLite() throws Exception {
        // TODO
    }

    @Override
    public void selectWithSQLite() throws Exception {
        // TODO
    }
}