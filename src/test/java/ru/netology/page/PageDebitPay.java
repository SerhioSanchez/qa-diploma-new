package ru.netology.page;

import static com.codeborne.selenide.Condition.visible;

public class PageDebitPay extends FormPaymentCard {

    public PageDebitPay() {
        super();
        formOfPaymentByCard.shouldBe(visible);
    }


}