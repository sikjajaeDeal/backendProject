package likelion.beanBa.backendProject.product.elasticsearch.service;

import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsDocument;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SearchRequestDTO;
import likelion.beanBa.backendProject.product.entity.SalePost;
import org.springframework.data.domain.Page;

public interface SalePostEsService {

  Page<SalePostEsDocument> search(SearchRequestDTO searchRequestDTO);

  void save(SalePost salePost);

  void delete(SalePost salePost);

  void update(SalePost salePost);

}
