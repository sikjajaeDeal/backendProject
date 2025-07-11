package likelion.beanBa.backendProject.product.repository;


import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.entity.SalePostImage;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalePostImageRepository extends JpaRepository<SalePostImage, Long> {
    List<SalePostImage> findAllByPostPkAndDeleteYn(SalePost salePost, Yn deleteYn);


}
