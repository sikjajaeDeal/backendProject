package likelion.beanBa.backendProject.product.dto;

import java.time.LocalDateTime;

public interface TopPostSummaryProjection {
    Long getPostPk();
    String getSellerNickname();
    String getCategoryName();
    String getTitle();
    String getContent();
    Integer getHopePrice();
    Long getViewCount();
    Integer getLikeCount();
    LocalDateTime getPostAt();
    LocalDateTime getStateAt();
    String getState(); // enum name
    Double getLatitude();
    Double getLongitude();
}
