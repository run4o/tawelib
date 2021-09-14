package Core;

/**
 * This class represents the exception when there are duplicate resources.
 *
 * @author Noah Lenagan
 * @version 1.0
 */
public class ResourceDuplicateException extends IllegalArgumentException {

    /**
     * Creates the class.
     */
    ResourceDuplicateException() {
        super("Resource already exists in database.");
    }
}
