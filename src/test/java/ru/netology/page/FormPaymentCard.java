package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.card.CardsBank;

import java.time.Duration;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;


public class FormPaymentCard {
    public static SelenideElement formOfPaymentByCard = $x("//h3[(text()='Оплата по карте')]");
    public static SelenideElement formOfCreditPaymentByCard = $x("//h3[(text()='Кредит по данным карты')]");

    public static SelenideElement numberOfCard = $ ("[placeholder='0000 0000 0000 0000']");
    public static SelenideElement month = $ ("[placeholder='08']");
    public static SelenideElement year = $ ("[placeholder='22']");
    public static SelenideElement holder = $x("//form//span[text()='Владелец']//..//input");
    public static SelenideElement cvc = $ ("[placeholder='999']");

    public static SelenideElement battonContinue = $x("//form//button//span[text()='Продолжить']/../..");

    public static SelenideElement successNotification =  $x("//div[contains(@class, 'notification_status_ok')]");
    public static SelenideElement errorNotification = $x("//div[contains(@class, 'notification_status_error')]");

    public static SelenideElement successNotificationCloseButton = successNotification.$x("./button");
    public static SelenideElement errorNotificationCloseButton = errorNotification.$x("./button");


    public FormPaymentCard(){

    }

    public void buyForm(CardsBank cardsBank){
        numberOfCard.setValue(cardsBank.getNumber());
        month.setValue(cardsBank.getMonth());
        year.setValue(cardsBank.getYear());
        holder.setValue(cardsBank.getHolder());
        cvc.setValue(cardsBank.getCvc());
        battonContinue.click();
    }

    public void operationWasApprovedByBank(){

        successNotification.shouldBe(visible, Duration.ofSeconds(15));
        successNotificationCloseButton.click();

        successNotification.shouldBe(hidden);
    }

    public void bankRefusedTocCarryOutOperation(){
        errorNotification.shouldBe(visible, Duration.ofSeconds(15));
        errorNotificationCloseButton.click();
        errorNotification.shouldBe(hidden);
    }

    public void invalidFormat(){
        $(byText("Неверный формат")).shouldBe(visible,Duration.ofSeconds(15));
    }
    public void fieldIsRequiredToFillIn(){
        $(byText("Поле обязательно для заполнения")).shouldBe(visible,Duration.ofSeconds(15));
    }
    public void validityPeriodOfCardIsSpecifiedIncorrectly(){
        $(byText("Неверно указан срок действия карты")).shouldBe(visible, Duration.ofSeconds(15));
    }
    public void cardExpired(){
        $(byText("Истёк срок действия карты")).shouldBe(visible, Duration.ofSeconds(15));
    }

}