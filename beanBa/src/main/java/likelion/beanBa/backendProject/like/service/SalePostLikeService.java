package likelion.beanBa.backendProject.like.service;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.mypage.dto.MyPagePostResponse;

import java.util.List;

public interface SalePostLikeService {


    // 찜하기
    void LikePost(Member member, Long postPk);

    // 찜하기 취소
    void unlikePost(Member member, Long postPk);

    // 해당 판매글 찜여부 판별
    boolean isPostLiked(Member member, Long postPk);

    //좋아요 한 모든 목록 보기
    List<MyPagePostResponse> getAllLikedPosts(Member member);

}
