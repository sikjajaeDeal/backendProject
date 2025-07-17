package likelion.beanBa.backendProject.product.dto;

import likelion.beanBa.backendProject.member.dto.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private Long totalElements;
    private int totalPage;
    private boolean last;

    public static PageResponse<MemberResponse> from(Page<MemberResponse> responsePage) {
        return new PageResponse<>(
                responsePage.getContent(),
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages(),
                responsePage.isLast()
        );
    }
}
