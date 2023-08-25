package ru.netology.test;


import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.netology.pages.StartPage;
import ru.netology.testData.DataGenerator;
import ru.netology.testData.SqlHelper;

import javax.xml.crypto.Data;
import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.*;

public class CashTests {
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
    @DisplayName("1. Купить. Успешная оплата разрешённой картой.")
    void shouldConfirmPayWithApprovedCard() {
        StartPage page = StartPage.payButtonClick();
        page.inputData(DataGenerator.getApprovedCard());
        page.continueButtonClick();
        page.checkNotificationApprovedVisible();
        assertEquals("APPROVED", SqlHelper.getOperationStatus(SqlHelper.getPayTable()));
    }

    @Test
    @DisplayName("2. Купить. Отказ в оплате запрещённой картой.")
    void shouldNotConfirmPayWithDeclinedCard() {
        StartPage page = StartPage.payButtonClick();
        page.inputData(DataGenerator.getDeclinedCard());
        page.continueButtonClick();
        page.checkNotificationDeclinedVisible();
        assertEquals("DECLINED", SqlHelper.getOperationStatus(SqlHelper.getPayTable()));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/invalidCardNumbers.csv")
    @DisplayName("3. Купить. Отказ. Номер карты валиден, но отсутствует в базе данных.")
    void shouldNotConfirmPayWithWrongCard(String number) {
        StartPage page = StartPage.payButtonClick();
        page.inputData(DataGenerator.getDeclinedCard());
        page.cleanCardNumberFieldAndInputNewData(number);
        page.continueButtonClick();
        page.checkNotificationDeclinedVisible();
        assertEquals("DECLINED", SqlHelper.getOperationStatus(SqlHelper.getPayTable()));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/wrongCardNumbers.csv")
    @DisplayName("4. Купить. Оплата проходит после исправления номера карты на валидный.")
    void shouldPayAfterCorrectingCardNumber(String number) {
        StartPage page = StartPage.payButtonClick();
        page.inputData(DataGenerator.getDeclinedCard());
        page.cleanCardNumberFieldAndInputNewData(number);
        page.continueButtonClick();
        page.checkCardNumberFormatErrorHave();
        page.cleanCardNumberFieldAndInputNewData(DataGenerator.approvedCardNumber());
        page.continueButtonClick();
        page.checkNotificationApprovedVisible();
        assertEquals("APPROVED", SqlHelper.getOperationStatus(SqlHelper.getPayTable()));
    }

    @Test
    @DisplayName("5. Купить. Оплата не происходит. Карта просрочена на месяц.")
    void shouldPayWithCardPreviousMonth() {
        StartPage page = StartPage.payButtonClick();
        page.inputData(DataGenerator.getApprovedCard());
        page.cleanMonthFieldAndInputNewData(DataGenerator.getShiftedMMFromCurrent(-1));
        page.cleanYearFieldAndInputNewData(DataGenerator.getShiftedYYFromCurrentByMonth(-1));
        page.continueButtonClick();
        page.checkMonthDateErrorHave();
    }

    @Test
    @DisplayName("6. Купить. Оплата не происходит. Карта просрочена на год.")
    void shouldPayWithCardPreviousYear() {
        StartPage page = StartPage.payButtonClick();
        page.inputData(DataGenerator.getApprovedCard());
        page.cleanMonthFieldAndInputNewData(DataGenerator.getShiftedMMFromCurrent(0));
        page.cleanYearFieldAndInputNewData(DataGenerator.getShiftedYYFromCurrent(-1));
        page.continueButtonClick();
        page.checkYearDateErrorHave();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/wrongMonth.csv")
    @DisplayName("7. Купить. Оплата проходит после исправления месяца.")
    void shouldPayAfterCorrectionMonth(String month) {
        StartPage page = StartPage.payButtonClick();
        page.inputData(DataGenerator.getApprovedCard());
        page.cleanMonthFieldAndInputNewData(month);
        page.continueButtonClick();
        page.checkMonthFormatErrorHave();
        page.cleanMonthFieldAndInputNewData("12");
        page.continueButtonClick();
        page.checkNotificationApprovedVisible();
        assertEquals("APPROVED", SqlHelper.getOperationStatus(SqlHelper.getPayTable()));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/wrongYear.csv")
    @DisplayName("8. Купить. Оплата проходит после исправления года")
    void shouldPayAfterCorrectionWrongYear(String year) {
        StartPage page = StartPage.payButtonClick();
        page.inputData(DataGenerator.getApprovedCard());
        page.cleanYearFieldAndInputNewData(year);
        page.continueButtonClick();
        page.checkYearFormatErrorHave();
        page.cleanYearFieldAndInputNewData(DataGenerator.getShiftedYYFromCurrent(2));
        page.continueButtonClick();
        page.checkNotificationApprovedVisible();
        assertEquals("APPROVED", SqlHelper.getOperationStatus(SqlHelper.getPayTable()));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/wrongCardholderName.csv")
    @DisplayName("9. Купить. Оплата происходит после исправления имени владельца")
    void shouldPayAfterCorrectionCardholderName(String owner) {
        StartPage page = StartPage.payButtonClick();
        page.inputData(DataGenerator.getApprovedCard());
        page.cleanOwnerFieldAndInputNewData(owner);
        page.continueButtonClick();
        page.checkOwnerFormatErrorHave();
        page.cleanOwnerFieldAndInputNewData(DataGenerator.generateCardholder());
        page.continueButtonClick();
        page.checkNotificationApprovedVisible();
        assertEquals("APPROVED", SqlHelper.getOperationStatus(SqlHelper.getPayTable()));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/wrongCVV.csv")
    @DisplayName("11. Купить. Оплата происходит после исправления CVC/CVV")
    void shouldPayProcessWithCorrectedCvc(String cvv) {
        StartPage page = StartPage.payButtonClick();
        page.inputData(DataGenerator.getApprovedCard());
        page.cleanCvcFieldAndInputNewData(cvv);
        page.continueButtonClick();
        page.checkCvcFormatErrorHave();
        page.cleanCvcFieldAndInputNewData(DataGenerator.generateCvc());
        page.continueButtonClick();
        page.checkNotificationApprovedVisible();
        assertEquals("APPROVED", SqlHelper.getOperationStatus(SqlHelper.getPayTable()));
    }
}
