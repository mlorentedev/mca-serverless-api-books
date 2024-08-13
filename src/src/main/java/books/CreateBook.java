package books;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handler for requests to Lambda function.
 */
public class CreateBook implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final BookRepository bookRepository = new BookRepository();

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
                
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {
            Book book = new ObjectMapper().readValue(input.getBody(), Book.class);
            book.setId(UUID.randomUUID().toString());
            bookRepository.createBook(book);
            return response
                    .withBody("{message: Book created successfully }")
                    .withStatusCode(201);
        } catch (JsonProcessingException e) {
            return response
                    .withBody("{error: " + e.getMessage() + "}")
                    .withStatusCode(500);
        }
    }
}
