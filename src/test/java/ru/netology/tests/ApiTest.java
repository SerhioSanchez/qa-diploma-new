package ru.netology.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.netology.data.ApiHelper.buyForm;
import static ru.netology.data.ApiHelper.creditForm;
import static ru.netology.data.BankCardDataHelper.getCardInValidValuesNextMonthYear;
import static ru.netology.data.BankCardDataHelper.getCardValidValuesCurrentMonthYear;

public class ApiTest {

    @Test
    void cardBuyApproved() {
        var approvedCard = getCardValidValuesCurrentMonthYear();
        var status = buyForm(approvedCard);
        assertTrue(status.contains("APPROVED"));
    }

    @Test
    void cardBuyDeclined() {
        var declinedCard = getCardInValidValuesNextMonthYear();
        var status = buyForm(declinedCard);
        assertTrue(status.contains("DECLINED"));
    }

    @Test
    void cardCreditApproved() {
        var approvedCard = getCardValidValuesCurrentMonthYear();
        var status = creditForm(approvedCard);
        assertTrue(status.contains("APPROVED"));
    }

    @Test
    void cardCreditDeclined() {
        var declinedCard = getCardInValidValuesNextMonthYear();
        var status = creditForm(declinedCard);
        assertTrue(status.contains("DECLINED"));
    }
}
