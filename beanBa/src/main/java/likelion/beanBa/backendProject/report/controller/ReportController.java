package likelion.beanBa.backendProject.report.controller;

import likelion.beanBa.backendProject.report.dto.ReportRequest;
import likelion.beanBa.backendProject.report.service.ReportService;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<String> reportUser(
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody ReportRequest request) {

        reportService.reportUser(request, userDetails.getMember());

        return ResponseEntity.ok("신고가 정상적으로 완료되었습니다.");
    }

}
