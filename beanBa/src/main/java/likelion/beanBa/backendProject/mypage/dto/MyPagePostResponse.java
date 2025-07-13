package likelion.beanBa.backendProject.mypage.dto;

import likelion.beanBa.backendProject.product.entity.SalePost;

import java.time.LocalDateTime;

public record MyPagePostResponse(
        Long postPk,
        String title,
        String thumbnailUrl,
        int hopePrice,
        String status,
        LocalDateTime postAt
) {
    public static MyPagePostResponse from(SalePost salePost, String thumbnailUrl) {
        return new MyPagePostResponse(
                salePost.getPostPk(),
                salePost.getTitle(),
                thumbnailUrl,
                salePost.getHopePrice(),
                salePost.getState().name(), // 필요하면 getDisplayName() 으로 변경 가능
                salePost.getPostAt()
        );
    }
}