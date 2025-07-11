package likelion.beanBa.backendProject.mypage.controller;

import likelion.beanBa.backendProject.member.Entity.Member;
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
@RequestMapping("/api/test-mypage")
public class MyPageTestController {

    private final MyPageService myPageService;

    /** 하드코딩된 테스트 계정 */
    private final Member testMember = Member.builder()
            .memberPk(1L)
            .nickname("test_user")
            .build();


    /** 내가 판매한 글 조회 (테스트용) **/
    @GetMapping("/sales")
    public ResponseEntity<List<MyPagePostResponse>> getMySalePostsTest() {
        List<MyPagePostResponse> responses = myPageService.getMySalePosts(testMember);
        return ResponseEntity.ok(responses);
    }

    /** 내가 구매한 글 조회 (테스트용) **/
    @GetMapping("/purchases")
    public ResponseEntity<List<MyPagePostResponse>> getMyPurchasedPostsTest() {
        List<MyPagePostResponse> responses = myPageService.getMyPurchasedPosts(testMember);
        return ResponseEntity.ok(responses);
    }
}
