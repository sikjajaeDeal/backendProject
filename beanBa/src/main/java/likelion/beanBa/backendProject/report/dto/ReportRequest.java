package likelion.beanBa.backendProject.report.dto;

import lombok.Getter;

@Getter
public class ReportRequest {
    private Long postPk;
    private String reporteeId;
    private String reportReason;
    private String reportKind;
}
