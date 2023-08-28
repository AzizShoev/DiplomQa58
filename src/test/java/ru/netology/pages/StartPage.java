package ru.netology.pages;

import com.codeborne.selenide.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.netology.testData.Cards;

import java.time.Duration;

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
    private SelenideElement cvcField = $(byText("CVC/CVV")).parent();
    private SelenideElement notificationApproved = $(".notification_status_ok ");
    private SelenideElement notificationDeclined = $(".notification_status_error");

    public StartPage() {
        open(host);
        header.shouldBe(visible);
    }

    public void inputData(Cards card) {
        cardNumberField.$(".input__control").setValue(card.getNumber());
        monthField.$(".input__control").setValue(card.getMonth());
        yearField.$(".input__control").setValue(card.getYear());
        ownerField.$(".input__control").setValue(card.getCardholder());
        cvcField.$(".input__control").setValue(card.getCvv());
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

    public void continueButtonClick() { //продолжить
        continueButton.click();
    }

    public void checkNotificationApprovedVisible() {    //плашка ОК
        notificationApproved.shouldBe(visible, Duration.ofMillis(15000));
    }

    public void checkNotificationDeclinedVisible() {    //плашка Отказ
        notificationDeclined.shouldBe(visible, Duration.ofMillis(15000));
    }

    public void cleanCardNumberFieldAndInputNewData(String data) {  //очистка и ввод новой карты
        cardNumberField.$(".input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        cardNumberField.$(".input__control").setValue(data);
    }

    public void cleanMonthFieldAndInputNewData(String data) {   //очистка и ввод нового месяца
        monthField.$(".input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        monthField.$(".input__control").setValue(data);
    }

    public void cleanYearFieldAndInputNewData(String data) {    //очистка и ввод нового года
        yearField.$(".input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        yearField.$(".input__control").setValue(data);
    }

    public void cleanOwnerFieldAndInputNewData(String data) {   //очистка и ввод нового CardHolder
        ownerField.$(".input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        ownerField.$(".input__control").setValue(data);
    }

    public void cleanCvcFieldAndInputNewData(String data) { //очистка и ввод нового CVV
        cvcField.$(".input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        cvcField.$(".input__control").setValue(data);
    }

    public void checkCardNumberFormatErrorHave() {  //ошибка поля карты
        cardNumberField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void checkMonthFormatErrorHave() {   //ошибка поля месяц
        monthField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void checkYearFormatErrorHave() {    //ошибка поля год
        yearField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void checkOwnerFormatErrorHave() {   //ошибка невалидного имени
        ownerField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void checkCvcFormatErrorHave() {     //ошибка невалидного CVV
        cvcField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void checkMonthDateErrorHave() { //ошибка невалидного месяца
        monthField.$(".input__sub").shouldHave(Condition.exactText("Неверно указан срок действия карты"));
    }

    public void checkYearDateErrorHave() {  //ошибка невалидного года
        yearField.$(".input__sub").shouldHave(Condition.exactText("Истёк срок действия карты"));
    }

    public void checkEmptyErrorHave() {   //пустые поля
        cardNumberField.$(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        monthField.$(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        yearField.$(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        ownerField.$(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        cvcField.$(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }
}
