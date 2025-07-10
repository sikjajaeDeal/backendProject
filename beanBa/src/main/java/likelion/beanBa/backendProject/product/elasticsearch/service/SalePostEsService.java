package likelion.beanBa.backendProject.product.elasticsearch.service;

import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsDocument;
import org.springframework.data.domain.Page;

public interface SalePostEsService {

  Page<SalePostEsDocument> search(String keyword, int page, int size);

}
