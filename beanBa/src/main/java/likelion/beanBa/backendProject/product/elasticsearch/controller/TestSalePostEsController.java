package likelion.beanBa.backendProject.product.elasticsearch.controller;


import java.util.List;
import likelion.beanBa.backendProject.product.elasticsearch.dto.ElasticInsertRequestDTO;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsDocument;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SearchRequestDTO;
import likelion.beanBa.backendProject.product.elasticsearch.repository.SalePostEsRepository;
import likelion.beanBa.backendProject.product.elasticsearch.service.SalePostEsService;
import likelion.beanBa.backendProject.product.elasticsearch.service.SalePostEsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test-sale-post")
public class TestSalePostEsController {

    private final SalePostEsService salePostEsService;
    private final SalePostEsRepository salePostEsRepository;

    //private final KafkaTemplate<String, SearchLogMessage> kafkaTemplate;
//    @PostMapping("/elasticsearch")
//    //엘라스틱서치 검색 결과를 page 형태로 감싼 다음 HTTP 응답을 json으로 반환
//    public ResponseEntity<Page<SalePostEsDocument>> elasticSearch(
//            @RequestBody SearchRequestDTO searchRequestDTO) {
//
//
//        //검색어 정보 카프카 전송(0701(카프카랑 연결))
//        String userId = "1";
//        String searchedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
//
//        //SearchLogMessage message = new SearchLogMessage(keyword, userId, searchedAt);
//        //kafkaTemplate.send("search-log", message); //search-log 토픽으로 메세지 전달
//
//
//        return ResponseEntity.ok(salePostEsService.search(searchRequestDTO));
//    }

    //엘라스틱서치에 SalePostEsDocument를 저장하는 API
    @PostMapping("/elasticsearch/insert")
    public ResponseEntity<?> insertSalePostEsDocument(
        @RequestBody List<ElasticInsertRequestDTO> requestDtoList) {
        List<SalePostEsDocument> salePostEsDocumentList = requestDtoList.stream()
                .map(requestDTO -> SalePostEsDocument.builder()
                    .postPk(requestDTO.getPostPk())
                    .sellerId(requestDTO.getSellerId())
                    .buyerId(requestDTO.getBuyerId())
                    .title(requestDTO.getTitle())
                    .content(requestDTO.getContent())
                    .hopePrice(requestDTO.getHopePrice())
                    .categoryPk(requestDTO.getCategoryPk())
                    .deleteYn(requestDTO.getDeleteYn())
                    .geoLocation(new GeoPoint(requestDTO.getLatitude(), requestDTO.getLongitude()))
                    .build()).toList();
        salePostEsRepository.saveAll(salePostEsDocumentList);
        return ResponseEntity.ok("SalePostEsDocuments inserted successfully");
    }

}
