package Core;

/**
 * Reviews data structure.
 *
 * @author Matt Ashman
 * @version 1.0
 */
public class Review {

    /**
     * Uniquely identifies the review.
     */
    private int reviewID;

    /**
     * Uniquely identifies the resource that is being reviewed.
     */
    private int resourceID;

    /**
     * The userId of the user leaving the review
     */
    private int userID;

    /**
     * The rating of the review
     */
    private int rating;

    /**
     * The review itself as a string
     */
    private String review;

    /**
     * Creates a review
     *
     * @param reviewID Uniquely identifies the Review.
     * @param resourceID Uniquely identifies the resource.
     * @param userID The userId of the user leaving the review
     * @param rating The rating of the review
     * @param review The review itself as a string
     */
    public Review(int reviewID, int resourceID, int userID, int rating, String review) {
        this.reviewID = reviewID;
        this.resourceID = resourceID;
        this.userID = userID;
        this.rating = rating;
        this.review = review;
    }

    /**
     * Gets the review ID
     *
     * @return the ID of the review
     */
    public int getReviewID() {
        return reviewID;
    }

    /**
     * Sets the review ID
     *
     * @param reviewID identifies the review
     */
    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    /**
     * Gets the resourceID
     *
     * @return the ID of the resource
     */
    public int getResourceID() {
        return resourceID;
    }

    /**
     * Sets the resource ID
     *
     * @param resourceID the resource ID being reviewed
     */
    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    /**
     * Gets the user ID
     *
     * @return the ID of the user
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the user ID
     *
     * @param userID the user ID who is leaving the review
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Gets the rating
     *
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the rating
     *
     * @param rating the rating of the resource out of 5
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Gets the review
     *
     * @return the review
     */
    public String getReview() {
        return review;
    }

    /**
     * Sets the review description
     *
     * @param review the written review of the resource
     */
    public void setReview(String review) {
        this.review = review;
    }
}
