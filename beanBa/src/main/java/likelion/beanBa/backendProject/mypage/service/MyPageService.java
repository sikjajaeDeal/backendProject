package likelion.beanBa.backendProject.mypage.service;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;

import java.util.List;

public interface MyPageService {

    List<SalePostSummaryResponse> getMySalePosts(Member loginMember);

    List<SalePostSummaryResponse> getMyPurchasedPosts(Member loginMember);
}
