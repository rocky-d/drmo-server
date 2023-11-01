package programming3.rocky.entity;

public class User {
    private String name;
    private int hashedpassword;
    private String email;
    private String phone;

    public User(String name, int hashedpassword) {
        this.name = name;
        this.hashedpassword = hashedpassword;
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
}