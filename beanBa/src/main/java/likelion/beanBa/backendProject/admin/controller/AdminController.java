package likelion.beanBa.backendProject.admin.controller;

import likelion.beanBa.backendProject.admin.service.AdminService;
import likelion.beanBa.backendProject.member.dto.MemberResponse;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.product.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    /**사용자 전체 조회**/
    @GetMapping("/member")
    public ResponseEntity<PageResponse<MemberResponse>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        System.out.println("🔥 Controller 도착함");
        PageResponse<MemberResponse> response = adminService.getAllMembers(page, size);
        System.out.println("🔥 전체 멤버 수: " + response.getTotalElements());
        System.out.println("📄 현재 페이지 멤버 수: " + response.getContent().size());
        return ResponseEntity.ok(response);
    }

    /**특정 멤버 검색
     category(memberId, email, nickName) 구분으로 검색
     **/

    @GetMapping("/member/search")
    public ResponseEntity<PageResponse<MemberResponse>> memberSearch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "memberId") String category,
            @RequestParam String keyword
    ){
        PageResponse<MemberResponse> response;
        if(category.equals("email")){
            System.out.println("🔥 searchEmail 도착함");
            response = adminService.memberSearchEmail(keyword, page, size);

        }else if(category.equals("nickName")){
            System.out.println("🔥 searchNickname 도착함");
            response = adminService.memberSearchNickName(keyword, page, size);
        }else{
            System.out.println("🔥 memberId 도착함");
            response = adminService.memberSearchId(keyword, page, size);
        }

        return ResponseEntity.ok(response);

    }



}
