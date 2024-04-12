package ru.netology;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.netology.data.Api.*;
import static ru.netology.data.BankCardDataHelper.*;

public class ApiTest {

    // Проверка карты со статусом "APPROVED" форма оплата по карте
    @Test
    void cardBuyApproved() {
        val approvedCard = getCardValidValuesCurrentMonthYear();
        val status = buyForm(approvedCard);
        assertTrue(status.contains("APPROVED"));
    }

    // Проверка карты со статусом "DECLINED" форма оплата по карте
    @Test
    void cardBuyDeclined() {
        val approvedCard = getCardInValidValuesNextMonthYear();
        val status = buyForm(approvedCard);
        assertTrue(status.contains("DECLINED"));
    }

    // Проверка карты со статусом "APPROVED" форма "Кредит по данным карты"
    @Test
    void cardBuyCreditApproved() {
        val declinedCard = getCardValidValuesCurrentMonthYear();
        val status = creditForm(declinedCard);
        assertTrue(status.contains("APPROVED"));
    }

    // Проверка карты со статусом "DECLINED" форма "Кредит по данным карты"
    @Test
    void cardBuyCreditDeclined() {
        val declinedCard = getCardInValidValuesNextMonthYear();
        val status = creditForm(declinedCard);
        assertTrue(status.contains("DECLINED"));
    }
}