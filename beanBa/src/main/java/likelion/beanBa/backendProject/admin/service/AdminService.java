package likelion.beanBa.backendProject.admin.service;

import likelion.beanBa.backendProject.member.dto.MemberResponse;
import likelion.beanBa.backendProject.product.dto.PageResponse;

public interface AdminService {

    PageResponse<MemberResponse> getAllMembers(int page, int size);

    /*PageResponse<MemberResponse> getMembers(,int page, int size)*/


}
