package likelion.beanBa.backendProject.product.kamis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.beanBa.backendProject.product.kamis.dto.response.KamisSearchResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;

class KamisClientTest {


  @Test
  void searchKamisDataByitemCode() throws JsonProcessingException {

    RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    ObjectMapper objectMapper = new ObjectMapper();

    //given
    String url = "http://www.kamis.or.kr/service/price/xml.do";
    String startDay = "2025-07-01";
    String endDay = "2025-07-08";

    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
        .queryParam("action", "periodProductList")
        .queryParam("p_productclscode", "01")
        .queryParam("p_startday", startDay)
        .queryParam("p_endday", endDay)
        .queryParam("p_itemcode", 658)
        .queryParam("p_productrankcode", "04")
        .queryParam("p_countrycode", "1101")
        .queryParam("p_convert_kg_yn", "Y")
        .queryParam("p_cert_key", "dbee1762-5543-4ee2-b51c-92035825bd7b")
        .queryParam("p_cert_id", "5945")
        .queryParam("p_returntype", "json");

    String rqUrl = builder.build().toUriString();
    System.out.println("rqUrl = " + rqUrl);


    //when
    String responseStr = restTemplate.getForObject(rqUrl, String.class);
    KamisSearchResponseDTO response = objectMapper.readValue(responseStr, KamisSearchResponseDTO.class);

    //then
    assertThat(response).isNotNull();

  }

}