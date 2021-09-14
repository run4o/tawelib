package Core;

/**
 * This class represents a resource and its information.
 *
 * @author Noah Lenagan, Paris Kelly Skopelitis
 * @version 1.0
 */
public class Resource {

    /**
     * Uniquely identifies the resource.
     */
    protected final int resourceID;

    /**
     * The title of the resource.
     */
    protected String title;

    /**
     * The release year of the resource.
     */
    protected int year;

    /**
     * The thumbnail image id the identifies an image.
     */
    protected int thumbImageID;

    /**
     * The date the resource was created.
     */
    protected String dateCreated;

    /**
     * Creates a resource with specified id, title, release year and image id.
     *
     * @param resourceID A unique identifier for the resource.
     * @param title The resource title/name.
     * @param year The release year.
     * @param thumbImageID The thumbnail image id which identifies a certain
     * @param dateCreated The date the resource was created. image.
     */
    public Resource(int resourceID, String title, int year, int thumbImageID, String dateCreated) {

        this.resourceID = resourceID;
        this.title = title;
        this.year = year;
        this.thumbImageID = thumbImageID;
        this.dateCreated = dateCreated;

    }

    /**
     * Get the resource id.
     *
     * @return The resource id.
     */
    public int getResourceID() {
        return resourceID;
    }

    /**
     * Get the resource title.
     *
     * @return The resource title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the resource title.
     *
     * @param title The new resource title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the release year.
     *
     * @return The release year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Set the release year.
     *
     * @param year The new release year.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Get the thumbnail image id.
     *
     * @return The thumbnail image id.
     */
    public int getThumbImage() {
        return thumbImageID;
    }

    /**
     * Set the thumbnail image id.
     *
     * @param thumbImage The new thumbnail image id.
     */
    public void setThumbImage(int thumbImage) {
        this.thumbImageID = thumbImage;
    }

    /**
     * Get the date the resource was created.
     *
     * @return The date the resource was created.
     */
    public String getdateCreated() {
        return dateCreated;
    }

    /**
     * Set the date the resource was created.
     *
     * @param dateCreated The new date the resource was created.
     */
    public void setdateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Creates a summary of information for the resource.
     *
     * @return Returns a summary of the resource.
     */
    @Override
    public String toString() {

        return "\nTitle - " + title
                + "\nRid - " + resourceID
                + "\nRelease Year - " + year
                + "\nDate Created - " + dateCreated;

    }
}
