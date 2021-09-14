package Core;

/**
 * This class represents a Dvd.
 *
 * @author Noah Lenagan, Paris Kelly Skopelitis
 * @version 1.0
 */
public class Dvd extends Resource {

    /**
     * The director of the Dvd.
     */
    private String director;

    /**
     * The runTime of the Dvd.
     */
    private int runTime;

    /**
     * The spoken language of the Dvd.
     */
    private String language;

    /**
     * The subtitle languages of the Dvd.
     */
    private String[] subLang;

    /**
     * Creates a Dvd.
     *
     * @param resourceID Uniquely identifies a Dvd.
     * @param title The title of the Dvd.
     * @param year The release year of the Dvd.
     * @param thumbImageID Identifies the thumbnail image of the Dvd.
     * @param dateCreated The date the Dvd was created.
     * @param director The director of the Dvd.
     * @param runTime The runtime of the Dvd.
     * @param language The spoken language of the Dvd.
     * @param subLang The subtitle languages of the Dvd.
     */
    public Dvd(int resourceID, String title, int year, int thumbImageID, String dateCreated, String director, int runTime, String language,
            String[] subLang) {

        super(resourceID, title, year, thumbImageID, dateCreated);

        this.director = director;
        this.runTime = runTime;
        this.language = language;
        this.subLang = subLang;

    }

    /**
     * Gets the director of the Dvd.
     *
     * @return The director of the Dvd.
     */
    public String getDirector() {
        return director;
    }

    /**
     * Sets the director of the Dvd.
     *
     * @param director The new director of the Dvd.
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Gets the runtime of the Dvd.
     *
     * @return The runtime of the Dvd.
     */
    public int getRunTime() {
        return runTime;
    }

    /**
     * Sets the runtime of the Dvd.
     *
     * @param runTime The new runtime of the Dvd.
     */
    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    /**
     * Gets the spoken language of the Dvd.
     *
     * @return The spoken language of the Dvd.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the spoken language of the Dvd.
     *
     * @param language The new spoken language of the Dvd.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Gets the subtitle languages of the Dvd.
     *
     * @return The subtitle languages of the Dvd.
     */
    public String[] getSubLang() {
        return subLang;
    }

    /**
     * Sets the subtitle languages of the Dvd.
     *
     * @param subLang The new subtitle languages of the Dvd.
     */
    public void setSubLang(String[] subLang) {
        this.subLang = subLang;
    }

    /**
     * Creates a summary of information for the Dvd.
     *
     * @return Returns a summary of the Dvd.
     */
    @Override
    public String toString() {

        //calculate runTime in hours and minutes
        final int MIN_IN_HOUR = 60;
        int hours = runTime / MIN_IN_HOUR;
        int min = runTime % MIN_IN_HOUR;

        //create summary
        String out = super.toString()
                + "\nType - Dvd"
                + "\nDirector - " + director
                + "\nRuntime - " + hours + "hr " + min + "min (" + runTime + "min)"
                + "\nLanguage - " + language
                + "\nSubtitle Languages: ";

        //for every subtitle lang, add to summary
        for (String sub : subLang) {
            out += "\n    " + sub;
        }
        return out;

    }
}
