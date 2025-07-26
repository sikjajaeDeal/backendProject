package likelion.beanBa.backendProject.mypage.service;

import likelion.beanBa.backendProject.like.repository.SalePostLikeRepository;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.dto.PageResponse;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.entity.SalePostImage;
import likelion.beanBa.backendProject.product.product_enum.SaleStatement;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import likelion.beanBa.backendProject.product.repository.SalePostImageRepository;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
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
    public PageResponse<SalePostSummaryResponse> getMySalePosts(Member loginMember, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "postAt"));
        Page<SalePost> mySalePosts = salePostRepository
                .findAllBySellerPkAndDeleteYn(loginMember, Yn.N, pageable);

        Set<Long> likedPostPks = salePostLikeRepository.findAllByMemberPk(loginMember).stream()
                .map(like -> like.getPostPk().getPostPk())
                .collect(Collectors.toSet());

        List<SalePostSummaryResponse> mySalePostContent = mySalePosts.getContent().stream()
                .map(mySalePost -> {
                    List<SalePostImage> images = salePostImageRepository.findAllByPostPkAndDeleteYn(mySalePost, Yn.N);

                    String thumbnailUrl = images.stream()
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

                    return SalePostSummaryResponse.from(mySalePost, thumbnailUrl, isLiked, likeCount);
                })
                .toList();

        return new PageResponse<>(
                mySalePostContent,
                mySalePosts.getNumber(),
                mySalePosts.getSize(),
                mySalePosts.getTotalElements(),
                mySalePosts.getTotalPages(),
                mySalePosts.isLast()
        );
    }

    @Override
    public PageResponse<SalePostSummaryResponse> getMyPurchasedPosts(Member loginMember, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "stateAt"));
        Page<SalePost> myPurchasedPosts = salePostRepository
                .findAllByBuyerPkAndStateAndDeleteYn(loginMember, SaleStatement.C, Yn.N, pageable);

        Set<Long> likedPostPks = salePostLikeRepository.findAllByMemberPk(loginMember).stream()
                .map(like -> like.getPostPk().getPostPk())
                .collect(Collectors.toSet());

        List<SalePostSummaryResponse> myPurchasedContent = myPurchasedPosts.getContent().stream()
                .map(myPurchasedPost -> {
                    List<SalePostImage> images = salePostImageRepository.findAllByPostPkAndDeleteYn(myPurchasedPost, Yn.N);

                    String thumbnailUrl = images.stream()
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

                    return SalePostSummaryResponse.from(myPurchasedPost, thumbnailUrl, isLiked, likeCount);
                })
                .toList();

        return new PageResponse<>(
                myPurchasedContent,
                myPurchasedPosts.getNumber(),
                myPurchasedPosts.getSize(),
                myPurchasedPosts.getTotalElements(),
                myPurchasedPosts.getTotalPages(),
                myPurchasedPosts.isLast()
        );
    }
}