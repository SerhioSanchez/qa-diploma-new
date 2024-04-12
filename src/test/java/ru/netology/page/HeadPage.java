package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class HeadPage {
  public static SelenideElement header = $x("//h2[text()='Путешествие дня']");
  public static SelenideElement payBattonDebit =$x("//button//span[text()='Купить']/../..");
  public static SelenideElement payBattonCredit = $x("//button//span[text()='Купить в кредит']/../..");

  public HeadPage(){
    header.shouldBe(visible);
    payBattonDebit.shouldBe(visible);
    payBattonCredit.shouldBe(visible);
  }

  public PageCreditPay openPaymentByCardPage(){
    payBattonDebit.click();
    return new PageCreditPay();
  }

  public PageDebitPay openPaymentByCreditPage(){
    payBattonCredit.click();
    return new PageDebitPay();
  }

}