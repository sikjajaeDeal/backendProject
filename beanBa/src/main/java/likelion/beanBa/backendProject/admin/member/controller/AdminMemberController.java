package likelion.beanBa.backendProject.admin.member.controller;

import likelion.beanBa.backendProject.admin.member.service.AdminMemberService;
import likelion.beanBa.backendProject.member.dto.AdminMemberDTO;
import likelion.beanBa.backendProject.member.service.MemberService;
import likelion.beanBa.backendProject.product.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminMemberController {

    private final AdminMemberService adminService;
    private final MemberService memberService;

    /**사용자 전체 조회**/
    @GetMapping("/member")
    public ResponseEntity<PageResponse<AdminMemberDTO>> getAllMembers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
            ) {
        System.out.println("🔥 Controller 도착함");
        PageResponse<AdminMemberDTO> response = adminService.getAllMembers(page, size);
        System.out.println("🔥 전체 멤버 수: " + response.getTotalElements());
        System.out.println("📄 현재 페이지 멤버 수: " + response.getContent().size());
        return ResponseEntity.ok(response);
    }

    /**특정 멤버 검색
     category(memberId, email, nickName) 구분으로 검색
     **/

    @GetMapping("/member/search")
    public ResponseEntity<PageResponse<AdminMemberDTO>> memberSearch(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value= "category", defaultValue = "memberId") String category,
            @RequestParam String keyword
    ){
        PageResponse<AdminMemberDTO> response;
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

    /** 다중 선택 삭제 **/

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteMembersAdmin(@RequestBody List<Long> memberPkList){

        System.out.println("🔥 deleteMembersAdmin 도착함");
        adminService.deleteMembersAdmin(memberPkList);

        System.out.println("🔥 deleteMembersAdmin 삭제 완");

        return ResponseEntity.ok().build();
    }


    /** 멤버 다중 수정 **/

    @PostMapping("/update")
    public ResponseEntity<Void> updateMembersAdmin(@RequestBody List<AdminMemberDTO> adminMemberDTO){
        System.out.println("🔥 updateMembersAdmin 도착함");
        adminService.updateMembersAdmin(adminMemberDTO);

        System.out.println("🔥 updateMembersAdmin 수정 완");

        return ResponseEntity.ok().build();
    }



}
