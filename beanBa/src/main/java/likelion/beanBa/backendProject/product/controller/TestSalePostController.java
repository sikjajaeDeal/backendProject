package likelion.beanBa.backendProject.product.controller;

import jakarta.validation.Valid;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
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
@RequestMapping("/api/test-sale-post")
public class TestSalePostController {

    private final SalePostService salePostService;
    private final MemberRepository memberRepository;

    // 테스트용 로그인 멤버 (DB에 1번 회원이 있다고 가정)
    private Member getTestMember() {
        return memberRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("ID가 1인 테스트 멤버가 존재하지 않습니다."));
    }

    /**
     * 게시글 등록 (테스트용)
     */
    @PostMapping
    public ResponseEntity<SalePostResponse> createPost(@RequestBody @Valid SalePostRequest request) {
        Member loginMember = getTestMember();
        SalePost salePost = salePostService.createPost(request, loginMember);
        return ResponseEntity.ok(SalePostResponse.from(salePost, request.getImageUrls()));
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
     * 게시글 수정 (테스트용)
     */
    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId,
                                           @RequestBody @Valid SalePostRequest request) {
        Member loginMember = getTestMember();
        salePostService.updatePost(postId, request, loginMember);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 삭제 (테스트용)
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        Member loginMember = getTestMember();
        salePostService.deletePost(postId, loginMember);
        return ResponseEntity.ok().build();
    }
}