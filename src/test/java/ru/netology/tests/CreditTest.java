package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.page.HeadPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.SQLHelper.*;

public class CreditTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void openPage() {
        String baseUrl = System.getProperty("sut.url", "http://localhost:8080");
        open(baseUrl);
    }

    @AfterEach
    void clean() {
        cleanDatabase();
    }

    @Test
    void creditApproved_validCard_shouldShowSuccess_andWriteApprovedToDb() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();

        creditCardPay.buyForm(ru.netology.data.BankCardDataHelper.getCardValidValuesCurrentMonthYear());
        creditCardPay.operationWasApprovedByBank();

        var status = getCreditCardStatus();
        assertEquals("APPROVED", status);
    }

    @Test
    void creditDeclined_declinedCard_shouldShowError_andWriteDeclinedToDb() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();

        creditCardPay.buyForm(ru.netology.data.BankCardDataHelper.getCardInValidValuesNextMonthYear());
        creditCardPay.bankRefusedToCarryOutOperation();

        var status = getCreditCardStatus();
        assertEquals("DECLINED", status);
    }

    @Test
    void credit_emptyForm_shouldShowRequiredFieldErrors() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();

        creditCardPay.buyForm(ru.netology.data.BankCardDataHelper.getEmptyNumberCardValidValuesMonthYear());
        creditCardPay.fieldsIsRequiredToFillIn();
    }

    @Test
    void credit_invalidMonth00_shouldShowValidationError() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();

        creditCardPay.buyForm(ru.netology.data.BankCardDataHelper.getValidCardValuesTwoZeroMonthYear());
        creditCardPay.validityPeriodOfCardIsSpecifiedIncorrectly();
    }

    @Test
    void credit_invalidMonth13_shouldShowValidationError() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();

        creditCardPay.buyForm(ru.netology.data.BankCardDataHelper.getValidCardValuesUnrealMonthYear());
        creditCardPay.validityPeriodOfCardIsSpecifiedIncorrectly();
    }

    @Test
    void credit_yearInPast_shouldShowValidationError() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();

        creditCardPay.buyForm(ru.netology.data.BankCardDataHelper.getValidCardValuesMinusMonthNextYear());
        creditCardPay.cardExpired();
    }

    @Test
    void credit_emptyCvc_shouldShowRequiredError() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();

        creditCardPay.buyForm(ru.netology.data.BankCardDataHelper.getEmptyCvcCardValidValuesMonthYear());
        creditCardPay.invalidFormat();
    }
}
