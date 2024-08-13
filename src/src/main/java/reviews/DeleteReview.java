package reviews;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handler for requests to Lambda function.
 */
public class DeleteReview implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ReviewRepository reviewRepository = new ReviewRepository();

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
                
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        try {
            if (reviewRepository.deleteReview(input.getPathParameters().get("id"))) {
                return response
                        .withBody("{message: Review deleted}")
                        .withStatusCode(200);
            }
            return response
                        .withBody("{error: Review not found}")
                        .withStatusCode(404);
        } catch (Exception e) {
            return response
                    .withBody("{error: " + e.getMessage() + "}")
                    .withStatusCode(500);
        }
    }
}
