package ru.netology.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.netology.data.ApiHelper.buyForm;
import static ru.netology.data.ApiHelper.creditForm;
import static ru.netology.data.BankCardDataHelper.getCardInValidValuesNextMonthYear;
import static ru.netology.data.BankCardDataHelper.getCardValidValuesCurrentMonthYear;

public class ApiTest {

    // Проверка карты со статусом "APPROVED" форма оплата по карте
    @Test
    void cardBuyApproved() {
        var approvedCard = getCardValidValuesCurrentMonthYear();
        var status = buyForm(approvedCard);
        assertTrue(status.contains("APPROVED"));
    }

    // Проверка карты со статусом "DECLINED" форма оплата по карте
    @Test
    void cardBuyDeclined() {
        var approvedCard = getCardInValidValuesNextMonthYear();
        var status = buyForm(approvedCard);
        assertTrue(status.contains("DECLINED"));
    }

    // Проверка карты со статусом "APPROVED" форма "Кредит по данным карты"
    @Test
    void cardBuyCreditApproved() {
        var declinedCard = getCardValidValuesCurrentMonthYear();
        var status = creditForm(declinedCard);
        assertTrue(status.contains("APPROVED"));
    }

    // Проверка карты со статусом "DECLINED" форма "Кредит по данным карты"
    @Test
    void cardBuyCreditDeclined() {
        var declinedCard = getCardInValidValuesNextMonthYear();
        var status = creditForm(declinedCard);
        assertTrue(status.contains("DECLINED"));
    }
}