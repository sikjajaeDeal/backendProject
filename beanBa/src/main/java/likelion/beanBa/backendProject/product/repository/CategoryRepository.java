package likelion.beanBa.backendProject.product.repository;

import likelion.beanBa.backendProject.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByDeleteYn(String deleteYn);
}