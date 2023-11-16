package uo.rocky;

import com.sun.net.httpserver.BasicAuthenticator;
import uo.rocky.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uo.rocky.LogWriter.LogEntryType.ERROR;

/**
 * Authenticates users for the server.
 * <p>
 * Extends {@link BasicAuthenticator}.
 *
 * @author Rocky Haotian Du
 */
public final class UserAuthenticator extends BasicAuthenticator {

    /**
     * Constructs a {@link UserAuthenticator} instance with a message of realm.
     *
     * @param realm the message of realm.
     */
    public UserAuthenticator(String realm) {
        super(realm);
    }

    /**
     * Checks user credentials for authentication.
     *
     * @param username the username of the user to be authenticated.
     * @param password the password of the user to be authenticated.
     * @return {@code true} if there is only one pair of username and password in the database is the same as provided; otherwise {@code false}.
     */
    @Override
    public boolean checkCredentials(String username, String password) {
        try {
            List<User> users = User.selectUserList(Stream.of(new String[]{"QUERY", "USERNAME"}, new String[]{"USERNAME", username}).collect(Collectors.toMap(pair -> pair[0], pair -> pair[1])));
            return null != users && 1 == users.size() && User.hashPassword(password) == users.get(0).getHashedpassword();
        } catch (SQLException sqlException) {
            LogWriter.appendEntry(ERROR, sqlException.getClass().getName() + ": " + sqlException.getMessage());
//            throw new RuntimeException(sqlException);
            return false;
        }
    }
}