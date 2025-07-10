package likelion.beanBa.backendProject.product.elasticsearch.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsDocument;
import likelion.beanBa.backendProject.product.elasticsearch.repository.SalePostEsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalePostEsServiceImpl implements SalePostEsService {


    private final ElasticsearchClient client;

    private final SalePostEsRepository repository;

    public void save(SalePostEsDocument document) {
        repository.save(document);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Page<SalePostEsDocument> search(String keyword, int page, int size){

        try{
            System.out.println("검색 시작 : ");

            //엘라스틱서치에서 페이징을 위한 시작 위치를 계산하는 변수
            int from = page * size;

            //엘라스틱서치에서 사용할 검색조건을 담는 객체
            Query query;

            //검색어가 없으면 모든 문서를 검색하는 matchAll 쿼리
            if (keyword == null || keyword.isBlank()) {
                query = MatchAllQuery.of(m -> m)._toQuery(); // 전체 문서를 가져오는 쿼리를 생성하는 람다함수
                // MatchAllQuery는 엘라스틱서치에서 조건 없이 모든 문서를 검색할 때 사용하는 쿼리
            }
            //검색어가 있을 때
            else {
                //boolquery는 복수 조건을 조합할 때 사용하는 쿼리
                // 이 쿼리 안에 여러개의 조건을 나열
                //예를 들어 "백엔드"라는 키워드가 들어왔을 때 이 "백엔드" 키워드를 어떻게 분석해서 데이터를 보여줄 것인가를 작성
                query = BoolQuery.of(b ->{

                    System.out.println("키워드는 : "+keyword);

                    // PrefixQuery는 해당 필드가 특정 단어로 시작하는지 검사하는 쿼리
                    // MatchQuery 는 해당 단어가 포함되어 있는지 검사하는 쿼리

                    /**
                     must: 모두 일치해야 함 (AND)
                     should: 하나라도 일치하면 됨 (OR)
                     must_not: 해당 조건을 만족하면 제외
                     filter : must와 같지만 점수 계산 안함 (속도가 빠름)
                     **/

                    //접두어 글자 검색
                    b.should(PrefixQuery.of(p->p.field("title").value(keyword))._toQuery());
                    b.should(PrefixQuery.of(p->p.field("content").value(keyword))._toQuery());

                    //초성 검색
                    b.should(PrefixQuery.of(p->p.field("title.chosung").value(keyword))._toQuery());
                    b.should(PrefixQuery.of(p->p.field("content.chosung").value(keyword))._toQuery());

                    //중간 글자 검색(match만 가능)
                    b.should(MatchQuery.of(p->p.field("title.ngram").query(keyword))._toQuery());
                    b.should(MatchQuery.of(p->p.field("content.ngram").query(keyword))._toQuery());

                    // fuzziness: "AUTO"는  오타 허용 검색 기능을 자동으로 켜주는 설정 -> 유사도 계산을 매번 수행하기 때문에 느림
                    //짧은 키워드에는 사용 xxx
                    //오타 허용 (오타허용은 match만 가능 )
                    if (keyword.length()>=3){
                        b.should(MatchQuery.of(m ->m.field("title").query(keyword).fuzziness("AUTO"))._toQuery());
                        b.should(MatchQuery.of(m ->m.field("content").query(keyword).fuzziness("AUTO"))._toQuery());
                    }

                    return b;
                })._toQuery();

            }
            //
            //SearchRequest 는 엘라스틱서치에서 검색을 하기 위한 검색요청 객체
            // 인덱스명, 페이징 정보, 쿼리를 포함한 검색 요청

            SearchRequest request = SearchRequest.of(s->s
                    .index("sale_post")
                    .from(from)
                    .size(size)
                    .query(query)

                    //정렬
                    .sort(sort->sort
                            .field(f->f
                                            .field("postPk") // 정렬 대상 필드명
                                            .order(SortOrder.Desc) // 최신순
                                    // 만약 id를 기준으로 할꺼면 board-index.txt 에서 "id"를 long으로 변경
                            )

                    )

            );

            //SearchResponse는 엘라스틱서치의 검색 결과를 담고 있는 응답 객체
            SearchResponse<SalePostEsDocument> response =
                    // 엘라스틱서치에 명령을 전달하는 자바API 검색요청을 담아서 응답객체로 반환
                    client.search(request, SalePostEsDocument.class);

            //위 응답객체에서 받은 검색 결과 중 문서만 추출해서 리스트로 만듬
            // Hit는 엘라스틱서치에서 검색된 문서 1개를 감싸고 있는 객체
            List<SalePostEsDocument> content = response.hits() //엘라스틱 서치 응답에서 hits(문서 검색결과) 전체를 꺼냄
                    .hits()// 검색 결과 안에 개별 리스트를 가져옴
                    .stream()// 자바 stream api를 사용
                    .map(Hit::source)// 각 Hit 객체에서 실제 문서를 꺼내는 작업
                    .collect(Collectors.toList()); // 위에서 꺼낸 객체를 자바 List에 넣음


            //전체 검색 결과 수(총 문서의 갯수)
            long total = response.hits().total().value();

            //PageImpl 객체를 사용해서 Spring에서 사용할 수 있는 page 객체로 변환

            return new PageImpl<>(content, PageRequest.of(page,size),total);


        }catch(Exception e){
            log.error("검색 오류",e.getMessage());
            throw new RuntimeException("검색 중 오류 발생",e);

        }

    }
}
