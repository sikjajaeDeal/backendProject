package likelion.beanBa.backendProject.product.controller;

import jakarta.validation.Valid;
import likelion.beanBa.backendProject.global.util.FileValidator;
import likelion.beanBa.backendProject.global.util.InputValidator;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.S3.service.S3Service;
import likelion.beanBa.backendProject.product.dto.SalePostRequest;
import likelion.beanBa.backendProject.product.dto.SalePostDetailResponse;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.service.SalePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 🚧 테스트 전용 컨트롤러
 * - 인증/인가 무시
 * - 하드코딩된 Member(멤버 PK = 1, nickname = test_user) 사용
 * - S3Service‧SalePostService 로직은 그대로 재사용
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test-sale-post")
public class TestSalePostController {

    private final SalePostService salePostService;
    private final S3Service s3Service;

    /** 하드코딩된 테스트 계정 */
    private final Member testMember = Member.builder()
            .memberPk(1L)
            .nickname("test_user")
            .build();

    /* ---------- 게시글 등록 ---------- */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SalePostDetailResponse> createPost(
            @RequestPart("salePostRequest") @Valid SalePostRequest salePostRequest,
            @RequestPart(value = "salePostImages") MultipartFile[] salePostImages) throws IOException {

        System.out.println("✅ POST 요청 도착"); // 여기에 로그

        FileValidator.validateImageFiles(salePostImages, 4); // ✅ 이미지 수 검증 추가
        InputValidator.validateHopePrice(salePostRequest.getHopePrice()); // ✅ 희망 가격 검증 추가

        List<String> imageUrls = s3Service.uploadFiles(salePostImages);
        salePostRequest.setImageUrls(imageUrls);

        SalePost saved = salePostService.createPost(salePostRequest, testMember);
        return ResponseEntity.ok(SalePostDetailResponse.from(saved, imageUrls, false, 0));
    }

    /* ---------- 게시글 전체 조회 ---------- */
    @GetMapping
    public ResponseEntity<List<SalePostSummaryResponse>> getAllPosts() {
        return ResponseEntity.ok(salePostService.getAllPosts(testMember));
    }

    /* ---------- 게시글 단건 조회 ---------- */
    @GetMapping("/{postPk}")
    public ResponseEntity<SalePostDetailResponse> getPost(@PathVariable("postPk") Long postPk) {
        return ResponseEntity.ok(salePostService.getPost(postPk, testMember));
    }

    /* ---------- 게시글 수정 ---------- */
    @PutMapping(value = "/{postPk}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePost(
            @PathVariable("postPk") Long postPk,
            @RequestPart("salePostRequest") @Valid SalePostRequest salePostRequest,
            @RequestPart(value = "salePostImages", required = false) MultipartFile[] salePostImages) throws IOException {

        InputValidator.validateHopePrice(salePostRequest.getHopePrice()); // ✅ 희망 가격 검증 추가

        List<String> fullImageUrls = salePostRequest.getImageUrls(); // 슬롯 순서 유지
        List<String> newImageUrls = new ArrayList<>();

        if (salePostImages != null) {
            List<MultipartFile> validFiles = Arrays.stream(salePostImages)
                    .filter(f -> f != null && !f.isEmpty())
                    .collect(Collectors.toList());

            if (!validFiles.isEmpty()) {
                newImageUrls = s3Service.uploadFiles(validFiles.toArray(new MultipartFile[0]));

                // 새 이미지 URL 을 빈 슬롯(null or "")에 순서대로 채워넣기
                int newImageIndex = 0;
                for (int i = 0; i < fullImageUrls.size(); i++) {
                    String url = fullImageUrls.get(i);
                    if ((url == null || url.isBlank()) && newImageIndex < newImageUrls.size()) {
                        fullImageUrls.set(i, newImageUrls.get(newImageIndex++));
                    }
                }

                if (newImageIndex < newImageUrls.size()) {
                    throw new IllegalArgumentException("빈 이미지 슬롯보다 업로드한 이미지 수가 더 많습니다.");
                }
            }
        }

//        List<String> imageUrls = s3Service.uploadFiles(salePostImages);
        salePostRequest.setImageUrls(fullImageUrls); //최종 슬롯 순서 반영

        salePostService.updatePost(postPk, salePostRequest, testMember);
        return ResponseEntity.ok().build();
    }

    /* ---------- 게시글 삭제 ---------- */
    @DeleteMapping("/{postPk}")
    public ResponseEntity<Void> deletePost(@PathVariable("postPk") Long postPk) {
        salePostService.deletePost(postPk, testMember);
        return ResponseEntity.ok().build();
    }
}