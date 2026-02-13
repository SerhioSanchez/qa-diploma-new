package ru.netology.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.netology.data.ApiHelper.buyForm;
import static ru.netology.data.ApiHelper.creditForm;
import static ru.netology.data.BankCardDataHelper.getCardInValidValuesNextMonthYear;
import static ru.netology.data.BankCardDataHelper.getCardValidValuesCurrentMonthYear;

/**
 * API-тесты (через ApiHelper).
 * Минимальный набор для диплома: 2 статуса (APPROVED/DECLINED) × 2 формы (Оплата/Кредит).
 */
public class ApiTest {

    // Оплата по карте -> APPROVED
    @Test
    void cardPayApproved() {
        var card = getCardValidValuesCurrentMonthYear();
        var status = buyForm(card);
        assertTrue(status.contains("APPROVED"));
    }

    // Оплата по карте -> DECLINED
    @Test
    void cardPayDeclined() {
        var card = getCardInValidValuesNextMonthYear();
        var status = buyForm(card);
        assertTrue(status.contains("DECLINED"));
    }

    // Кредит по данным карты -> APPROVED
    @Test
    void creditPayApproved() {
        var card = getCardValidValuesCurrentMonthYear();
        var status = creditForm(card);
        assertTrue(status.contains("APPROVED"));
    }

    // Кредит по данным карты -> DECLINED
    @Test
    void creditPayDeclined() {
        var card = getCardInValidValuesNextMonthYear();
        var status = creditForm(card);
        assertTrue(status.contains("DECLINED"));
    }
}
