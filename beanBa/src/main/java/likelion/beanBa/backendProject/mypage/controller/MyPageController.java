package likelion.beanBa.backendProject.mypage.controller;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import likelion.beanBa.backendProject.mypage.dto.MyPagePostResponse;
import likelion.beanBa.backendProject.mypage.service.MyPageService;
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
    public ResponseEntity<List<MyPagePostResponse>> getMySalePosts(
            @CurrentUser CustomUserDetails mySaleUserDetails) {


        Member loginMember = mySaleUserDetails.getMember();
        List<MyPagePostResponse> responses = myPageService.getMySalePosts(loginMember);
        return ResponseEntity.ok(responses);
    }

    /** 내가 구매한 글 조회 **/
    @GetMapping("/purchases")
    public ResponseEntity<List<MyPagePostResponse>> getMyPurchasedPosts(
            @CurrentUser CustomUserDetails myPurchaseUserDetails) {

        Member loginMember = myPurchaseUserDetails.getMember();
        List<MyPagePostResponse> responses = myPageService.getMyPurchasedPosts(loginMember);
        return ResponseEntity.ok(responses);
    }
}
