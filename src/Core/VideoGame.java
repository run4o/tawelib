package Core;

/**
 * Class to model a video game.
 *
 * @author Rory Richards
 * @version 1.0
 */
public class VideoGame extends Resource {

    /**
     * The publisher of the video game.
     */
    private String publisher;

    /**
     * The genre of the video game.
     */
    private String genre;

    /**
     * The certificate rating of the video game.
     */
    private String certRating;

    /**
     * Whether or not the video game has multiplayer capability.
     */
    private String multiplayer;

    /**
     * Constructor for a video game.
     *
     * @param resourceID The unique identifier of the video game.
     * @param title The title of the video game.
     * @param year The release year of the video game.
     * @param thumbImageID Identifies the thumbnail of the image.
     * @param dateCreated Date created.
     * @param publisher The publisher of the video game.
     * @param genre The genre of the video game.
     * @param certRating The certificate rating of the video game.
     * @param multiplayer Whether or not the video game has multiplayer
     * capability.
     */
    public VideoGame(int resourceID, String title, int year, int thumbImageID, String dateCreated, String publisher, String genre,
            String certRating, String multiplayer) {

        super(resourceID, title, year, thumbImageID, dateCreated);

        this.publisher = publisher;
        this.genre = genre;
        this.certRating = certRating;
        this.multiplayer = multiplayer;
    }

    /**
     * Method to return the publisher of the video game.
     *
     * @return The publisher of the video game.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Method to set the publisher of the video game.
     *
     * @param publisher The publisher of the video game.
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Method to return the genre of the video game.
     *
     * @return The genre of the video game.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Method to set the genre of the video game.
     *
     * @param genre The genre of the video game.
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Method to return the certificate rating of the video game.
     *
     * @return The certificate rating of the video game.
     */
    public String getCertRating() {
        return certRating;
    }

    /**
     * Method to set the certificate rating of the video game.
     *
     * @param certRating The certificate rating of the video game.
     */
    public void setCertRating(String certRating) {
        this.certRating = certRating;
    }

    /**
     * Method to return whether the game is multiplayer or not.
     *
     * @return Whether the game is multiplayer or not.
     */
    public String getMultiplayer() {
        return multiplayer;
    }

    /**
     * Method to set whether the game is multiplayer or not.
     *
     * @param multiplayer Whether the game is multiplayer or not.
     */
    public void setMultiplayer(String multiplayer) {
        this.multiplayer = multiplayer;
    }

    /**
     * Method to create a summary of information for the video game.
     *
     * @return A brief summary of the video game.
     */
    @Override
    public String toString() {
        return super.toString()
                + "\nType - Video Game"
                + "\nPublisher - " + publisher
                + "\nGenre - " + genre
                + "\nCertificate Rating - " + certRating
                + "\nMultiplayer - " + multiplayer;
    }

}
