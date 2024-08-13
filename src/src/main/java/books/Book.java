package books;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import reviews.Review;

public class Book {

    private String id;
    private String title;
    private String summary;
    private String author;
    private String publisher;
    private String publishDate;

    private List<Review> reviews = new ArrayList<>();

    public Book() {
    }

    public Book(String id, String title, String summary, String author, String publisher, String publishDate) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishDate() {
        return this.publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public List<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }
    
    @Override
    public String toString() {
        return "Book{" +
            " id='" + getId() + '\'' +
            ", title='" + getTitle() + '\'' +
            ", summary='" + getSummary() + '\'' +
            ", author='" + getAuthor() + '\'' +
            ", publisher='" + getPublisher() + '\'' +
            ", publishDate='" + getPublishDate() + '\'' +
            ", reviews='" + getReviews() + '\'' +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Book)) {
            return false;
        }
        Book book = (Book) o;
        return id.equals(book.id) && title.equals(book.title) && summary.equals(book.summary) && author.equals(book.author) && publisher.equals(book.publisher) && publishDate.equals(book.publishDate) && reviews.equals(book.reviews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, summary, author, publisher, publishDate, reviews);
    }
}
