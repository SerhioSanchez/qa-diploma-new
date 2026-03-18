package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.page.HeadPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.SQLHelper.*;

public class DebitTest {

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
    void debitApproved_validCard_shouldShowSuccess_andWriteApprovedToDb() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();

        debitCardPay.buyForm(ru.netology.data.BankCardDataHelper.getCardValidValuesCurrentMonthYear());
        debitCardPay.operationWasApprovedByBank();

        var status = getDebitCardStatus();
        assertEquals("APPROVED", status);
    }

    @Test
    void debitDeclined_declinedCard_shouldShowError_andWriteDeclinedToDb() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();

        debitCardPay.buyForm(ru.netology.data.BankCardDataHelper.getCardInValidValuesNextMonthYear());
        debitCardPay.bankRefusedToCarryOutOperation();

        var status = getDebitCardStatus();
        assertEquals("DECLINED", status);
    }

    @Test
    void debit_emptyForm_shouldShowRequiredFieldErrors() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();

        debitCardPay.buyForm(ru.netology.data.BankCardDataHelper.getEmptyNumberCardValidValuesMonthYear());
        debitCardPay.fieldsIsRequiredToFillIn();
    }

    @Test
    void debit_invalidMonth00_shouldShowValidationError() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();

        debitCardPay.buyForm(ru.netology.data.BankCardDataHelper.getValidCardValuesTwoZeroMonthYear());
        debitCardPay.validityPeriodOfCardIsSpecifiedIncorrectly();
    }

    @Test
    void debit_invalidMonth13_shouldShowValidationError() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();

        debitCardPay.buyForm(ru.netology.data.BankCardDataHelper.getValidCardValuesUnrealMonthYear());
        debitCardPay.validityPeriodOfCardIsSpecifiedIncorrectly();
    }

    @Test
    void debit_yearInPast_shouldShowValidationError() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();

        debitCardPay.buyForm(ru.netology.data.BankCardDataHelper.getValidCardValuesMinusMonthNextYear());
        debitCardPay.cardExpired();
    }

    @Test
    void debit_emptyCvc_shouldShowRequiredError() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();

        debitCardPay.buyForm(ru.netology.data.BankCardDataHelper.getEmptyCvcCardValidValuesMonthYear());
        debitCardPay.invalidFormat();
    }
}
