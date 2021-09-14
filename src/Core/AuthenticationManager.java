package Core;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Responsible for authenticating accounts.
 *
 * @author Daryl Tan
 * @author Martin Trifonov
 * @version 2.0
 */
public class AuthenticationManager {

    /**
     * The username.
     */
    private final String username;

    /**
     * The database manager instance.
     */
    private final DatabaseManager dbManager;

    /**
     * Constructor for the Authentication manager.
     *
     * @param username Username of an account.
     * @param dbManager The database manager.
     */
    public AuthenticationManager(String username, DatabaseManager dbManager) {
        this.username = username;
        this.dbManager = dbManager;
    }

    private static byte[] convertHash(String hash) {
        byte[] bytes = new byte[hash.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hash.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /**
     * Checks if the provided input is the same as in the Database.
     *
     * @param key User input.
     * @param serverKey Database key.
     * @return boolean.
     */
    public static boolean checkKey(String key, String serverKey) {
        try {
            String[] parts = serverKey.split(":");
            int iterations = Integer.parseInt(parts[0]);

            byte[] salt = convertHash(parts[1]);
            byte[] hash = convertHash(parts[2]);
            //hashes user input
            PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), salt, iterations, hash.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] testHash = skf.generateSecret(spec).getEncoded();

            //checks if out is the same
            if (Arrays.equals(testHash, hash)) {
                return true;
            } else {
                return false;
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Method that authenticates a user through their username.
     *
     * @param key User input for password.
     * @param userID User trying to login.
     * @return true if the username can be authenticated, else false.
     *
     * @throws IllegalArgumentException Thrown if user does not exist.
     * @throws Exception Password incorrect.
     */
    public boolean authenticate(String key, String userID) throws IllegalArgumentException, Exception {
        try {
            if (dbManager.checkIfExist("User", new String[]{"UID"}, new String[]{userID})) {
                //checks if out is the same
                String serverKey = dbManager.getTupleListByQuery("Select Key From User Where UID= " + userID)[0][0];
                //checks if out is the same
                if (checkKey(key, serverKey)) {
                    return true;
                } else {
                    throw new Exception("Wrong password");
                }
            } else {
                throw new IllegalArgumentException("Account corresponding to specified username does not exist");
            }
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Method that checks whether a user is a staff member.
     *
     * @return true if specified user is a staff member, else false.
     *
     * @throws SQLException Thrown if failed to connect to database.
     */
    public boolean isStaff() throws SQLException {
        return dbManager.checkIfExist("Staff", new String[]{"UID"}, new String[]{username});
    }
}
