package likelion.beanBa.backendProject.product.controller;

import jakarta.validation.Valid;
import likelion.beanBa.backendProject.global.util.InputValidator;
import likelion.beanBa.backendProject.global.util.FileValidator;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import likelion.beanBa.backendProject.product.S3.service.S3Service;
import likelion.beanBa.backendProject.product.dto.PageResponse;
import likelion.beanBa.backendProject.product.dto.SalePostRequest;
import likelion.beanBa.backendProject.product.dto.SalePostDetailResponse;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import likelion.beanBa.backendProject.product.elasticsearch.service.SalePostEsService;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.product_enum.SaleStatement;
import likelion.beanBa.backendProject.product.service.SalePostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static likelion.beanBa.backendProject.global.util.AuthUtils.getAuthenticatedMember;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sale-post")
public class SalePostController {

    private final SalePostService salePostService;
    private final S3Service s3Service;


    /** 게시글 등록 (S3 이미지 업로드 포함) **/
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SalePostDetailResponse> createPost(
            @RequestPart("salePostRequest") @Valid SalePostRequest salePostRequest,
            @RequestPart("salePostImages") MultipartFile[] salePostImages,
            @CurrentUser CustomUserDetails userDetails) throws IOException {

//        log.info("생성 요청 시작");

        InputValidator.validateHopePrice(salePostRequest.getHopePrice());

        List<String> fullImageUrls = salePostRequest.getImageUrls(); // 슬롯 순서 유지
        if (fullImageUrls == null) fullImageUrls = new ArrayList<>(List.of("", "", "", "")); // null 방지

        List<String> newImageUrls = new ArrayList<>();

        if (salePostImages != null) {
            List<MultipartFile> validFiles = Arrays.stream(salePostImages)
                    .filter(f -> f != null && !f.isEmpty())
                    .collect(Collectors.toList());

            FileValidator.validateImageFiles(validFiles.toArray(new MultipartFile[0]), 4); // 실제 유효 파일 기준 검증

            if (!validFiles.isEmpty()) {
                newImageUrls = s3Service.uploadFiles(validFiles.toArray(new MultipartFile[0]));

                // 새 이미지 URL을 빈 슬롯(null or "")에 순서대로 채워넣기
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

        salePostRequest.setImageUrls(fullImageUrls); // 최종 슬롯 순서 반영

        Member loginMember = getAuthenticatedMember(userDetails);
        SalePost salePost = salePostService.createPost(salePostRequest, loginMember);

        return ResponseEntity.ok(SalePostDetailResponse.from(salePost, fullImageUrls, false, 0));
    }


    /** 전체 게시글 조회 **/
    @GetMapping("/all")
    public ResponseEntity<PageResponse<SalePostSummaryResponse>> getAllPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @CurrentUser CustomUserDetails userDetails) {

        Member loginMember = userDetails != null ? userDetails.getMember() : null;
        PageResponse<SalePostSummaryResponse> salePosts = salePostService.getAllPosts(loginMember, page, size);
        return ResponseEntity.ok(salePosts);
    }


    /** 게시글 단건 조회 **/
    @GetMapping("/detail/{postPk}")
    public ResponseEntity<SalePostDetailResponse> getPost(@PathVariable("postPk") Long postPk,
                                                          @CurrentUser CustomUserDetails userDetails) {

        Member loginMember = userDetails != null ? userDetails.getMember() : null;
        SalePostDetailResponse salePost = salePostService.getPost(postPk, loginMember);
        return ResponseEntity.ok(salePost);
    }


    /** 게시글 수정 (S3 이미지 업로드 포함) **/
    @PutMapping(value = "/{postPk}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePost(
            @PathVariable("postPk") Long postPk,
            @RequestPart("salePostRequest") @Valid SalePostRequest salePostRequest,
            @RequestPart(value = "salePostImages", required = false) MultipartFile[] salePostImages,
            @CurrentUser CustomUserDetails userDetails) throws IOException {

        InputValidator.validateHopePrice(salePostRequest.getHopePrice()); // ✅ 희망 가격 검증 추가

        List<String> fullImageUrls = salePostRequest.getImageUrls(); // 슬롯 순서 유지
        List<String> newImageUrls = new ArrayList<>();

        // 이미지 존재 여부 검증 (기존 + 새 이미지 모두 없음 → 에러)
        boolean noExistingImages = fullImageUrls == null || fullImageUrls.stream().allMatch(url -> url == null || url.isBlank());
        boolean noNewImages = salePostImages == null || Arrays.stream(salePostImages).allMatch(f -> f == null || f.isEmpty());

        if (noExistingImages && noNewImages) {
            throw new IllegalArgumentException("최소 1장의 이미지를 등록해야 합니다.");
        }

        //에러 검증 이후 기본 로직 진행
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


        salePostRequest.setImageUrls(fullImageUrls); //최종 슬롯 순서 반영

        Member loginMember = getAuthenticatedMember(userDetails);
        salePostService.updatePost(postPk, salePostRequest, loginMember);
        return ResponseEntity.ok().build();
    }


    /** 게시글 삭제 （테스트 완） **/
    @DeleteMapping("/{postPk}")
    public ResponseEntity<Void> deletePost(@PathVariable("postPk") Long postPk,
                                           @CurrentUser CustomUserDetails userDetails) {

        Member loginMember = getAuthenticatedMember(userDetails);
        salePostService.deletePost(postPk, loginMember);
        return ResponseEntity.ok().build();
    }

    /** 판매 상태 변경 시 **/
    @PutMapping("/{postPk}/status")
    public ResponseEntity<?> changeSaleStatus(
            @PathVariable ("postPk") Long postPk,
            @RequestParam("status") SaleStatement status,
            @RequestParam(value = "buyerPk", required = false) Long buyerPk,
            @CurrentUser CustomUserDetails userDetails
    ) {

        Member loginMember = getAuthenticatedMember(userDetails);

        String changeStatusMessage = salePostService.changeSaleStatus(postPk, status, buyerPk, loginMember);
        return ResponseEntity.ok(Map.of("message", changeStatusMessage));
    }
}