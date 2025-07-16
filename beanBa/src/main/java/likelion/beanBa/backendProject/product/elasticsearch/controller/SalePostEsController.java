package likelion.beanBa.backendProject.product.elasticsearch.controller;


import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import likelion.beanBa.backendProject.product.dto.SalePostDetailResponse;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsDocument;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SearchRequestDTO;
import likelion.beanBa.backendProject.product.elasticsearch.service.SalePostEsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sale-post/elasticsearch")
public class SalePostEsController {

  private final SalePostEsService salePostEsService;

  @PostMapping
  public ResponseEntity<Page<SalePostSummaryResponse>> elasticSearch
      (@RequestBody SearchRequestDTO searchRequestDTO, @CurrentUser CustomUserDetails userDetails) {

    Member member = userDetails != null ? userDetails.getMember() : null;
    return ResponseEntity.ok(salePostEsService.search(searchRequestDTO, member));
  }

}
