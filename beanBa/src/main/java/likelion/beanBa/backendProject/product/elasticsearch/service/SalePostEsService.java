package likelion.beanBa.backendProject.product.elasticsearch.service;

import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsDocument;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SearchRequestDTO;
import org.springframework.data.domain.Page;

public interface SalePostEsService {

  Page<SalePostEsDocument> search(SearchRequestDTO searchRequestDTO);

}
