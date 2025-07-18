package likelion.beanBa.backendProject.product.kamis;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import likelion.beanBa.backendProject.product.kamis.dto.response.KamisCodeResponseDTO;
import likelion.beanBa.backendProject.product.kamis.dto.response.KamisSearchResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
@RequiredArgsConstructor
public class KamisClient {

  private final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private final ObjectMapper objectMapper = new ObjectMapper();


  public KamisSearchResponseDTO searchKamisDataByitemCode(String itemCode) throws Exception {
    String startDay = getWeekAgoAsString();
    String endDay = getTodayAsString();

    String url = "http://www.kamis.or.kr/service/price/xml.do";
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
        .queryParam("action", "periodProductList")
        .queryParam("p_productclscode", "01")
        .queryParam("p_startday", startDay)
        .queryParam("p_endday", endDay)
        .queryParam("p_itemcode", itemCode)
        .queryParam("p_productrankcode", "04")
        .queryParam("p_countrycode", "1101")
        .queryParam("p_convert_kg_yn", "Y")
        .queryParam("p_cert_key", "dbee1762-5543-4ee2-b51c-92035825bd7b")
        .queryParam("p_cert_id", "5945")
        .queryParam("p_returntype", "json");

    String rqUrl = builder.build().toUriString();

    try {
      String responseStr = restTemplate.getForObject(rqUrl, String.class);
      System.out.println("responseStrData = " + responseStr);

      //data 필드가 비어있는 배열 형태일 경우 null 반환
      if (responseStr.contains("\"data\":[")) {
        log.info("배열 형태 응답 감지 - 상품코드: {} 건너뜀", itemCode);
        return null; // 또는 빈 응답 객체 반환
      }
      KamisSearchResponseDTO response = objectMapper.readValue(responseStr, KamisSearchResponseDTO.class);
      return response;
    } catch (Exception e) {
      log.error("kamis API 호출 실패: {}", e.getMessage());
      throw new Exception("Kamis API 호출 실패: " + e.getMessage());
    }
  }

  public KamisCodeResponseDTO searchKamisCode() throws Exception {
    String url = "http://www.kamis.or.kr/service/price/xml.do?action=productInfo&p_returntype=json";

    try {
      String responseStr = restTemplate.getForObject(url, String.class);
      System.out.println("responseStrCode = " + responseStr);
      KamisCodeResponseDTO responseDTO = objectMapper.readValue(responseStr, KamisCodeResponseDTO.class);
      return responseDTO;
    } catch (Exception e) {
      log.error("kamis API 호출 실패: {}", e.getMessage());
      throw new Exception("Kamis API 호출 실패: " + e.getMessage());
    }

  }



  public String getTodayAsString() {
    return LocalDate.now().minusDays(3).format(DATE_FORMAT);
  }

  public String getWeekAgoAsString() {
    return LocalDate.now().minusDays(10).format(DATE_FORMAT);
  }
}
