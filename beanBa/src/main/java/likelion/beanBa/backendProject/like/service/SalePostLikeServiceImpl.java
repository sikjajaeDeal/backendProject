package likelion.beanBa.backendProject.like.service;

import likelion.beanBa.backendProject.like.entity.SalePostLike;
import likelion.beanBa.backendProject.like.repository.SalePostLikeRepository;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.mypage.dto.MyPagePostResponse;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.entity.SalePostImage;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import likelion.beanBa.backendProject.product.repository.SalePostImageRepository;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalePostLikeServiceImpl implements SalePostLikeService {

    private final SalePostLikeRepository likeRepository;
    private final SalePostRepository salePostRepository;
    private final SalePostImageRepository salePostImageRepository;

    @Override
    @Transactional
    public void likePost(Member member, Long postPk) {
        SalePost post = salePostRepository.findById(postPk)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (likeRepository.existsByMemberPkAndPostPk(member, post)) {
            throw new IllegalStateException("이미 찜한 게시글입니다.");
        }

        likeRepository.save(SalePostLike.of(member, post));
    }

    @Override
    @Transactional
    public void unlikePost(Member member, Long postPk) {
        SalePost post = salePostRepository.findById(postPk)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        SalePostLike like = likeRepository.findByMemberPkAndPostPk(member, post)
                .orElseThrow(() -> new IllegalStateException("찜하지 않은 게시글입니다."));

        likeRepository.delete(like);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPostLiked(Member member, Long postPk) {
        SalePost post = salePostRepository.findById(postPk)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return likeRepository.existsByMemberPkAndPostPk(member, post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalePostSummaryResponse> getAllLikedPosts(Member member) {

        return likeRepository.findAllByMemberPk(member).stream()
                .map(SalePostLike::getPostPk)
                .filter(salePost -> salePost.getDeleteYn() == Yn.N)
                .map(salePost -> {
                    String thumbnailUrl = salePostImageRepository
                            .findTopByPostPkAndDeleteYnOrderByImagePkAsc(salePost, Yn.N)
                            .map(SalePostImage::getImageUrl)
                            .orElseThrow(() -> new IllegalStateException("썸네일 이미지가 존재하지 않습니다."));

                    int likeCount = likeRepository.countByPostPk(salePost); // 찜 수 조회

                    return SalePostSummaryResponse.from(salePost, thumbnailUrl, true, likeCount);
                })
                .toList();
    }
}