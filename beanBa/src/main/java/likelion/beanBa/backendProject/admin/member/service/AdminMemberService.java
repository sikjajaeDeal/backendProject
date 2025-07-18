package likelion.beanBa.backendProject.admin.member.service;

import likelion.beanBa.backendProject.member.dto.AdminMemberDTO;
import likelion.beanBa.backendProject.product.dto.PageResponse;

import java.util.List;

public interface AdminMemberService {

    PageResponse<AdminMemberDTO> getAllMembers(int page, int size);

    PageResponse<AdminMemberDTO> memberSearchId(String key, int page, int size);

    PageResponse<AdminMemberDTO>memberSearchNickName(String nickName, int page, int size);

    PageResponse<AdminMemberDTO>memberSearchEmail(String email, int page, int size);

    void deleteMembersAdmin(List<Long> memberPkList);

    void updateMembersAdmin(List<AdminMemberDTO> memberPkList);


}
