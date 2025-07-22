package likelion.beanBa.backendProject.admin.dashboard.controller;


import likelion.beanBa.backendProject.admin.dashboard.service.AdminDashboardService;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;


    /**관리자 main 화면 들어갈 시에 통합 요약본
     단순히 통계는 service단에서가 아닌
     * */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {

        return ResponseEntity.ok(adminDashboardService.getDashboardStats());
    }
}
