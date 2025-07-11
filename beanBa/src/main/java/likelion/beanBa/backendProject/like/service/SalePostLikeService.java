package likelion.beanBa.backendProject.like.service;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.mypage.dto.MyPagePostResponse;

import java.util.List;

public interface SalePostLikeService {

    void getLikePost(Member member, Long postPk);
    void unlikePost(Member member, Long postPk);
    boolean isPostLiked(Member member, Long postPk);
    List<MyPagePostResponse> getAllLikedPosts(Member member);

}
