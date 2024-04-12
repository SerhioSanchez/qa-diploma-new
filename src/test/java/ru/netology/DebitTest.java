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
import static ru.netology.data.SQLHelper.*;


public class DebitTest {
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
    public void testDebitPayValidCard() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getCardValidValuesCurrentMonthYear());
        debitCardPay.operationWasApprovedByBank();
        var cardStatus = getDebitCardStatus();
        assertEquals("APPROVED", cardStatus);

        var recordAmount = getDbRecordAmount();
        assertEquals(4500000, recordAmount);
    }

    // Карта со статусом DECLINED, остальные поля заполнены валидными данными
    @Test
    public void testDebitPayInValidCard() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getCardInValidValuesNextMonthYear());
        debitCardPay.bankRefusedTocCarryOutOperation();
        var cardStatus = getDebitCardStatus();
        assertEquals("DECLINED", cardStatus);
    }

    // Карта со статусом APPROVED, поле номер карты заполнено не полностью. Остальные поля валидными данными
    @Test
    public void testDebitPayIncompleteCard() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getIncompleteNumberCardValuesMonthYear());
        debitCardPay.InvalidFormat();
    }

    // Поле номер карты не заполнено. Остальные поля валидными данными
    @Test
    public void testDebitPayEmptyCard() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getEmptyNumberCardValuesMonthYear());
        debitCardPay.FieldIsRequiredToFillIn();
    }

    // Поле номер месяца заполнено одной цифрой, остальные поля заполнены валидными данными
    @Test
    public void testDebitPayIncompleteMonth() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getValidCardValuesInvalidMonthYear());
        debitCardPay.InvalidFormat();
    }

    // Поле номер месяца заполнено двумя нулями
    @Test
    public void testDebitPayTwoZeroMonth() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getValidCardValuesTwoZeroMonthYear());
        debitCardPay.ValidityPeriodOfCardIsSpecifiedIncorrectly();
    }

    // Поле номер месяца заполнено числом больше 12
    @Test
    public void testDebitPayUnrealMonth() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getValidCardValuesUnrealMonthYear());
        debitCardPay.ValidityPeriodOfCardIsSpecifiedIncorrectly();
    }

    // Поле номер месяца заполнен месяцем меньше текущего, будущего года
    @Test
    public void testDebitPayMinusMonthNextYear() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getCardValidValuesMinusMonthNextYear());
        debitCardPay.operationWasApprovedByBank();
        var cardStatus = getDebitCardStatus();
        assertEquals("APPROVED", cardStatus);

        var recordAmount = getDbRecordAmount();
        assertEquals(4500000, recordAmount);
    }

    // Поле номер месяца не заполнен
    @Test
    public void testDebitPayEmptyMonthCurrentYear() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getCardValidValuesEmptyMonthNextYear());
        debitCardPay.FieldIsRequiredToFillIn();
    }

    // Поле год не заполнено
    @Test
    public void testDebitPayMonthEmptyYear() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getCardValidValuesMonthEmptyYear());
        debitCardPay.FieldIsRequiredToFillIn();
    }

    // Поле год заполнено двумя нулями
    @Test
    public void testDebitPayMonthTwoZeroYear() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getCardValidValuesMonthTwoZeroYear());
        debitCardPay.CardExpired();
    }

    // Поле год заполнено +6 лет к текущему году
    @Test
    public void testDebitPayMonthNextSixYear() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getCardValidValuesMonthNextSixYear());
        debitCardPay.ValidityPeriodOfCardIsSpecifiedIncorrectly();
    }

    // Поле владелец заполнено киррилицей
    @Test
    public void testDebitPayMonthYearHolderOfKirrilitsa() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getKirrilitsaHolderCardValidValuesMonthYear());
        debitCardPay.InvalidFormat();
    }

    // Поле владелец не заполнено
    @Test
    public void testDebitPayMonthYearEmptyHolder() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getEmptyHolderCardValidValuesMonthYear());
        debitCardPay.FieldIsRequiredToFillIn();
    }

    // Поле владелец заполнено не полностью
    @Test
    public void testDebitPayMonthYearOneWordHolder() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getOneWordHolderCardValidValuesMonthYear());
        debitCardPay.InvalidFormat();
    }

    // Поле владелец заполнено одним символом на латинице
    @Test
    public void testDebitPayMonthYearOneCharacterHolder() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getOneCharacterHolderCardValidValuesMonthYear());
        debitCardPay.InvalidFormat();
    }

    // Поле владелец заполнено цифрами
    @Test
    public void testDebitPayMonthYearNumbersHolder() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getNumbersHolderCardValidValuesMonthYear());
        debitCardPay.InvalidFormat();
    }

    // Поле владелец заполнено спец символами
    @Test
    public void testDebitPayMonthYearCharactersHolder() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getCharacterHolderCardValidValuesMonthYear());
        debitCardPay.InvalidFormat();
    }

    // Поле CVC/CVV
    @Test
    public void testDebitPayMonthYearHolderZeroCvc() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getZeroCvcCardValidValuesMonthYear());
        debitCardPay.InvalidFormat();
    }

    // Поле CVC/CVV заполнено двумя цифрами
    @Test
    public void testDebitPayMonthYearHolderTwoNumbersCvc() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getTwoNumberCvcCardValidValuesMonthYear());
        debitCardPay.InvalidFormat();
    }

    // Поле CVC/CVV не заполнено , остальные поля заполнены валидными данными
    @Test
    public void testDebitPayMonthYearHolderEmptyCvc() {
        var headPage = new HeadPage();
        var debitCardPay = headPage.openPaymentByCardPage();
        debitCardPay.buyForm(BankCardDataHelper.getEmptyCvcCardValidValuesMonthYear());
        debitCardPay.FieldIsRequiredToFillIn();
    }

}