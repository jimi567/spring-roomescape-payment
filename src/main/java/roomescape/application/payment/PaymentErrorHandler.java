package roomescape.application.payment;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import roomescape.application.payment.dto.response.ErrorResponse;
import roomescape.exception.payment.PaymentException;

public class PaymentErrorHandler implements ResponseErrorHandler {
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        ErrorResponse errorResponse = mapper.readValue(response.getBody(), ErrorResponse.class);
        throw new PaymentException(errorResponse.message());
    }
}
