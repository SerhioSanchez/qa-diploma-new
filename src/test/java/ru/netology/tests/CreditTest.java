package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.BankCardDataHelper;
import ru.netology.page.HeadPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.SQLHelper.cleanDatabase;
import static ru.netology.data.SQLHelper.getCreditCardStatus;

public class CreditTest {

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @BeforeEach
    public void openPage() {
        open("http://localhost:8080/");
    }

    @AfterAll
    static void tearDown() {
        cleanDatabase();
    }

    // Карта со статусом APPROVED, Все поля заполнены валидными данными
    @Test
    public void testCreditPayValidCard() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getCardValidValuesCurrentMonthYear());
        creditCardPay.operationWasApprovedByBank();
        var cardStatus = getCreditCardStatus();
        assertEquals("APPROVED", cardStatus);
    }

    // Карта со статусом DECLINED, остальные поля заполнены валидными данными
    @Test
    public void testCreditPayInValidCard() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getCardInValidValuesNextMonthYear());
        creditCardPay.bankRefusedTocCarryOutOperation();
        var cardStatus = getCreditCardStatus();
        assertEquals("DECLINED", cardStatus);
    }

    // Карта со статусом APPROVED, поле номер карты заполнено не полностью. Остальные поля валидными данными
    @Test
    public void testCreditPayIncompleteCard() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getIncompleteNumberCardValuesMonthYear());
        creditCardPay.invalidFormat();
    }

    // Поле номер карты не заполнено. Остальные поля валидными данными
    @Test
    public void testCreditPayEmptyCard() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getEmptyNumberCardValuesMonthYear());
        creditCardPay.fieldIsRequiredToFillIn();
    }

    // Поле номер месяца заполнено одной цифрой, остальные поля заполнены валидными данными
    @Test
    public void testCreditPayIncompleteMonth() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getValidCardValuesInvalidMonthYear());
        creditCardPay.invalidFormat();
    }

    // Поле номер месяца заполнено двумя нулями
    @Test
    public void testCreditPayTwoZeroMonth() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getValidCardValuesTwoZeroMonthYear());
        creditCardPay.validityPeriodOfCardIsSpecifiedIncorrectly();
    }

    // Поле номер месяца заполнено числом больше 12
    @Test
    public void testCreditPayUnrealMonth() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getValidCardValuesUnrealMonthYear());
        creditCardPay.validityPeriodOfCardIsSpecifiedIncorrectly();
    }

    // Поле номер месяца заполнен месяцем меньше текущего, будущего года
    @Test
    public void testCreditPayMinusMonthNextYear() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getCardValidValuesMinusMonthNextYear());
        creditCardPay.operationWasApprovedByBank();
        var cardStatus = getCreditCardStatus();
        assertEquals("APPROVED", cardStatus);
    }

    // Поле номер месяца не заполнен
    @Test
    public void testCreditPayEmptyMonthCurrentYear() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getCardValidValuesEmptyMonthNextYear());
        creditCardPay.fieldIsRequiredToFillIn();
    }

    // Поле год не заполнено
    @Test
    public void testCreditPayMonthEmptyYear() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getCardValidValuesMonthEmptyYear());
        creditCardPay.fieldIsRequiredToFillIn();
    }

    // Поле год заполнено двумя нулями
    @Test
    public void testCreditPayMonthTwoZeroYear() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getCardValidValuesMonthTwoZeroYear());
        creditCardPay.cardExpired();
    }

    // Поле год заполнено +6 лет к текущему году
    @Test
    public void testCreditPayMonthNextSixYear() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getCardValidValuesMonthNextSixYear());
        creditCardPay.validityPeriodOfCardIsSpecifiedIncorrectly();
    }

    // Поле владелец заполнено кириллицей
    @Test
    public void testCreditPayMonthYearHolderOfKirillitsa() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getKirrilitsaHolderCardValidValuesMonthYear());
        creditCardPay.invalidFormat();
    }

    // Поле владелец не заполнено
    @Test
    public void testCreditPayMonthYearEmptyHolder() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getEmptyHolderCardValidValuesMonthYear());
        creditCardPay.fieldIsRequiredToFillIn();
    }

    // Поле владелец заполнено не полностью
    @Test
    public void testCreditPayMonthYearOneWordHolder() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getOneWordHolderCardValidValuesMonthYear());
        creditCardPay.invalidFormat();
    }

    // Поле владелец заполнено одним символом на латинице
    @Test
    public void testCreditPayMonthYearOneCharacterHolder() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getOneCharacterHolderCardValidValuesMonthYear());
        creditCardPay.invalidFormat();
    }

    // Поле владелец заполнено цифрами
    @Test
    public void testCreditPayMonthYearNumbersHolder() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getNumbersHolderCardValidValuesMonthYear());
        creditCardPay.invalidFormat();
    }

    // Поле владелец заполнено спец символами
    @Test
    public void testCreditPayMonthYearCharactersHolder() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getCharacterHolderCardValidValuesMonthYear());
        creditCardPay.invalidFormat();
    }

    // Поле CVC/CVV
    @Test
    public void testCreditPayMonthYearHolderZeroCvc() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getZeroCvcCardValidValuesMonthYear());
        creditCardPay.invalidFormat();
    }

    // Поле CVC/CVV заполнено двумя цифрами
    @Test
    public void testCreditPayMonthYearHolderTwoNumbersCvc() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getTwoNumberCvcCardValidValuesMonthYear());
        creditCardPay.invalidFormat();
    }

    // Поле CVC/CVV не заполнено , остальные поля заполнены валидными данными
    @Test
    public void testCreditPayMonthYearHolderEmptyCvc() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getEmptyCvcCardValidValuesMonthYear());
        creditCardPay.invalidFormat();
    }

}