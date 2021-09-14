package Core;

/**
 * This class represents a book.
 *
 * @author Noah Lenagan, Paris Kelly Skopelitis
 * @version 1.0
 */
public class Book extends Resource {

    /**
     * The author of the book.
     */
    private String author;

    /**
     * The publisher of the book.
     */
    private String publisher;

    /**
     * The genre of the book.
     */
    private String genre;

    /**
     * The isbn of the book.
     */
    private String isbn;

    /**
     * The language of the book.
     */
    private String lang;

    /**
     * Creates a book.
     *
     * @param resourceID Uniquely identifies the book.
     * @param title The title of the book.
     * @param year The release date of the book.
     * @param thumbImageID Identifies the thumbnail image.
     * @param dateCreated The date the book was created.
     * @param author The author of the book.
     * @param publisher The publisher of the book.
     * @param genre The genre of the book.
     * @param isbn The isbn of the book.
     * @param lang The language of the book.
     */
    public Book(int resourceID, String title, int year, int thumbImageID, String dateCreated, String author, String publisher, String genre,
            String isbn, String lang) {

        super(resourceID, title, year, thumbImageID, dateCreated);

        this.author = author;
        this.publisher = publisher;
        this.genre = genre;
        this.isbn = isbn;
        this.lang = lang;

    }

    /**
     * Gets the author.
     *
     * @return The author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the author of the book.
     *
     * @param author The new author.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the publisher.
     *
     * @return The publisher.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the publisher of the book.
     *
     * @param publisher The new publisher.
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Gets the genre.
     *
     * @return The genre.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the book.
     *
     * @param genre The new genre.
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets the isbn.
     *
     * @return The isbn.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the isbn of the book.
     *
     * @param isbn The new isbn.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets the language.
     *
     * @return The language.
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the language of the book.
     *
     * @param lang The new language.
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * Creates a summary of information for the Book.
     *
     * @return Returns a summary of the Book.
     */
    @Override
    public String toString() {

        //create summary
        return super.toString()
                + "\nType - Book"
                + "\nAuthor - " + author
                + "\nPublisher - " + publisher
                + "\nGenre - " + genre
                + "\nISBN - " + isbn
                + "\nLanguage - " + lang;

    }
}
