package likelion.beanBa.backendProject.product.repository;


import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.entity.SalePostImage;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalePostImageRepository extends JpaRepository<SalePostImage, Long> {
    List<SalePostImage> findAllByPostPkAndDeleteYn(SalePost salePost, Yn deleteYn);

    // 이미지들을 오름차순 정렬하여 가장 위에 것(가장 첫번째 등록한) 하나를 가져옴
    Optional<SalePostImage> findTopByPostPkAndDeleteYnOrderByImagePkAsc(SalePost salePost, Yn deleteYn);

    //삭제되지 않은 글만 단건 조회
    Optional<SalePostImage> findByPostPkAndDeleteYn(SalePost postPk, Yn deleteYn);

    // 삭제되지 않은 이미지 중에서 imageOrder 가장 낮은 이미지 1개
    Optional<SalePostImage> findTopByPostPkAndDeleteYnOrderByImageOrderAsc(SalePost postPk, Yn deleteYn);


}
