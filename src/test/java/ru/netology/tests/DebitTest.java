package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import ru.netology.page.HeadPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.SQLHelper.*;
import static ru.netology.data.BankCardDataHelper.getCardInValidValuesNextMonthYear;
import static ru.netology.data.BankCardDataHelper.getCardValidValuesCurrentMonthYear;

/**
 * UI-тесты: форма "Оплата по карте".
 * Важно: НЕ хардкодим URL — берем из system property:
 * -Dapp.url=http://localhost:8080
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DebitTest {

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
    void debitApproved() {
        var headPage = new HeadPage();
        var debitPage = headPage.openPaymentByCardPage();

        debitPage.buyForm(getCardValidValuesCurrentMonthYear());
        debitPage.operationIsApprovedByBank();

        var status = getDebitCardStatus();
        assertEquals("APPROVED", status);
    }

    @Test
    @Order(2)
    void debitDeclined() {
        var headPage = new HeadPage();
        var debitPage = headPage.openPaymentByCardPage();

        debitPage.buyForm(getCardInValidValuesNextMonthYear());
        debitPage.bankRefusedToCarryOutOperation();

        var status = getDebitCardStatus();
        assertEquals("DECLINED", status);
    }
}
