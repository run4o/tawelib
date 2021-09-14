package Core;

/**
 * This class represents a Computer.
 *
 * @author Noah Lenagan, Paris Kelly Skopelitis
 * @version 1.0
 */
public class Computer extends Resource {

    /**
     * The manufacturer of the computer.
     */
    private String manufacturer;

    /**
     * The model of the computer.
     */
    private String model;

    /**
     * The installed os.
     */
    private String os;

    /**
     * Creates a computer.
     *
     * @param resourceID Uniquely identifies the computer.
     * @param title The name of the computer.
     * @param year The release year of the computer.
     * @param thumbImageID Identifies the thumbnail image of the computer.
     * @param dateCreated The date the computer was created.
     * @param manufacturer The computer manufacturer.
     * @param model The computer model.
     * @param os The installed os.
     */
    public Computer(int resourceID, String title, int year, int thumbImageID, String dateCreated, String manufacturer, String model,
            String os) {

        super(resourceID, title, year, thumbImageID, dateCreated);

        this.manufacturer = manufacturer;
        this.model = model;
        this.os = os;

    }

    /**
     * Gets the manufacturer of the computer.
     *
     * @return The manufacturer of the computer.
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets the manufacturer of computer.
     *
     * @param manufacturer The new manufacturer.
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Gets the model of the computer.
     *
     * @return The computer model.
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model of the computer.
     *
     * @param model The new model.
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the installed os of the computer.
     *
     * @return The installed os of the computer.
     */
    public String getOs() {
        return os;
    }

    /**
     * Sets the installed os.
     *
     * @param os The new installed os.
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * Creates a summary of information for the Computer.
     *
     * @return Returns a summary of the Computer.
     */
    @Override
    public String toString() {

        //create summary
        return super.toString()
                + "\nType - Computer"
                + "\nManufacturer - " + manufacturer
                + "\nModel - " + model
                + "\nInstalled OS - " + os;

    }
}
