package likelion.beanBa.backendProject.mypage.service;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.mypage.dto.MyPagePostResponse;
import likelion.beanBa.backendProject.product.entity.SalePostImage;
import likelion.beanBa.backendProject.product.product_enum.SaleStatement;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import likelion.beanBa.backendProject.product.repository.SalePostImageRepository;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final SalePostRepository salePostRepository;
    private final SalePostImageRepository salePostImageRepository;

    @Override
    public List<MyPagePostResponse> getMySalePosts(Member loginMember) {
        return salePostRepository
                .findAllBySellerPkAndDeleteYnOrderByPostAtDesc(loginMember, Yn.N)
                .stream()
                .map(salePost -> {
                    SalePostImage thumbnailImage = salePostImageRepository
                            .findTopByPostPkAndDeleteYnOrderByImagePkAsc(salePost, Yn.N)
                            .orElse(null);

                    String thumbnail = thumbnailImage != null ? thumbnailImage.getImageUrl() : null;
                    return MyPagePostResponse.from(salePost, thumbnail);
                })
                .toList();
    }

    @Override
    public List<MyPagePostResponse> getMyPurchasedPosts(Member loginMember) {
        return salePostRepository
                .findAllByBuyerPkAndStateAndDeleteYnOrderByPostAtDesc(loginMember, SaleStatement.C, Yn.N)
                .stream()
                .map(purchasedPost -> {
                    SalePostImage thumbnailImage = salePostImageRepository
                            .findTopByPostPkAndDeleteYnOrderByImagePkAsc(purchasedPost, Yn.N)
                            .orElse(null);

                    String thumbnail = thumbnailImage != null ? thumbnailImage.getImageUrl() : null;
                    return MyPagePostResponse.from(purchasedPost, thumbnail);
                })
                .toList();
    }
}