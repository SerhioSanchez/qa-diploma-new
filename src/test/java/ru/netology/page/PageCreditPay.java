package ru.netology.page;

import static com.codeborne.selenide.Condition.visible;

public class PageCreditPay extends FormPaymentCard {

  public PageCreditPay(){
    super();
    formOfCreditPaymentByCard.shouldBe(visible);

  }


}