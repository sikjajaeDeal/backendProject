package likelion.beanBa.backendProject.product.repository;


import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalePostRepository extends JpaRepository<SalePost, Long> {

    // 삭제되지 않은 게시글 전체 조회
    List<SalePost> findAllByDeleteYn(Yn deleteYn);
}
