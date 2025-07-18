package likelion.beanBa.backendProject.product.elasticsearch.repository;


import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalePostEsRepository extends ElasticsearchRepository<SalePostEsDocument,Long> {

    void deleteById(Long id);
}
