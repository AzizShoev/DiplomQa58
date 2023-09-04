package ru.netology.pages;

import com.codeborne.selenide.*;
import org.openqa.selenium.By;
import ru.netology.testData.Cards;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;


public class StartPage {
    private String host = System.getProperty("test.host");
    private SelenideElement header = $(By.cssSelector("h2.heading"));
    private SelenideElement payButton = $(byText("Купить")).parent().parent();
    private SelenideElement creditButton = $(byText("Купить в кредит")).parent().parent();
    private SelenideElement continueButton = $(byText("Продолжить")).parent().parent();
    private SelenideElement cardNumberField = $(byText("Номер карты")).parent();
    private SelenideElement monthField = $(byText("Месяц")).parent();
    private SelenideElement yearField = $(byText("Год")).parent();
    private SelenideElement ownerField = $(byText("Владелец")).parent();
    private SelenideElement cardCodeField = $(byText("CVC/CVV")).parent();
    private SelenideElement notificationApproved = $(".notification_status_ok ");
    private SelenideElement notificationDeclined = $(".notification_status_error");

    public StartPage() {
        open(host);
        header.shouldBe(visible);
    }

    public void inputData(Cards card) { //заполнить поля + клик продолжить
        cardNumberField.$(".input__control").setValue(card.getNumber());
        monthField.$(".input__control").setValue(card.getMonth());
        yearField.$(".input__control").setValue(card.getYear());
        ownerField.$(".input__control").setValue(card.getCardholder());
        cardCodeField.$(".input__control").setValue(card.getCvc());
        continueButton.click();
    }

    public static StartPage payButtonClick() {  //купить
        StartPage payPage = page(StartPage.class);
        payPage.payButton.click();
        return payPage;
    }

    public static StartPage creditPayButtonClick() {    //купить в кредит
        StartPage creditPage = page(StartPage.class);
        creditPage.creditButton.click();
        return creditPage;
    }

    public void checkNotificationApprovedVisible() {    //плашка ОК
        notificationApproved.shouldBe(visible, Duration.ofMillis(15000));
    }

    public void checkNotificationDeclinedVisible() {    //плашка Отказ
        notificationDeclined.shouldBe(visible, Duration.ofMillis(15000));
    }
    public void cardNumberFieldSubMessage(String subMessage) {  //ошибка номер карты
        cardNumberField.$(".input__sub").shouldHave(visible, exactText(subMessage));
    }

    public void cardMonthFieldSubMessage(String subMessage) {   //ошибка месяц
        monthField.$(".input__sub").shouldHave(visible, exactText(subMessage));
    }

    public void cardYearFieldSubMessage(String subMessage) {    //ошибка год
        yearField.$(".input__sub").shouldHave(visible, exactText(subMessage));
    }

    public void cardHolderFieldSubMessage(String subMessage) {  //ошибка владелец
        ownerField.$(".input__sub").shouldHave(visible, exactText(subMessage));
    }

    public void cardCVVFieldSubMessage(String subMessage) {     //ошибка cvc
        cardCodeField.$(".input__sub").shouldHave(visible, exactText(subMessage));
    }

}
