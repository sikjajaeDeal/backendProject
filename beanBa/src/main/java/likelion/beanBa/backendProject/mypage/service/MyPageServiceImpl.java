package likelion.beanBa.backendProject.mypage.service;

import likelion.beanBa.backendProject.like.repository.SalePostLikeRepository;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.entity.SalePostImage;
import likelion.beanBa.backendProject.product.product_enum.SaleStatement;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import likelion.beanBa.backendProject.product.repository.SalePostImageRepository;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final SalePostRepository salePostRepository;
    private final SalePostImageRepository salePostImageRepository;
    private final SalePostLikeRepository salePostLikeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SalePostSummaryResponse> getMySalePosts(Member loginMember) {

        List<SalePost> mySalePosts = salePostRepository
                .findAllBySellerPkAndDeleteYnOrderByPostAtDesc(loginMember, Yn.N);

        Set<Long> likedPostPks = salePostLikeRepository.findAllByMemberPk(loginMember).stream()
                .map(like -> like.getPostPk().getPostPk())
                .collect(Collectors.toSet());

        return mySalePosts.stream()
                .map(mySalePost -> {
                    List<SalePostImage> images = salePostImageRepository.findAllByPostPkAndDeleteYn(mySalePost, Yn.N);

                    String thumbnail = images.stream()
                            .sorted((a, b) -> {
                                Integer orderA = a.getImageOrder() != null ? a.getImageOrder() : Integer.MAX_VALUE;
                                Integer orderB = b.getImageOrder() != null ? b.getImageOrder() : Integer.MAX_VALUE;
                                return orderA.compareTo(orderB);
                            })
                            .map(SalePostImage::getImageUrl)
                            .findFirst()
                            .orElse(null);

                    boolean isLiked = likedPostPks.contains(mySalePost.getPostPk());

                    int likeCount = salePostLikeRepository.countByPostPk(mySalePost); // 찜 수 조회

                    return SalePostSummaryResponse.from(mySalePost, thumbnail, isLiked, likeCount);
                })
                .toList();
    }

    @Override
    public List<SalePostSummaryResponse> getMyPurchasedPosts(Member loginMember) {

        List<SalePost> myPurchasedPosts = salePostRepository
                .findAllByBuyerPkAndStateAndDeleteYnOrderByPostAtDesc(loginMember, SaleStatement.C, Yn.N);

        Set<Long> likedPostPks = salePostLikeRepository.findAllByMemberPk(loginMember).stream()
                .map(like -> like.getPostPk().getPostPk())
                .collect(Collectors.toSet());

        return myPurchasedPosts.stream()
                .map(myPurchasedPost -> {
                    List<SalePostImage> images = salePostImageRepository.findAllByPostPkAndDeleteYn(myPurchasedPost, Yn.N);

                    String thumbnail = images.stream()
                            .sorted((a, b) -> {
                                Integer orderA = a.getImageOrder() != null ? a.getImageOrder() : Integer.MAX_VALUE;
                                Integer orderB = b.getImageOrder() != null ? b.getImageOrder() : Integer.MAX_VALUE;
                                return orderA.compareTo(orderB);
                            })
                            .map(SalePostImage::getImageUrl)
                            .findFirst()
                            .orElse(null);

                    boolean isLiked = likedPostPks.contains(myPurchasedPost.getPostPk());

                    int likeCount = salePostLikeRepository.countByPostPk(myPurchasedPost); // 찜 수 조회

                    return SalePostSummaryResponse.from(myPurchasedPost, thumbnail, isLiked, likeCount);
                })
                .toList();
    }
}