package com.money.app.external;

import com.money.app.module.tx.dto.DecisionResponseDto;
import com.money.app.module.tx.dto.InputSchema;
import com.money.app.util.exception.CustomException;
import com.money.app.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class FastApiConnect {

    // 이름 일치시킴
    private final RestTemplate fastApiRestTemplate;

    public DecisionResponseDto getAbcDecision(InputSchema input) {
        String fastApiUrl = "http://fastapi:8000/decide";

        try {
            ResponseEntity<DecisionResponseDto> response = fastApiRestTemplate.postForEntity(
                    fastApiUrl, input, DecisionResponseDto.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            throw new CustomException(ErrorCode.FASTAPI_SERVER_ERROR);
        }
    }
}
