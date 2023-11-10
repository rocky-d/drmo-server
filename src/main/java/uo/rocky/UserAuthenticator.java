package uo.rocky;

import com.sun.net.httpserver.BasicAuthenticator;
import uo.rocky.entity.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UserAuthenticator extends BasicAuthenticator {
    public UserAuthenticator(String realm) {
        super(realm);
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("QUERY", "username");
        queryParameters.put("USERNAME", username);
        try {
            List<User> users = User.selectUserList(queryParameters);
            return null != users && 1 == users.size() && password.hashCode() == users.get(0).getHashedpassword();
        } catch (SQLException sqlException) {
            // TODO
            System.out.println(sqlException.getClass().getSimpleName() + sqlException.getMessage());
            throw new RuntimeException(sqlException);
        }
    }
}