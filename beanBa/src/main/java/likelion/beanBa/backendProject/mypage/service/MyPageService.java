package likelion.beanBa.backendProject.mypage.service;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.mypage.dto.MyPagePostResponse;

import java.util.List;

public interface MyPageService {

    List<MyPagePostResponse> getMySalePosts(Member loginMember);

    List<MyPagePostResponse> getMyPurchasedPosts(Member loginMember);
}
