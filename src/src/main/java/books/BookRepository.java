package books;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import reviews.Review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BookRepository {

    private static final String TABLE_NAME = "books";
    private final Table table;
    private AmazonDynamoDB dynamoDB;

    public BookRepository() {
        dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        table = new DynamoDB(dynamoDB).getTable(TABLE_NAME);
    }

    public List<Book> getAllBooks() {
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(TABLE_NAME);
        ScanResult res = dynamoDB.scan(scanRequest);
        List<Item> itemList = ItemUtils.toItemList(res.getItems());
        List<Book> books = new ArrayList<>();
        for (Item item: itemList){
            Book book = new Book();
            book.setId(item.getString("id"));
            book.setTitle(item.getString("title"));
            book.setSummary(item.getString("summary"));
            book.setAuthor(item.getString("author"));
            book.setPublisher(item.getString("publisher"));
            book.setPublishDate(item.getString("publishDate"));
            book.addReview(null);
            books.add(book);
        }
        return books;
    }

    public Book getBookById(String id){
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("id", id);
        Item item = table.getItem(spec);
        Book book = new Book();
        book.setId(item.getString("id"));
        book.setTitle(item.getString("title"));
        book.setSummary(item.getString("summary"));
        book.setAuthor(item.getString("author"));
        book.setPublisher(item.getString("publisher"));
        book.setPublishDate(item.getString("publishDate"));
        book.setReviews(item.getList("reviews"));
        return book;
    }

    public Book createBook(Book book) {
        table.putItem(new Item()
                .withPrimaryKey("id", UUID.randomUUID().toString())
                .withString("title", book.getTitle())
                .withString("summary", book.getSummary())
                .withString("author", book.getAuthor())
                .withString("publisher", book.getPublisher())
                .withString("publishDate", book.getPublishDate())
                .withList("reviews", book.getReviews())
                );
        return book;
    }

    public Boolean deleteBook(String id) {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("id", id));
        return table.deleteItem(deleteItemSpec).getItem() == null;
    }

    public Boolean updateBook(String id, Book updatedBook){
        List<Map<String, Object>> reviewMaps = new ArrayList<>();
        for (Review review : updatedBook.getReviews()) {
            Map<String, Object> reviewMap = new HashMap<>();
            reviewMap.put("id", review.getId());
            reviewMap.put("userName", review.getUserName());
            reviewMap.put("rating", review.getRating());
            reviewMap.put("bookId", review.getBookId());
            reviewMap.put("reviewDescription", review.getReviewDescription());
            reviewMaps.add(reviewMap);
        }        
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey(new PrimaryKey("id", id))
                .withUpdateExpression("set title = :t, summary = :s, author = :a, publisher = :p, publishDate = :d, reviews = list_append(reviews, :vals)")
                .withValueMap(new ValueMap()
                        .withString(":t", updatedBook.getTitle())
                        .withString(":s", updatedBook.getSummary())
                        .withString(":a", updatedBook.getAuthor())
                        .withString(":p", updatedBook.getPublisher())
                        .withString(":d", updatedBook.getPublishDate())
                        .withList(":vals", reviewMaps))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        return table.updateItem(updateItemSpec).getItem() != null;
    }
}
