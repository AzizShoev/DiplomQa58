package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.netology.pages.StartPage;
import ru.netology.testData.DataGenerator;
import ru.netology.testData.SqlHelper;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTests {
    @BeforeAll
    static void setUpAll() throws SQLException {
        SqlHelper.cleanTables();
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterEach
    void cleanTables() throws SQLException {
        SqlHelper.cleanTables();
    }

    @Test
    @DisplayName("11. Купить в кредит. Успешная оплата кредитом с разрешённой картой.")
    void shouldConfirmPayWithApprovedCard() {
        StartPage page = StartPage.creditPayButtonClick();
        page.inputData(DataGenerator.getApprovedCard());
        page.continueButtonClick();
        page.checkNotificationApprovedVisible();
        assertEquals("APPROVED", SqlHelper.getOperationStatus(SqlHelper.getCreditTable()));
    }

    @Test
    @DisplayName("12. Купить в кредит. Отказ в оплате кредитом с запрещённой картой.")
    void shouldNotConfirmPayWithDeclinedCard() {
        StartPage page = StartPage.creditPayButtonClick();
        page.inputData(DataGenerator.getDeclinedCard());
        page.continueButtonClick();
        page.checkNotificationDeclinedVisible();
        assertEquals("DECLINED", SqlHelper.getOperationStatus(SqlHelper.getCreditTable()));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/invalidCardNumbers.csv")
    @DisplayName("13. Купить в кредит. Отказ. Номер карты валиден, но отсутствует в базе данных.")
    void shouldNotConfirmPayWithWrongCard(String number) {
        StartPage page = StartPage.creditPayButtonClick();
        page.inputData(DataGenerator.getDeclinedCard());
        page.cleanCardNumberFieldAndInputNewData(number);
        page.continueButtonClick();
        page.checkNotificationDeclinedVisible();
        assertEquals("DECLINED", SqlHelper.getOperationStatus(SqlHelper.getCreditTable()));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/wrongCardNumbers.csv")
    @DisplayName("14. Купить в кредит. Оплата проходит после исправления номера карты на валидный")
    void shouldPayAfterCorrectingCardNumber(String number) {
        StartPage page = StartPage.creditPayButtonClick();
        page.inputData(DataGenerator.getDeclinedCard());
        page.cleanCardNumberFieldAndInputNewData(number);
        page.continueButtonClick();
        page.checkCardNumberFormatErrorHave();
        page.cleanCardNumberFieldAndInputNewData(DataGenerator.approvedCardNumber());
        page.continueButtonClick();
        page.checkNotificationApprovedVisible();
        assertEquals("APPROVED", SqlHelper.getOperationStatus(SqlHelper.getCreditTable()));
    }

    @Test
    @DisplayName("15. Купить в кредит. Оплата не происходит. Карта просрочена на месяц.")
    void shouldPayWithCardPreviousMonth() {
        StartPage page = StartPage.creditPayButtonClick();
        page.inputData(DataGenerator.getApprovedCard());
        page.cleanMonthFieldAndInputNewData(DataGenerator.getShiftedMMFromCurrent(-1));
        page.cleanYearFieldAndInputNewData(DataGenerator.getShiftedYYFromCurrentByMonth(-1));
        page.continueButtonClick();
        page.checkMonthDateErrorHave();
    }

    @Test
    @DisplayName("16. Купить в кредит. Оплата не происходит. Карта просрочена на год.")
    void shouldPayWithCardPreviousYear() {
        StartPage page = StartPage.creditPayButtonClick();
        page.inputData(DataGenerator.getApprovedCard());
        page.cleanMonthFieldAndInputNewData(DataGenerator.getShiftedMMFromCurrent(0));
        page.cleanYearFieldAndInputNewData(DataGenerator.getShiftedYYFromCurrent(-1));
        page.continueButtonClick();
        page.checkYearDateErrorHave();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/wrongMonth.csv")
    @DisplayName("17. Купить в кредит. Оплата проходит после исправления месяца.")
    void shouldPayAfterCorrectionMonth(String month) {
        StartPage page = StartPage.creditPayButtonClick();
        page.inputData(DataGenerator.getApprovedCard());
        page.cleanMonthFieldAndInputNewData(month);
        page.continueButtonClick();
        page.checkMonthFormatErrorHave();
        page.cleanMonthFieldAndInputNewData("12");
        page.continueButtonClick();
        page.checkNotificationApprovedVisible();
        assertEquals("APPROVED", SqlHelper.getOperationStatus(SqlHelper.getCreditTable()));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/wrongYear.csv")
    @DisplayName("18. Купить в кредит. Оплата проходит после исправления года.")
    void shouldPayAfterCorrectionWrongYear(String year) {
        StartPage page = StartPage.creditPayButtonClick();
        page.inputData(DataGenerator.getApprovedCard());
        page.cleanYearFieldAndInputNewData(year);
        page.continueButtonClick();
        page.checkYearFormatErrorHave();
        page.cleanYearFieldAndInputNewData(DataGenerator.getShiftedYYFromCurrent(2));
        page.continueButtonClick();
        page.checkNotificationApprovedVisible();
        assertEquals("APPROVED", SqlHelper.getOperationStatus(SqlHelper.getCreditTable()));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/wrongCardholderName.csv")
    @DisplayName("19. Купить в кредит. Оплата происходит после исправления имени владельца.")
    void shouldPayAfterCorrectionCardholderName(String owner) {
        StartPage page = StartPage.creditPayButtonClick();
        page.inputData(DataGenerator.getApprovedCard());
        page.cleanOwnerFieldAndInputNewData(owner);
        page.continueButtonClick();
        page.checkOwnerFormatErrorHave();
        page.cleanOwnerFieldAndInputNewData(DataGenerator.generateCardholder());
        page.continueButtonClick();
        page.checkNotificationApprovedVisible();
        assertEquals("APPROVED", SqlHelper.getOperationStatus(SqlHelper.getCreditTable()));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/wrongCVV.csv")
    @DisplayName("20. Купить в кредит. Оплата происходит после исправления CVC/CVV.")
    void shouldPayProcessWithCorrectedCvc(String cvv) {
        StartPage page = StartPage.creditPayButtonClick();
        page.inputData(DataGenerator.getApprovedCard());
        page.cleanCvcFieldAndInputNewData(cvv);
        page.continueButtonClick();
        page.checkCvcFormatErrorHave();
        page.cleanCvcFieldAndInputNewData(DataGenerator.generateCvc());
        page.continueButtonClick();
        page.checkNotificationApprovedVisible();
        assertEquals("APPROVED", SqlHelper.getOperationStatus(SqlHelper.getCreditTable()));
    }
}
