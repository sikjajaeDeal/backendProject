package likelion.beanBa.backendProject.like.controller;


import jakarta.persistence.Column;
import likelion.beanBa.backendProject.like.service.SalePostLikeService;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import likelion.beanBa.backendProject.mypage.dto.MyPagePostResponse;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class SalePostLikeController {

    private final SalePostLikeService salePostLikeService;

    /** 판매글 찜 등록 **/
    @PostMapping("/{postPk}")
    public ResponseEntity<Void> likePost (@PathVariable("postPk") Long postPk,
                                          @CurrentUser CustomUserDetails userDetails) {

        salePostLikeService.likePost(userDetails.getMember(), postPk);
        return ResponseEntity.ok().build();
    }


    /** 판매글 찜 해제 **/
    @DeleteMapping("/{postPk}")
    public ResponseEntity<Void> unlikePost(@PathVariable("postPk") Long postPk,
                                           @CurrentUser CustomUserDetails userDetails) {

        salePostLikeService.unlikePost(userDetails.getMember(), postPk);
        return ResponseEntity.ok().build();
    }


    /** 해당 게시글 찜 여부 확인 **/
    @GetMapping("/{postPk}")
    public ResponseEntity<Boolean> isPostLiked(@PathVariable("postPk") Long postPk,
                                               @CurrentUser CustomUserDetails userDetails) {

        boolean saleLiked = salePostLikeService.isPostLiked(userDetails.getMember(), postPk);
        return ResponseEntity.ok(saleLiked);
    }


    /** 마이페이지 - 내가 찜한 게시글 목록 조회 **/
    @GetMapping("/mypage")
    public ResponseEntity<List<SalePostSummaryResponse>> getMyLikedPosts(@CurrentUser CustomUserDetails userDetails) {

        List<SalePostSummaryResponse> saleLikedPosts = salePostLikeService.getAllLikedPosts(userDetails.getMember());
        return ResponseEntity.ok(saleLikedPosts);
    }



}
