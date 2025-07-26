package likelion.beanBa.backendProject.report.controller;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.service.MemberService;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.report.dto.ReportRequest;
import likelion.beanBa.backendProject.report.entity.Report;
import likelion.beanBa.backendProject.report.service.ReportService;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/submit")
    public ResponseEntity<String> reportMember(
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody ReportRequest request) {
        reportService.createReport(request, userDetails.getMember());
        return ResponseEntity.ok("신고가 정상적으로 완료되었습니다.");
    }

    @GetMapping("/getAllBlockMember")
    public ResponseEntity<List<Member>> getAllBlockMember(
            @CurrentUser CustomUserDetails userDetails) {
        return ResponseEntity.ok(reportService.getAllBlockMember());
    }

    @GetMapping("/getBlockMember/{memberPk}")
    public ResponseEntity<List<Report>> getBlockMember(
            @CurrentUser CustomUserDetails userDetails,
            @PathVariable("memberPk") Long memberPk) {
        return ResponseEntity.ok(reportService.getBlockMember(memberPk));
    }

    @GetMapping("/getAllBlockPost")
    public ResponseEntity<List<SalePost>> getAllBlockPost(
            @CurrentUser CustomUserDetails userDetails) {
        return ResponseEntity.ok(reportService.getAllBlockPost());
    }

    @GetMapping("/getBlockPost/{postPk}")
    public ResponseEntity<List<Report>> getBlockPost(
            @CurrentUser CustomUserDetails userDetails,
            @PathVariable("postPk") Long postPk) {
        return ResponseEntity.ok(reportService.getBlockPost(postPk));
    }

}
