package likelion.beanBa.backendProject.mypage.service;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.dto.PageResponse;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;

import java.util.List;

public interface MyPageService {

    PageResponse<SalePostSummaryResponse> getMySalePosts(Member loginMember, int page, int size);

    PageResponse<SalePostSummaryResponse> getMyPurchasedPosts(Member loginMember, int page, int size);
}
