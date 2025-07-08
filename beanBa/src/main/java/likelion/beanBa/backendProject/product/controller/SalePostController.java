package likelion.beanBa.backendProject.product.controller;

import jakarta.validation.Valid;
import likelion.beanBa.backendProject.member.Entity.Member;
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
     * 게시글 등록
     */
    @PostMapping
    public ResponseEntity<SalePostResponse> createPost(@RequestBody @Valid SalePostRequest request,
                                                       @RequestAttribute("loginMember") Member loginMember) {

//        Member loginMember = Member.builder()
//                .memberPk(1L)
//                .nickname("test_user")
//                .build();

        SalePost salePost = salePostService.createPost(request, loginMember);
        List<String> imageUrls = request.getImageUrls();
        return ResponseEntity.ok(SalePostResponse.from(salePost, imageUrls));
    }

    /**
     * 전체 게시글 조회
     */
    @GetMapping
    public ResponseEntity<List<SalePostResponse>> getAllPosts() {
        List<SalePostResponse> posts = salePostService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * 게시글 단건 조회
     */
    @GetMapping("/{postId}")
    public ResponseEntity<SalePostResponse> getPost(@PathVariable Long postId) {
        SalePostResponse response = salePostService.getPost(postId);
        return ResponseEntity.ok(response);
    }

    /**
     * 게시글 수정
     */
    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId,
                                           @RequestBody @Valid SalePostRequest request,
                                           @RequestAttribute("loginMember") Member loginMember) {
        salePostService.updatePost(postId, request, loginMember);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @RequestAttribute("loginMember") Member loginMember) {
        salePostService.deletePost(postId, loginMember);
        return ResponseEntity.ok().build();
    }
}