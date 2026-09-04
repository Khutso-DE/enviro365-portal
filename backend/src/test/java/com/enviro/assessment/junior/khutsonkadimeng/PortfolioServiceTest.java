package com.enviro.assessment.junior.khutsonkadimeng;

import com.enviro.assessment.junior.khutsonkadimeng.dto.InvestorPortfolioResponse;
import com.enviro.assessment.junior.khutsonkadimeng.dto.WithdrawalRequest;
import com.enviro.assessment.junior.khutsonkadimeng.model.Investor;
import com.enviro.assessment.junior.khutsonkadimeng.model.Product;
import com.enviro.assessment.junior.khutsonkadimeng.model.WithdrawalNotice;
import com.enviro.assessment.junior.khutsonkadimeng.repository.InvestorRepository;
import com.enviro.assessment.junior.khutsonkadimeng.repository.ProductRepository;
import com.enviro.assessment.junior.khutsonkadimeng.repository.WithdrawalNoticeRepository;
import com.enviro.assessment.junior.khutsonkadimeng.service.PortfolioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PortfolioServiceTest {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private InvestorRepository investorRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WithdrawalNoticeRepository withdrawalNoticeRepository;

    private Investor investor;
    private Product retirementProduct;
    private Product savingsProduct;

    @BeforeEach
    void setUp() {
        withdrawalNoticeRepository.deleteAll();
        productRepository.deleteAll();
        investorRepository.deleteAll();

        investor = new Investor();
        investor.setFirstName("Jane");
        investor.setLastName("Doe");
        investor.setEmail("jane@email.com");
        investor.setContactNumber("0810000000");
        investor.setAge(70);
        investor = investorRepository.save(investor);

        retirementProduct = new Product();
        retirementProduct.setType("RETIREMENT");
        retirementProduct.setName("Retirement Growth");
        retirementProduct.setCurrentBalance(1000.0);
        retirementProduct.setInvestorId(investor.getId());
        retirementProduct = productRepository.save(retirementProduct);

        savingsProduct = new Product();
        savingsProduct.setType("SAVINGS");
        savingsProduct.setName("Emergency Savings");
        savingsProduct.setCurrentBalance(500.0);
        savingsProduct.setInvestorId(investor.getId());
        savingsProduct = productRepository.save(savingsProduct);
    }

    @Test
    void getInvestorPortfolio_returnsInvestorAndProducts() {
        InvestorPortfolioResponse response = portfolioService.getInvestorPortfolio(investor.getId());

        assertEquals(investor.getId(), response.id());
        assertEquals("Jane", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals(2, response.products().size());
    }

    @Test
    void createWithdrawal_rejects_retirement_with_age_under_65() {
        investor.setAge(64);
        investor = investorRepository.save(investor);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> portfolioService.createWithdrawal(new WithdrawalRequest(
                        retirementProduct.getId(), 200.0, "ABC Bank 123456"))
        );

        assertTrue(exception.getMessage().contains("Retirement withdrawals are only allowed"));
    }

    @Test
    void createWithdrawal_rejects_amount_over_90_percent_of_balance() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> portfolioService.createWithdrawal(new WithdrawalRequest(
                        savingsProduct.getId(), 451.0, "ABC Bank 123456"))
        );

        assertTrue(exception.getMessage().contains("90%"));
    }

    @Test
    void createWithdrawal_saves_valid_notice() {
        WithdrawalNotice notice = portfolioService.createWithdrawal(new WithdrawalRequest(
                savingsProduct.getId(), 200.0, "ABC Bank 123456"));

        assertNotNull(notice);
        assertEquals("PENDING", notice.getStatus());
        assertEquals(200.0, notice.getWithdrawalAmount());
        assertEquals(savingsProduct.getId(), notice.getProductId());
    }

    @Test
    void exportWithdrawalHistory_returnsCsvReport() {
        portfolioService.createWithdrawal(new WithdrawalRequest(savingsProduct.getId(), 200.0, "ABC Bank 123456"));

        String csv = portfolioService.exportWithdrawalHistory(null, null);

        assertTrue(csv.startsWith("id,productId,withdrawalAmount,bankingDetails,status,noticeDate"));
        assertTrue(csv.contains("ABC Bank 123456"));
    }
}
