package uo.rocky;

import com.sun.net.httpserver.BasicAuthenticator;
import uo.rocky.entity.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
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
     * Constructs a new {@link UserAuthenticator} object with a realm message.
     *
     * @param realm the message of the realm.
     */
    public UserAuthenticator(String realm) {
        super(realm);
    }

    /**
     * Hashes passwords to implement password encryption.
     *
     * @param password the password to be hashed.
     * @return the hashed password.
     */
    public static long hashPassword(String password) {
        final String DIGEST_ALGORITHM = "SHA-256";
        final String SALT = "6GYxNi78Dqd2I";
        try {
            long hashedPassword = 0;
            for (byte b : MessageDigest.getInstance(DIGEST_ALGORITHM).digest((password + SALT).getBytes(UTF_8))) {
                hashedPassword <<= 8;
                hashedPassword |= (b & 0xFF);
            }
            return hashedPassword;
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            LogWriter.appendLogEntry(ERROR, noSuchAlgorithmException.getClass().getName() + ": " + noSuchAlgorithmException.getMessage());

            noSuchAlgorithmException.printStackTrace(System.err);
            System.exit(-1);
            return 0;
        }
    }

    /**
     * Checks user credentials for authentication.
     *
     * @param username the username of the user to be authenticated.
     * @param password the password of the user to be authenticated.
     * @return {@code true} if only one pair of username and password in the database
     * is exactly the same as provided; otherwise {@code false}.
     */
    @Override
    public boolean checkCredentials(String username, String password) {
        try {
            List<User> users = User.selectUserList(Stream.of(new String[]{"QUERY", "USERNAME"}, new String[]{"USERNAME", username}).collect(Collectors.toMap(pair -> pair[0], pair -> pair[1])));
            return 1 == users.size() && hashPassword(password) == users.get(0).getHashedpassword();
        } catch (SQLException sqlException) {
            LogWriter.appendLogEntry(ERROR, sqlException.getClass().getName() + ": " + sqlException.getMessage());

            sqlException.printStackTrace(System.err);
            System.exit(-1);
            return false;
        }
    }
}