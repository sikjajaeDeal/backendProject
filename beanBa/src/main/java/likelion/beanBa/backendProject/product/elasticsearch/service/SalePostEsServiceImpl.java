package likelion.beanBa.backendProject.product.elasticsearch.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsDocument;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SearchRequestDTO;
import likelion.beanBa.backendProject.product.elasticsearch.repository.SalePostEsRepository;
import likelion.beanBa.backendProject.product.entity.SalePost;
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

    private final SalePostEsRepository esRepository;

    public void deleteById(Long id) {
        esRepository.deleteById(id);
    }

    public Page<SalePostEsDocument> search(SearchRequestDTO searchRequestDTO){

        try{
            System.out.println("검색 시작 : ");

            int page = searchRequestDTO.getPage();
            int size = searchRequestDTO.getSize();

            int minPrice = searchRequestDTO.getMinPrice();
            int maxPrice = searchRequestDTO.getMaxPrice();

            String keyword = searchRequestDTO.getKeyword();

            //엘라스틱서치에서 페이징을 위한 시작 위치를 계산하는 변수
            int from = page * size;

            //엘라스틱서치에서 사용할 검색조건을 담는 객체
            Query query;

            //검색어가 없으면 모든 문서를 검색하는 matchAll 쿼리
//            if ((keyword == null || keyword.isBlank())) {
//                query = MatchAllQuery.of(m -> m)._toQuery(); // 전체 문서를 가져오는 쿼리를 생성하는 람다함수
//                // MatchAllQuery는 엘라스틱서치에서 조건 없이 모든 문서를 검색할 때 사용하는 쿼리
//            }
            //검색어가 있을 때
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


                    // 접두어 초성 중간글자 검색 쿼리를 생성하는 메서드 입니다.
                    baseSearchFilter(b,searchRequestDTO);

                    // 위치 기반 검색 쿼리를 생성하는 메서드 입니다.(기본값은 5km)
                    locationSearchFilter(b, searchRequestDTO);

                    // 가격 범위기반 검색 쿼리를 생성하는 메서드입니다.
                    priceSearchFilter(b, searchRequestDTO.getMinPrice(), searchRequestDTO.getMaxPrice());

                    // 카테고리 검색 쿼리를 생성하는 메서드입니다.
                    categorySearchFilter(b, searchRequestDTO.getCategoryPk());


                    return b;
                })._toQuery();

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
// ================ 엘라스틱서치 i/o ====================
    public void save(SalePost salePost) {

        try {
            SalePostEsDocument doc = SalePostEsDocument.from(salePost);
            esRepository.save(doc);
        } catch (Exception e) {
            log.error("Elasticsearch 저장 오류: {}", e.getMessage());
            throw new RuntimeException("Elasticsearch 저장 중 오류 발생", e);
        }

    }

    public void delete(SalePost salePost) {
        try {
            SalePostEsDocument doc = SalePostEsDocument.from(salePost);
            esRepository.delete(doc);
        } catch (Exception e) {
            log.error("Elasticsearch 삭제 오류: {}", e.getMessage());
            throw new RuntimeException("Elasticsearch 삭제 중 오류 발생", e);
        }
    }

    public void update(SalePost salePost) {
        try {
            SalePostEsDocument doc = SalePostEsDocument.from(salePost);
            esRepository.save(doc);
        } catch (Exception e) {
            log.error("Elasticsearch 업데이트 오류: {}", e.getMessage());
            throw new RuntimeException("Elasticsearch 업데이트 중 오류 발생", e);
        }
    }
