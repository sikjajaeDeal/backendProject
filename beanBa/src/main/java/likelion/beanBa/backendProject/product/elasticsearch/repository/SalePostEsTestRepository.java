package likelion.beanBa.backendProject.product.elasticsearch.repository;

import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsDocument;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsTestDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalePostEsTestRepository extends ElasticsearchRepository<SalePostEsTestDocument,Long> {

  void deleteById(Long id);
}
