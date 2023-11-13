package uo.rocky;

import com.sun.net.httpserver.BasicAuthenticator;
import uo.rocky.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class UserAuthenticator extends BasicAuthenticator {
    public UserAuthenticator(String realm) {
        super(realm);
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        try {
            List<User> users = User.selectUserList(Stream.of(new String[]{"QUERY", "USERNAME"}, new String[]{"USERNAME", username}).collect(Collectors.toMap(pair -> pair[0], pair -> pair[1])));
            return null != users && 1 == users.size() && password.hashCode() == users.get(0).getHashedpassword();
        } catch (SQLException sqlException) {
            // TODO
            System.out.println(sqlException.getClass().getSimpleName() + ": " + sqlException.getMessage());
            throw new RuntimeException(sqlException);
        }
    }
}