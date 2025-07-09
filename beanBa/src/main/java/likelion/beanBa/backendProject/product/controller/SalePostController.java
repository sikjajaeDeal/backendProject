package likelion.beanBa.backendProject.product.controller;

import jakarta.validation.Valid;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import likelion.beanBa.backendProject.product.S3.service.S3Service;
import likelion.beanBa.backendProject.product.dto.SalePostRequest;
import likelion.beanBa.backendProject.product.dto.SalePostResponse;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.service.SalePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sale-post")
public class SalePostController {

    private final SalePostService salePostService;
    private final S3Service s3Service;

    /**
     * 게시글 등록 （테스트 완）
     */
    /**
     * 게시글 등록 (S3 이미지 업로드 포함)
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SalePostResponse> createPost(
            @RequestPart("salePostRequest") @Valid SalePostRequest salePostRequest,
            @RequestPart("salePostImages") MultipartFile[] salePostImages,
            @CurrentUser CustomUserDetails saleUserDetails) throws IOException {

        if (salePostImages == null || salePostImages.length == 0 || salePostImages.length > 4) {
            return ResponseEntity.badRequest().body(null);
        }

        List<String> imageUrls = s3Service.uploadFiles(salePostImages);
        salePostRequest.setImageUrls(imageUrls);

        Member loginMember = saleUserDetails.getMember();
        SalePost salePost = salePostService.createPost(salePostRequest, loginMember);
        return ResponseEntity.ok(SalePostResponse.from(salePost, imageUrls));
    }

    /**
     * 전체 게시글 조회
     */
    @GetMapping
    public ResponseEntity<List<SalePostResponse>> getAllPosts() {
        List<SalePostResponse> salePosts = salePostService.getAllPosts();
        return ResponseEntity.ok(salePosts);
    }

    /**
     * 게시글 단건 조회
     */
    @GetMapping("/{postPk}")
    public ResponseEntity<SalePostResponse> getPost(@PathVariable Long postPk) {
        SalePostResponse response = salePostService.getPost(postPk);
        return ResponseEntity.ok(response);
    }


    /**
     * 게시글 수정 (S3 이미지 업로드 포함)
     */
    @PutMapping(value = "/{postPk}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePost(
            @PathVariable Long postPk,
            @RequestPart("salePostRequest") @Valid SalePostRequest salePostRequest,
            @RequestPart("salePostImages") MultipartFile[] salePostImages,
            @CurrentUser CustomUserDetails saleUserDetails) throws IOException {

        if (salePostImages == null || salePostImages.length == 0 || salePostImages.length > 4) {
            return ResponseEntity.badRequest().body("이미지는 1개 이상 4개 이하로 등록해야 합니다.");
        }

        List<String> imageUrls = s3Service.uploadFiles(salePostImages);
        salePostRequest.setImageUrls(imageUrls);

        Member loginMember = saleUserDetails.getMember();
        salePostService.updatePost(postPk, salePostRequest, loginMember);
        return ResponseEntity.ok().build();
    }


    /**
     * 게시글 삭제 （테스트 완）
     */
    @DeleteMapping("/{postPk}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postPk,
                                           @CurrentUser CustomUserDetails saleUserDetails) {

        Member loginMember = saleUserDetails.getMember();
        salePostService.deletePost(postPk, loginMember);
        return ResponseEntity.ok().build();
    }
}