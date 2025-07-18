package likelion.beanBa.backendProject.product.kamis.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import likelion.beanBa.backendProject.product.kamis.KamisClient;
import likelion.beanBa.backendProject.product.kamis.dto.response.KamisCodeResponseDTO.info;
import likelion.beanBa.backendProject.product.kamis.dto.response.KamisSearchResponseDTO;
import likelion.beanBa.backendProject.product.kamis.entity.Kamis;
import likelion.beanBa.backendProject.product.kamis.repository.KamisRespotiroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KamisService {

  private final KamisClient kamisClient;
  private final KamisRespotiroy kamisRespotiroy;

  @Transactional
  @Scheduled(fixedRate = 86400000, initialDelay = 0)
  public void updateKamisData() throws Exception {

    System.out.println("Kamis 데이터 업데이트 시작");
    Long startTime = System.currentTimeMillis();



    // 0. Kamis db 정보를 모두 제거합니다.
    kamisRespotiroy.deleteAll();
    kamisRespotiroy.flush();

    log.info("[1] 기존 KAMIS 데이터 삭제 완료");

    // 1. Kamis API에서 상품 코드 목록을 가져옵니다.
    List<String> itemCodes = kamisClient.searchKamisCode().getInfo().stream()
        .map(info::getItemCode)
        .distinct()
        .toList();

    log.info("[2] 상품 코드 {} 개 조회 완료", itemCodes.size());


    // 2. 각 상품 코드에 대해 Kamis API에서 데이터를 가져오고 엔티티로 변환합니다.
    List<Kamis> responseKamisEntitys = itemCodes.stream()
          .map(code -> {

            try {
              KamisSearchResponseDTO kamisSearchResponseDTO = kamisClient.searchKamisDataByitemCode(code);
              return kamisSearchResponseDTO;
            } catch (Exception e) {
              log.error("searchKamisDataByitemCode : " + e.getMessage());
              return null;
            }
          }
        )
        .filter(Objects::nonNull)
        .map(Kamis::from)
        .toList();

    // 3. 변환된 엔티티를 데이터베이스에 저장합니다.
    kamisRespotiroy.saveAll(responseKamisEntitys);
    log.info("[3] KAMIS 데이터 {} 개 저장 완료", responseKamisEntitys.size());

    log.info("[4] KAMIS 데이터 업데이트 완료, 소요 시간: {} ms", System.currentTimeMillis() - startTime);

  }


}
