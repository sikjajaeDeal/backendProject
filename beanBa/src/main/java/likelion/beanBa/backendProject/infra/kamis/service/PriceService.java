package likelion.beanBa.backendProject.infra.kamis.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import likelion.beanBa.backendProject.infra.kamis.dto.KamisPriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j

@Service
@RequiredArgsConstructor
public class PriceService {
    private final WebClient webClient;

    @Value("${kamis.api.key}")
    private String apiKey;

    @Value("${kamis.api.cert.id}")
    private String certId;

    public Mono<List<KamisPriceResponse.Item>> getPriceList(String itemCode) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6); // 7일 전부터 오늘까지

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDay = startDate.format(formatter);
        String endDay = endDate.format(formatter);

        log.info("Requesting price list from {} to {}", startDay, endDay);
        log.info("Requesting itemCode: {}", itemCode);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/service/price/xml.do")
                        .queryParam("action", "periodProductList")
                        .queryParam("p_cert_key", apiKey)
                        .queryParam("p_cert_id", certId)
                        .queryParam("p_returntype", "json")
                        .queryParam("p_startday", startDay)
                        .queryParam("p_endday", endDay)
                        .queryParam("p_productclscode", "01")
                        // .queryParam("p_itemcategorycode", "200")
                        .queryParam("p_itemcode", itemCode)
                        .build())
                .retrieve()
                .bodyToMono(String.class) // Raw String으로 먼저 받아서 로깅
                // .doOnNext(responseBody -> log.info("Raw API Response: {}", responseBody))
                .map(responseBody -> {
                    try {
                        // String을 KamisPriceResponse 객체로 변환
                        return new com.fasterxml.jackson.databind.ObjectMapper().readValue(responseBody, KamisPriceResponse.class);
                    } catch (Exception e) {
                        log.error("Error parsing KamisPriceResponse: {}", e.getMessage(), e);
                        throw new RuntimeException("Failed to parse API response", e);
                    }
                })
                .map(response -> Optional.ofNullable(response.getData())
                        .map(KamisPriceResponse.DataBlock::getItem)
                        .orElse(Collections.emptyList()))
                .doOnError(e -> {
                    log.error("Error fetching price list: {}", e.getMessage(), e);
                    e.printStackTrace();
                });
    }
}
