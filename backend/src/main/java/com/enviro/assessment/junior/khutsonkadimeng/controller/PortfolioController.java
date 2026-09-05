package com.enviro.assessment.junior.khutsonkadimeng.controller;

import com.enviro.assessment.junior.khutsonkadimeng.dto.InvestorPortfolioResponse;
import com.enviro.assessment.junior.khutsonkadimeng.dto.WithdrawalRequest;
import com.enviro.assessment.junior.khutsonkadimeng.model.WithdrawalNotice;
import com.enviro.assessment.junior.khutsonkadimeng.service.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/investors/{id}/portfolio")
    public ResponseEntity<InvestorPortfolioResponse> getInvestorPortfolio(@PathVariable Integer id) {
        return ResponseEntity.ok(portfolioService.getInvestorPortfolio(id));
     }

    @PostMapping("/withdrawals")
    public ResponseEntity<WithdrawalNotice> createWithdrawal(@Valid @RequestBody WithdrawalRequest request) {
        WithdrawalNotice notice = portfolioService.createWithdrawal(request);
        return new ResponseEntity<>(notice, HttpStatus.CREATED);
    }

    @GetMapping("/withdrawals")
    public ResponseEntity<List<WithdrawalNotice>> getAllWithdrawals() {
        return ResponseEntity.ok(portfolioService.getAllWithdrawalNotices());
    }

    @GetMapping("/withdrawals/export")
    public ResponseEntity<String> exportWithdrawalHistory(
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) String status) {
        String csv = portfolioService.exportWithdrawalHistory(productId, status);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, "text/csv")
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=enviro365-withdrawal-history.csv")
                .body(csv);
    }
}
