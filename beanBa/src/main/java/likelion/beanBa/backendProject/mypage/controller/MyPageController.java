package likelion.beanBa.backendProject.mypage.controller;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import likelion.beanBa.backendProject.mypage.service.MyPageService;
import likelion.beanBa.backendProject.product.dto.PageResponse;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    /** 내가 판매한 글 조회 **/
    @GetMapping("/sales")
    public ResponseEntity<PageResponse<SalePostSummaryResponse>> getMySalePosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @CurrentUser CustomUserDetails mySaleUserDetails) {


        Member loginMember = mySaleUserDetails.getMember();
        PageResponse<SalePostSummaryResponse> mySalePosts = myPageService.getMySalePosts(loginMember, page, size);
        return ResponseEntity.ok(mySalePosts);
    }

    /** 내가 구매한 글 조회 **/
    @GetMapping("/purchases")
    public ResponseEntity<PageResponse<SalePostSummaryResponse>> getMyPurchasedPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @CurrentUser CustomUserDetails myPurchaseUserDetails) {

        Member loginMember = myPurchaseUserDetails.getMember();
        PageResponse<SalePostSummaryResponse> myPurchasedPosts = myPageService.getMyPurchasedPosts(loginMember, page, size);
        return ResponseEntity.ok(myPurchasedPosts);
    }
}
