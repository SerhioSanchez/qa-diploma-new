package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import ru.netology.page.HeadPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.SQLHelper.cleanDatabase;
import static ru.netology.data.SQLHelper.getCreditCardStatus;
import static ru.netology.data.BankCardDataHelper.getCardInValidValuesNextMonthYear;
import static ru.netology.data.BankCardDataHelper.getCardValidValuesCurrentMonthYear;

/**
 * UI-тесты: форма "Кредит по данным карты".
 * Важно: НЕ хардкодим URL — берем из system property:
 * -Dapp.url=http://localhost:8080
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreditTest {

    private static String baseUrl() {
        return System.getProperty("app.url", "http://localhost:8080");
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void openApp() {
        open(baseUrl());
    }

    @AfterEach
    void clean() {
        cleanDatabase();
    }

    @Test
    @Order(1)
    void creditApproved() {
        var headPage = new HeadPage();
        var creditPage = headPage.openPaymentByCreditPage();

        creditPage.buyForm(getCardValidValuesCurrentMonthYear());
        creditPage.operationIsApprovedByBank();

        var status = getCreditCardStatus();
        assertEquals("APPROVED", status);
    }

    @Test
    @Order(2)
    void creditDeclined() {
        var headPage = new HeadPage();
        var creditPage = headPage.openPaymentByCreditPage();

        creditPage.buyForm(getCardInValidValuesNextMonthYear());
        creditPage.bankRefusedToCarryOutOperation();

        var status = getCreditCardStatus();
        assertEquals("DECLINED", status);
    }
}
