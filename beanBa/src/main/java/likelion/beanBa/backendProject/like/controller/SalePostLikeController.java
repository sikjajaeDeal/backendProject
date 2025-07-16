package likelion.beanBa.backendProject.like.controller;


import likelion.beanBa.backendProject.like.service.SalePostLikeService;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import likelion.beanBa.backendProject.product.dto.PageResponse;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static likelion.beanBa.backendProject.global.util.AuthUtils.getAuthenticatedMember;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class SalePostLikeController {

    private final SalePostLikeService salePostLikeService;

    /** 판매글 찜 등록 **/
    @PostMapping("/{postPk}")
    public ResponseEntity<Void> likePost (@PathVariable("postPk") Long postPk,
                                          @CurrentUser CustomUserDetails userDetails) {

        Member loginMember = getAuthenticatedMember(userDetails);
        salePostLikeService.likePost(loginMember, postPk);
        return ResponseEntity.ok().build();
    }


    /** 판매글 찜 해제 **/
    @DeleteMapping("/{postPk}")
    public ResponseEntity<Void> unlikePost(@PathVariable("postPk") Long postPk,
                                           @CurrentUser CustomUserDetails userDetails) {

        Member loginMember = getAuthenticatedMember(userDetails);
        salePostLikeService.unlikePost(loginMember, postPk);
        return ResponseEntity.ok().build();
    }


    /** 해당 게시글 찜 여부 확인 **/
    @GetMapping("/{postPk}")
    public ResponseEntity<Boolean> isPostLiked(@PathVariable("postPk") Long postPk,
                                               @CurrentUser CustomUserDetails userDetails) {

        Member loginMember = getAuthenticatedMember(userDetails);
        boolean saleLiked = salePostLikeService.isPostLiked(loginMember, postPk);
        return ResponseEntity.ok(saleLiked);
    }


    /** 마이페이지 - 내가 찜한 게시글 목록 조회 **/
    @GetMapping("/mypage")
    public ResponseEntity<PageResponse<SalePostSummaryResponse>> getMyLikedPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @CurrentUser CustomUserDetails userDetails) {

        Member loginMember = getAuthenticatedMember(userDetails);
        PageResponse<SalePostSummaryResponse> saleLikedPosts = salePostLikeService.getAllLikedPosts(loginMember, page, size);
        return ResponseEntity.ok(saleLikedPosts);
    }



}
