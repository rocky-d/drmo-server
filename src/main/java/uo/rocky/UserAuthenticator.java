package uo.rocky;

import com.sun.net.httpserver.BasicAuthenticator;
import uo.rocky.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAuthenticator extends BasicAuthenticator {
    public UserAuthenticator(String realm) {
        super(realm);
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("QUERY", "username");
        paramsMap.put("USERNAME", username);
        try {
            List<User> users = User.selectUserList(paramsMap);
            return null != users && 1 == users.size() && password.hashCode() == users.get(0).getHashedpassword();
        } catch (Exception exception) {
            // TODO
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
}