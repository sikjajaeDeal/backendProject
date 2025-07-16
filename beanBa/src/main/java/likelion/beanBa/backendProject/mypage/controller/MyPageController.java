package likelion.beanBa.backendProject.mypage.controller;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import likelion.beanBa.backendProject.mypage.service.MyPageService;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    /** 내가 판매한 글 조회 **/
    @GetMapping("/sales")
    public ResponseEntity<List<SalePostSummaryResponse>> getMySalePosts(
            @CurrentUser CustomUserDetails mySaleUserDetails) {


        Member loginMember = mySaleUserDetails.getMember();
        List<SalePostSummaryResponse> mySalePosts = myPageService.getMySalePosts(loginMember);
        return ResponseEntity.ok(mySalePosts);
    }

    /** 내가 구매한 글 조회 **/
    @GetMapping("/purchases")
    public ResponseEntity<List<SalePostSummaryResponse>> getMyPurchasedPosts(
            @CurrentUser CustomUserDetails myPurchaseUserDetails) {

        Member loginMember = myPurchaseUserDetails.getMember();
        List<SalePostSummaryResponse> myPurchasedPosts = myPageService.getMyPurchasedPosts(loginMember);
        return ResponseEntity.ok(myPurchasedPosts);
    }
}
