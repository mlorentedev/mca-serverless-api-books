package reviews;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import books.Book;
import books.BookRepository;

/**
 * Handler for requests to Lambda function.
 */
public class CreateReview implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ReviewRepository reviewRepository = new ReviewRepository();
    private final BookRepository bookRepository = new BookRepository();

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        LambdaLogger logger = context.getLogger();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {
            Review review = new ObjectMapper().readValue(input.getBody(), Review.class);
            review.setId(UUID.randomUUID().toString());
            logger.log("Review to create: " + review.toString());
            this.reviewRepository.createReview(review);
            logger.log("Review created");
            Book book = this.bookRepository.getBookById(review.getBookId());
            logger.log("Book to update: " + book.toString());
            book.addReview(review);
            logger.log(review.toString() + " added to " + book.toString());
            this.bookRepository.updateBook(review.getBookId(),book);
            return response
                .withBody("{message: Review created successfully }")
                .withStatusCode(201);
        } catch (JsonProcessingException e) {
            return response
                    .withBody("{error: " + e.getMessage() + "}")
                    .withStatusCode(500);
        }
    }
}
