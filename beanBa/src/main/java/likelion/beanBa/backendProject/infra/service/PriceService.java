package likelion.beanBa.backendProject.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import likelion.beanBa.backendProject.infra.dto.KamisPriceResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PriceService {
    private final WebClient webClient;

    @Value("${kamis.api.key}")
    private String apiKey;

    @Value("${kamis.api.cert.id}")
    private String certId;

    public Mono<List<KamisPriceResponse.Item>> getPriceList() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/service/price/xml.do?action=periodProductList")
                        .queryParam("p_cert_key", apiKey)
                        .queryParam("p_cert_id", certId)
                        .queryParam("p_returntype", "json")
                        .queryParam("p_startday", "2025-07-01")
                        .queryParam("p_endday", "2025-07-07")
                        .queryParam("p_productclscode", "01")
                        .queryParam("p_itemcategorycode", "200")
                        .build())
                .retrieve()
                .bodyToMono(KamisPriceResponse.class)
                .map(KamisPriceResponse::getPrice);
    }
}
