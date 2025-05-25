package com.money.app.external;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.money.app.module.tx.dto.DecisionResponseDto;
import com.money.app.module.tx.dto.InputSchema;
import com.money.app.util.exception.CustomException;
import com.money.app.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@Component
@RequiredArgsConstructor
public class FastApiConnect {

    private final RestTemplate fastApiRestTemplate;

    public DecisionResponseDto getAbcDecision(InputSchema input) {
        String fastApiUrl = "http://moneyplanet-fastapi-server:8000/v1/decision/abc";

        try {
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String jsonBody = mapper.writeValueAsString(input);
            System.out.println("실제 전송 JSON:\n" + jsonBody);

            ResponseEntity<DecisionResponseDto> response = fastApiRestTemplate.postForEntity(
                    fastApiUrl, input, DecisionResponseDto.class
            );
            return response.getBody();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.FASTAPI_SERVER_ERROR);
        } catch (RestClientException e) {
            throw new CustomException(ErrorCode.FASTAPI_SERVER_ERROR);
        }
    }
}
