package com.enviro.assessment.junior.khutsonkadimeng.service;

import com.enviro.assessment.junior.khutsonkadimeng.dto.InvestorPortfolioResponse;
import com.enviro.assessment.junior.khutsonkadimeng.dto.ProductResponse;
import com.enviro.assessment.junior.khutsonkadimeng.dto.WithdrawalRequest;
import com.enviro.assessment.junior.khutsonkadimeng.model.Investor;
import com.enviro.assessment.junior.khutsonkadimeng.model.Product;
import com.enviro.assessment.junior.khutsonkadimeng.model.WithdrawalNotice;
import com.enviro.assessment.junior.khutsonkadimeng.repository.InvestorRepository;
import com.enviro.assessment.junior.khutsonkadimeng.repository.ProductRepository;
import com.enviro.assessment.junior.khutsonkadimeng.repository.WithdrawalNoticeRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    private final InvestorRepository investorRepository;
    private final ProductRepository productRepository;
    private final WithdrawalNoticeRepository withdrawalNoticeRepository;

    public PortfolioService(
            InvestorRepository investorRepository,
            ProductRepository productRepository,
            WithdrawalNoticeRepository withdrawalNoticeRepository) {
        this.investorRepository = investorRepository;
        this.productRepository = productRepository;
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
    }

    public InvestorPortfolioResponse getInvestorPortfolio(Integer investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new IllegalArgumentException("Investor not found with ID: " + investorId));

        List<Product> products = productRepository.findByInvestorId(investorId);

        List<ProductResponse> productResponses = products.stream()
                .map(p -> new ProductResponse(p.getId(), p.getType(), p.getName(), p.getCurrentBalance()))
                .collect(Collectors.toList());

        return new InvestorPortfolioResponse(
                investor.getId(),
                investor.getFirstName(),
                investor.getLastName(),
                investor.getAge(),
                productResponses);
    }

    public WithdrawalNotice createWithdrawal(WithdrawalRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + request.productId()));

        Investor investor = investorRepository.findById(product.getInvestorId())
                .orElseThrow(() -> new IllegalArgumentException("Investor not found for product ID: " + request.productId()));

        double currentBalance = product.getCurrentBalance() == null ? 0.0 : product.getCurrentBalance();
        double requestAmount = request.withdrawalAmount();

        if ("RETIREMENT".equalsIgnoreCase(product.getType()) && investor.getAge() <= 65) {
            throw new IllegalArgumentException("Retirement withdrawals are only allowed if the investor's age is greater than 65.");
        }

        if (requestAmount > currentBalance) {
            throw new IllegalArgumentException("Withdrawal amount cannot exceed the total balance.");
        }

        double maxAllowedAmount = currentBalance * 0.9;
        if (requestAmount > maxAllowedAmount) {
            throw new IllegalArgumentException("Withdrawal amount cannot exceed 90% of the current balance.");
        }

        WithdrawalNotice notice = new WithdrawalNotice();
        notice.setProductId(product.getId());
        notice.setWithdrawalAmount(requestAmount);
        notice.setBankingDetails(request.bankingDetails());
        notice.setNoticeDate(LocalDateTime.now());
        notice.setStatus("PENDING");

        product.setCurrentBalance(currentBalance - requestAmount);
        productRepository.save(product);

        return withdrawalNoticeRepository.save(notice);
    }

    public List<WithdrawalNotice> getAllWithdrawalNotices() {
        return withdrawalNoticeRepository.findAll();
    }

    public String exportWithdrawalHistory(Integer productId, String status) {
        List<WithdrawalNotice> notices = withdrawalNoticeRepository.findAll();

        if (productId != null) {
            notices = notices.stream()
                    .filter(n -> n.getProductId() != null && n.getProductId().equals(productId))
                    .collect(Collectors.toList());
        }

        if (status != null && !status.isBlank()) {
            notices = notices.stream()
                    .filter(n -> n.getStatus() != null && n.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        List<String> lines = new ArrayList<>();
        lines.add("id,productId,withdrawalAmount,bankingDetails,status,noticeDate");

        for (WithdrawalNotice notice : notices) {
            lines.add(String.format(
                    "%s,%s,%s,%s,%s,%s",
                    notice.getId() == null ? "" : notice.getId(),
                    notice.getProductId() == null ? "" : notice.getProductId(),
                    notice.getWithdrawalAmount() == null ? "" : notice.getWithdrawalAmount(),
                    notice.getBankingDetails() == null ? "" : notice.getBankingDetails(),
                    notice.getStatus() == null ? "" : notice.getStatus(),
                    notice.getNoticeDate() == null ? "" : notice.getNoticeDate()
            ));
        }

        return String.join(System.lineSeparator(), lines);
    }
}