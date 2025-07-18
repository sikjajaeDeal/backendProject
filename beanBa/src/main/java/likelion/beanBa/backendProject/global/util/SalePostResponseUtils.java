package likelion.beanBa.backendProject.global.util;

import likelion.beanBa.backendProject.like.repository.SalePostLikeRepository;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.entity.SalePostImage;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import likelion.beanBa.backendProject.product.repository.SalePostImageRepository;

import java.util.Set;

public class SalePostResponseUtils {

    public static SalePostSummaryResponse toSummaryResponse(
            SalePost salePost,
            Set<Long> likedPostPks,
            SalePostImageRepository imageRepository,
            SalePostLikeRepository likeRepository
    ) {
        String thumbnail = imageRepository
                .findTopByPostPkAndDeleteYnOrderByImagePkAsc(salePost, Yn.N)
                .map(SalePostImage::getImageUrl)
                .orElse(null);

        boolean isLiked = likedPostPks.contains(salePost.getPostPk());
        int likeCount = likeRepository.countByPostPk(salePost);

        return SalePostSummaryResponse.from(salePost, thumbnail, isLiked, likeCount);
    }

    private SalePostResponseUtils() {} // 유틸 클래스는 생성자 막기
}