package books;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handler for requests to Lambda function.
 */
public class DeleteBook implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final BookRepository bookRepository = new BookRepository();

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
                
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        try {
            if (bookRepository.deleteBook(input.getPathParameters().get("id"))) {
                return response
                        .withBody("{message: Book deleted}")
                        .withStatusCode(200);
            }
            return response
                        .withBody("{error: Book not found}")
                        .withStatusCode(404);
        } catch (Exception e) {
            return response
                    .withBody("{error: " + e.getMessage() + "}")
                    .withStatusCode(500);
        }
    }
}
