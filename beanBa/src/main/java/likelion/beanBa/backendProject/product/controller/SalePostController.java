package likelion.beanBa.backendProject.product.controller;

import jakarta.validation.Valid;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import likelion.beanBa.backendProject.product.dto.SalePostRequest;
import likelion.beanBa.backendProject.product.dto.SalePostResponse;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.service.SalePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sale-post")
public class SalePostController {

    private final SalePostService salePostService;

    /**
     * 게시글 등록 （테스트 완）
     */
    @PostMapping
    public ResponseEntity<SalePostResponse> createPost(@RequestBody @Valid SalePostRequest salePostRequest,
                                                       @CurrentUser CustomUserDetails saleUserDetails) {

//        Member loginMember = Member.builder()
//                .memberPk(1L)
//                .nickname("test_user")
//                .build();
//        String memberId = saleUserDetails.getUsername();
        Member loginMember = saleUserDetails.getMember();
        System.out.println("=======================로그인 PK: " + loginMember);
        SalePost salePost = salePostService.createPost(salePostRequest, loginMember);
        List<String> imageUrls = salePostRequest.getImageUrls();
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
     * 게시글 수정
     */
    @PutMapping("/{postPk}")
    public ResponseEntity<?> updatePost(@PathVariable Long postPk,
                                        @RequestBody @Valid SalePostRequest salePostRequest,
                                        @CurrentUser CustomUserDetails saleUserDetails) {

        List<String> imageUrls = salePostRequest.getImageUrls();

        if (imageUrls == null || imageUrls.isEmpty() || imageUrls.size() > 4) {
            return ResponseEntity
                    .badRequest()
                    .body("이미지는 1개 이상 4개 이하로 등록해야 합니다.");
        }

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