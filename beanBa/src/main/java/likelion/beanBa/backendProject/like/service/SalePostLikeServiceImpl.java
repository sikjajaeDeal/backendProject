package likelion.beanBa.backendProject.like.service;

import likelion.beanBa.backendProject.like.entity.SalePostLike;
import likelion.beanBa.backendProject.like.repository.SalePostLikeRepository;
import likelion.beanBa.backendProject.member.Entity.Member;
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

        //좋아요 누른 최신 순 정렬해서 보여주기
        return likeRepository.findAllByMemberPkOrderByLikedAtDesc(member).stream()
                .map(SalePostLike::getPostPk)
                .filter(salePost -> salePost.getDeleteYn() == Yn.N)
                .map(myLikedPost -> {
                    List<SalePostImage> images = salePostImageRepository.findAllByPostPkAndDeleteYn(myLikedPost, Yn.N);

                    String thumbnail = images.stream()
                            .sorted((a, b) -> {
                                Integer orderA = a.getImageOrder() != null ? a.getImageOrder() : Integer.MAX_VALUE;
                                Integer orderB = b.getImageOrder() != null ? b.getImageOrder() : Integer.MAX_VALUE;
                                return orderA.compareTo(orderB);
                            })
                            .map(SalePostImage::getImageUrl)
                            .findFirst()
                            .orElse(null);

                    int likeCount = likeRepository.countByPostPk(myLikedPost); // 찜 수 조회

                    return SalePostSummaryResponse.from(myLikedPost, thumbnail, true, likeCount);
                })
                .toList();
    }
}