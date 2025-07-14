package likelion.beanBa.backendProject.like.controller;

import likelion.beanBa.backendProject.like.service.SalePostLikeService;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test-like")
public class TestSalePostLikeController {

    private final SalePostLikeService salePostLikeService;

    /** 하드코딩된 테스트 계정 */
    private final Member testMember = Member.builder()
            .memberPk(1L)
            .nickname("test_user")
            .build();

    /** 판매글 찜 등록 */
    @PostMapping("/{postPk}")
    public ResponseEntity<Void> likePost(@PathVariable("postPk") Long postPk) {
        log.info("찜 등록 시작: memberPk={}, postPk={}", testMember.getMemberPk(), postPk);
        salePostLikeService.likePost(testMember, postPk);
        return ResponseEntity.ok().build();
    }

    /** 판매글 찜 해제 */
    @DeleteMapping("/{postPk}")
    public ResponseEntity<Void> unlikePost(@PathVariable("postPk") Long postPk) {
        salePostLikeService.unlikePost(testMember, postPk);
        return ResponseEntity.ok().build();
    }

    /** 해당 게시글 찜 여부 확인 */
    @GetMapping("/{postPk}")
    public ResponseEntity<Boolean> isPostLiked(@PathVariable("postPk") Long postPk) {
        boolean saleLiked = salePostLikeService.isPostLiked(testMember, postPk);
        return ResponseEntity.ok(saleLiked);
    }

    /** 내가 찜한 게시글 목록 조회 */
    @GetMapping("/mypage")
    public ResponseEntity<List<SalePostSummaryResponse>> getMyLikedPosts() {
        List<SalePostSummaryResponse> saleLikedPosts = salePostLikeService.getAllLikedPosts(testMember);
        return ResponseEntity.ok(saleLikedPosts);
    }
}
