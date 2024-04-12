package ru.netology;

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

    private static HeadPage headPage;

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
        creditCardPay.InvalidFormat();
    }

    // Поле номер карты не заполнено. Остальные поля валидными данными
    @Test
    public void testCreditPayEmptyCard() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getEmptyNumberCardValuesMonthYear());
        creditCardPay.FieldIsRequiredToFillIn();
    }

    // Поле номер месяца заполнено одной цифрой, остальные поля заполнены валидными данными
    @Test
    public void testCreditPayIncompleteMonth() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getValidCardValuesInvalidMonthYear());
        creditCardPay.InvalidFormat();
    }

    // Поле номер месяца заполнено двумя нулями
    @Test
    public void testCreditPayTwoZeroMonth() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getValidCardValuesTwoZeroMonthYear());
        creditCardPay.ValidityPeriodOfCardIsSpecifiedIncorrectly();
    }

    // Поле номер месяца заполнено числом больше 12
    @Test
    public void testCreditPayUnrealMonth() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getValidCardValuesUnrealMonthYear());
        creditCardPay.ValidityPeriodOfCardIsSpecifiedIncorrectly();
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
        creditCardPay.FieldIsRequiredToFillIn();
    }

    // Поле год не заполнено
    @Test
    public void testCreditPayMonthEmptyYear() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getCardValidValuesMonthEmptyYear());
        creditCardPay.FieldIsRequiredToFillIn();
    }

    // Поле год заполнено двумя нулями
    @Test
    public void testCreditPayMonthTwoZeroYear() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getCardValidValuesMonthTwoZeroYear());
        creditCardPay.CardExpired();
    }

    // Поле год заполнено +6 лет к текущему году
    @Test
    public void testCreditPayMonthNextSixYear() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getCardValidValuesMonthNextSixYear());
        creditCardPay.ValidityPeriodOfCardIsSpecifiedIncorrectly();
    }

    // Поле владелец заполнено кириллицей
    @Test
    public void testCreditPayMonthYearHolderOfKirillitsa() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getKirrilitsaHolderCardValidValuesMonthYear());
        creditCardPay.InvalidFormat();
    }

    // Поле владелец не заполнено
    @Test
    public void testCreditPayMonthYearEmptyHolder() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getEmptyHolderCardValidValuesMonthYear());
        creditCardPay.FieldIsRequiredToFillIn();
    }

    // Поле владелец заполнено не полностью
    @Test
    public void testCreditPayMonthYearOneWordHolder() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getOneWordHolderCardValidValuesMonthYear());
        creditCardPay.InvalidFormat();
    }

    // Поле владелец заполнено одним символом на латинице
    @Test
    public void testCreditPayMonthYearOneCharacterHolder() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getOneCharacterHolderCardValidValuesMonthYear());
        creditCardPay.InvalidFormat();
    }

    // Поле владелец заполнено цифрами
    @Test
    public void testCreditPayMonthYearNumbersHolder() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getNumbersHolderCardValidValuesMonthYear());
        creditCardPay.InvalidFormat();
    }

    // Поле владелец заполнено спец символами
    @Test
    public void testCreditPayMonthYearCharactersHolder() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getCharacterHolderCardValidValuesMonthYear());
        creditCardPay.InvalidFormat();
    }

    // Поле CVC/CVV
    @Test
    public void testCreditPayMonthYearHolderZeroCvc() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getZeroCvcCardValidValuesMonthYear());
        creditCardPay.InvalidFormat();
    }

    // Поле CVC/CVV заполнено двумя цифрами
    @Test
    public void testCreditPayMonthYearHolderTwoNumbersCvc() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getTwoNumberCvcCardValidValuesMonthYear());
        creditCardPay.InvalidFormat();
    }

    // Поле CVC/CVV не заполнено , остальные поля заполнены валидными данными
    @Test
    public void testCreditPayMonthYearHolderEmptyCvc() {
        var headPage = new HeadPage();
        var creditCardPay = headPage.openPaymentByCreditPage();
        creditCardPay.buyForm(BankCardDataHelper.getEmptyCvcCardValidValuesMonthYear());
        creditCardPay.InvalidFormat();
    }

}