//================ 엘라스틱서치 i/o ====================


    /**
     * 접두어, 초성, 중간 글자 검색 쿼리를 생성하는 메서드입니다.
     * @param b BoolQuery.Builder 객체
     * @param searchRequestDTO 검색 요청 DTO
     */
    private void baseSearchFilter(BoolQuery.Builder b, SearchRequestDTO searchRequestDTO) {

        //검색어가 없으면 검색하지 않음
        if (searchRequestDTO.getKeyword() == null || searchRequestDTO.getKeyword().isBlank()) {
            return; // 검색어가 없으면 쿼리를 생성하지 않음
        }

        String keyword = searchRequestDTO.getKeyword();

        //접두어 글자 검색
//        b.should(PrefixQuery.of(p->p.field("title").value(keyword))._toQuery());
//        b.should(PrefixQuery.of(p->p.field("content").value(keyword))._toQuery());
//
//        //초성 검색
//        b.should(PrefixQuery.of(p->p.field("title.chosung").value(keyword))._toQuery());
//        b.should(PrefixQuery.of(p->p.field("content.chosung").value(keyword))._toQuery());
//
//        //중간 글자 검색(match만 가능)
//        b.should(MatchQuery.of(p->p.field("title.ngram").query(keyword))._toQuery());
//        b.should(MatchQuery.of(p->p.field("content.ngram").query(keyword))._toQuery());
//
//



        b.must(bb->bb.bool(bbb-> bbb.
            should(PrefixQuery.of(p->p.field("title").value(keyword))._toQuery())
            .should(PrefixQuery.of(p->p.field("title.chosung").value(keyword))._toQuery())
            .should(MatchQuery.of(p->p.field("title.ngram").query(keyword))._toQuery())
                .should(PrefixQuery.of(p->p.field("content").value(keyword))._toQuery())
                .should(PrefixQuery.of(p->p.field("content.chosung").value(keyword))._toQuery())
                .should(MatchQuery.of(p->p.field("content.ngram").query(keyword))._toQuery())
            )
        );

//
//        b.must(bb->bb.bool(bbb-> bbb.
//                should(PrefixQuery.of(p->p.field("content").value(keyword))._toQuery())
//                .should(PrefixQuery.of(p->p.field("content.chosung").value(keyword))._toQuery())
//                .should(MatchQuery.of(p->p.field("content.ngram").query(keyword))._toQuery())
//            )
//        );



        // fuzziness: "AUTO"는  오타 허용 검색 기능을 자동으로 켜주는 설정 -> 유사도 계산을 매번 수행하기 때문에 느림
        //짧은 키워드에는 사용 xxx
        //오타 허용 (오타허용은 match만 가능 )
        if (keyword.length()>=3){
            b.should(MatchQuery.of(m ->m.field("title").query(keyword).fuzziness("AUTO"))._toQuery());
            b.should(MatchQuery.of(m ->m.field("content").query(keyword).fuzziness("AUTO"))._toQuery());
        }
    }

    /**
     * 위치 기반 검색 쿼리를 생성하는 메서드입니다.
     * @param b BoolQuery.Builder 객체
     * @param searchRequestDTO 검색 요청 DTO
     */
    private void locationSearchFilter(BoolQuery.Builder b, SearchRequestDTO searchRequestDTO) {

        Integer distance = searchRequestDTO.getDistance();     //거리 5km

        if(searchRequestDTO.getLatitude() == 0.0 || searchRequestDTO.getLongitude() == 0.0) {
            return; //위도나 경도 값이 없을 경우 쿼리를 생성하지 않음
        }

        double latitude = searchRequestDTO.getLatitude();
        double longitude = searchRequestDTO.getLongitude();

        LatLonGeoLocation latLonGeoLocation = new LatLonGeoLocation.Builder()
            .lat(latitude)
            .lon(longitude)
            .build();

        b.must(
            GeoDistanceQuery.of(g -> g
                .field("geoLocation")
                .distance(distance + "km")
                .location(l -> l.latlon(latLonGeoLocation))
        )._toQuery());

    }

    private void priceSearchFilter(BoolQuery.Builder boolBuilder, int minPrice, int maxPrice) {

        if(minPrice == 0 && maxPrice == 0) {
            return;
        }

        boolBuilder.filter(f -> f.range(RangeQuery.of(r -> r
            .field("hopePrice")
            .gte(JsonData.of(minPrice))
            .lte(JsonData.of(maxPrice))
        )));
    }

    private void categorySearchFilter(BoolQuery.Builder boolBuilder, Long categoryPk) {

        if (categoryPk == null || categoryPk <= 0) {
            return; // 카테고리 검색이 필요하지 않음
        }

        boolBuilder.filter(TermQuery.of(t -> t
            .field("categoryPk")
            .value(categoryPk)
        )._toQuery());
    }


}
