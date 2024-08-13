package reviews;

public class Review {

    private String id;
    private String userName;
    private String reviewDescription;
    private String rating;
    private String bookId;

    public Review() {
    }

    public Review(String id, String userName, String reviewDescription, String rating, String bookId) {
        this.id = id;
        this.userName = userName;
        this.reviewDescription = reviewDescription;
        this.rating = rating;
        this.bookId = bookId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReviewDescription() {
        return this.reviewDescription;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public String getRating() {
        return this.rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getBookId() {
        return this.bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "Review{" +
            " id='" + getId() + '\'' +
            ", userName='" + getUserName() + '\'' +
            ", reviewDescription='" + getReviewDescription() + '\'' +
            ", rating='" + getRating() + '\'' +
            ", bookId='" + getBookId() + '\'' +
            "}";
    }
}
