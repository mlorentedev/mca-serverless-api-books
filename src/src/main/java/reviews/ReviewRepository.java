package reviews;

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
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReviewRepository {
    
    private static final String TABLE_NAME = "reviews";
    private final Table table;
    private AmazonDynamoDB dynamoDB;

    public ReviewRepository(){
        dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        table = new DynamoDB(dynamoDB).getTable(TABLE_NAME);
    }

    public List<Review> getAllReviews(){
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(TABLE_NAME);
        ScanResult res = dynamoDB.scan(scanRequest);
        List<Item> itemList = ItemUtils.toItemList(res.getItems());
        List<Review> reviews = new ArrayList<>();
        for (Item item: itemList){
            Review review = new Review();
            review.setId(item.getString("id"));
            review.setUserName(item.getString("userName"));
            review.setReviewDescription(item.getString("reviewDescription"));
            review.setRating(item.getString("rating"));
            review.setBookId(item.getString("bookId"));
            reviews.add(review);
        }
        return reviews;
    }

    public Review getReviewById(String id){
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("id", id);
        Item item = table.getItem(spec);
        Review review = new Review();
        review.setId(item.getString("id"));
        review.setUserName(item.getString("userName"));
        review.setReviewDescription(item.getString("reviewDescription"));
        review.setRating(item.getString("rating"));
        return review;
    }

    public Review createReview(Review review) {
        table.putItem(new Item()
                .withPrimaryKey("id", UUID.randomUUID().toString())
                .withString("userName", review.getUserName())
                .withString("reviewDescription", review.getReviewDescription())
                .withString("rating", review.getRating())
                .withString("bookId", review.getBookId()));
        return review;
    }

    public boolean deleteReview(String id) {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("id", id))
                .withReturnValues(ReturnValue.ALL_OLD);
        return table.deleteItem(deleteItemSpec).getItem() == null;
    }

    public Boolean updateReview(String id, Review updatedReview) {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey(new PrimaryKey("id", id))
            .withUpdateExpression("set username = :u, reviewDescription = :c, rating = :r")
            .withValueMap(new ValueMap()
                .withString(":u", updatedReview.getUserName())
                .withString(":c", updatedReview.getReviewDescription())
                .withString(":r", updatedReview.getRating()))
            .withReturnValues(ReturnValue.ALL_NEW);
        return table.updateItem(updateItemSpec).getItem() != null;
    }

}
