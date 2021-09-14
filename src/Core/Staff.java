package Core;

/**
 * This class represents a Staff user.
 *
 * @author Noah Lenagan, Paris Kelly Skopelitis
 * @author Kaleb Tuck(V.2.0)
 * @author Matt LLewellyn(V.2.0)
 * @version 2.0
 */
public class Staff extends User {

    /**
     * The employment date of the member of staff.
     */
    private String employmentDate;
    /**
     * The member of staff's staff number.
     */
    private int staffNum;

    /**
     * Creates a staff user with specified account id, username, first name,
     * last name, telephone number, street number, street name, county, city and
     * postcode.
     *
     * @param userID A unique identifier for the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param telNum The telephone number of the user.
     * @param streetNum The street number of the user's address.
     * @param streetName The street name of the user's address..
     * @param county The county of the user's address.
     * @param city The city of the user's address.
     * @param postCode The post code of the user's address.
     * @param employmentDate The employment date of the member of staff.
     * @param staffNum The member of staff's staff number.
     * @param lastLogin The date and time the user last logged in.
     * @param avatarID The unique identifier for the avatar image.
     * @param resourceCap Current resource cap for the user.
     * @param email User email.
     */
    public Staff(int userID, String firstName, String lastName,
            String telNum, String streetNum, String streetName, String county,
            String city, String postCode, String employmentDate, int staffNum,
            String lastLogin, int avatarID, int resourceCap, String email) {

        super(userID, firstName, lastName, telNum, streetNum,
                streetName, county, city, postCode, lastLogin, avatarID, resourceCap, email);

        this.employmentDate = employmentDate;
        this.staffNum = staffNum;

    }

    /**
     * Get the Employment Date
     *
     * @return The Employment Date.
     */
    public String getEmploymentDate() {
        return employmentDate;
    }

    /**
     * Set the Employment Date
     *
     * @param employmentDate The new Employment Date.
     */
    public void setEmploymentDate(String employmentDate) {
        this.employmentDate = employmentDate;
    }

    /**
     * Get the staff number.
     *
     * @return The staff number.
     */
    public int getStaffNum() {
        return staffNum;
    }

    /**
     * Get the staff number.
     *
     * @param staffNum The new staff number.
     */
    public void setStaffNum(int staffNum) {
        this.staffNum = staffNum;
    }

    /**
     * Creates a summary of information for the member of Staff.
     *
     * @return Returns a summary of the member of Staff.
     */
    @Override
    public String toString() {

        //create summary
        return super.toString()
                + "\nEmployment Date - " + employmentDate
                + "\nStaff Number - " + staffNum;
    }
}
