package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.pages.StartPage;
import ru.netology.testData.DataGenerator;
import ru.netology.testData.SqlHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTests {
    private final String subMessageWrongFormat = "Неверный формат";
    private final String subMessageWrongDate = "Неверно указан срок действия карты";
    private final String subMessageEmptyData = "Поле обязательно для заполнения";
    private final String subMessageExpiredDate = "Истёк срок действия карты";
    static StartPage page = new StartPage();

    @BeforeAll
    static void setUpAll() {
        SqlHelper.cleanTables();
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        page = StartPage.creditPayButtonClick();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterEach
    void cleanTables() {
        SqlHelper.cleanTables();
    }

    @Test
    @DisplayName("25. Купить в кредит. Успешная оплата разрешённой (APPROVED) картой")
    void shouldConfirmPayWithApprovedCard() {
        page.inputData(DataGenerator.approvedCardFormData());
        page.checkNotificationApprovedVisible();
        assertEquals("APPROVED", SqlHelper.getCreditStatus());
    }

    @Test
    @DisplayName("26. Купить в кредит. Отказ в оплате запрещённой (DECLINED) картой")
    void shouldNotConfirmPayWithDeclinedCard() {
        page.inputData(DataGenerator.declinedCardFormData());
        page.checkNotificationDeclinedVisible();
        assertEquals("DECLINED", SqlHelper.getCreditStatus());
    }

    @Test
    @DisplayName("27. Купить в кредит. Отказ. Номер карты валиден, но не существует в системе")
    void shouldNotConfirmInvalidCard() {
        page.inputData(DataGenerator.unknownCardFormData());
        page.checkNotificationDeclinedVisible();
    }

    @Test
    @DisplayName("28. Купить в кредит. Отказ. Неверный формат номера карты")
    void shouldPayWrongFormatCardNumber() {
        page.inputData(DataGenerator.wrongCardNumberFormData());
        page.cardNumberFieldSubMessage(subMessageWrongFormat);
    }

    @Test
    @DisplayName("29. Купить в кредит. Оплата не происходит. Карта просрочена на месяц")
    void shouldNotPayProcessWithOverdueMonth() {
        page.inputData(DataGenerator.wrongCardMonthFormData());
        page.cardMonthFieldSubMessage(subMessageWrongDate);
    }

    @Test
    @DisplayName("30. Купить в кредит. Оплата не происходит. Карта просрочена на год")
    void shouldNotPayProcessWithOverdueYear() {
        page.inputData(DataGenerator.expiredCardYearFormData());
        page.cardYearFieldSubMessage(subMessageExpiredDate);
    }

    @Test
    @DisplayName("31. Купить в кредит. Оплата не происходит. В поле месяц значение от 0 до 9")
    void shouldNotPayProcessWithWrongMonthOneNumber() {
        page.inputData(DataGenerator.wrongCardMonthOneNumberFormData());
        page.cardMonthFieldSubMessage(subMessageWrongFormat);
    }

    @Test
    @DisplayName("32. Купить в кредит. Оплата не происходит. В поле месяц значение от 13 до 99")
    void shouldNotPayProcessWithWrongMonthTwoNumber() {
        page.inputData(DataGenerator.wrongCardMonthTwoNumbersFormData());
        page.cardMonthFieldSubMessage(subMessageWrongDate);
    }

    @Test
    @DisplayName("33. Купить в кредит. Оплата не происходит. В поле год указан год старше 6 лет")
    void shouldNotPayProcessWithOveredYear() {
        page.inputData(DataGenerator.wrongCardYearFormData());
        page.cardYearFieldSubMessage(subMessageWrongDate);
    }

    @Test
    @DisplayName("34. Купить в кредит. Оплата не происходит. В поле год указано значение от 0 до 9")
    void shouldNotPayProcessWithOneNumberYear() {
        page.inputData(DataGenerator.wrongCardYearFormatFormData());
        page.cardYearFieldSubMessage(subMessageWrongFormat);
    }

    @Test
    @DisplayName("35. Купить в кредит. Оплата не происходит. В поле месяц значение 00")
    void shouldNotPayProcessWithDoubleZeroMonth() {
        page.inputData(DataGenerator.doubleZeroMonthFormData());
        page.cardMonthFieldSubMessage(subMessageWrongDate);
    }

    @Test
    @DisplayName("36. Купить в кредит. Оплата не происходит. В поле год значение 00")
    void shouldNotPayProcessWithDoubleZeroYear() {
        page.inputData(DataGenerator.doubleZeroYearFormData());
        page.cardYearFieldSubMessage(subMessageExpiredDate);
    }

    @Test
    @DisplayName("37. Купить в кредит. Оплата не происходит. В поле Владелец только имя")
    void shouldNotPayProcessWithWrongCardHolder() {
        page.inputData(DataGenerator.wrongCardHolderFormData());
        page.cardHolderFieldSubMessage(subMessageWrongFormat);
    }

    @Test
    @DisplayName("38. Купить в кредит. Оплата не происходит. В поле Владелец данные кириллицей")
    void shouldNotPayProcessWithCyrilCardHolder() {
        page.inputData(DataGenerator.cyrillicCardHolderFormData());
        page.cardHolderFieldSubMessage(subMessageWrongFormat);
    }

    @Test
    @DisplayName("39. Купить в кредит. Оплата не происходит. В поле Владелец цифры")
    void shouldNotPayProcessWithNumbersCardHolder() {
        page.inputData(DataGenerator.numbersCardHolderFormData());
        page.cardHolderFieldSubMessage(subMessageWrongFormat);
    }

    @Test
    @DisplayName("40. Купить в кредит. Оплата не происходит. В поле Владелец спецсимволы")
    void shouldNotPayProcessWithSpecSymbolsCardHolder() {
        page.inputData(DataGenerator.specSymbolsCardHolderFormData());
        page.cardHolderFieldSubMessage(subMessageWrongFormat);
    }

    @Test
    @DisplayName("41. Купить в кредит. Оплата не происходит. В поле CVV одна цифра")
    void shouldNotPayProcessWithOneNumberCardCode() {
        page.inputData(DataGenerator.oneNumberCardCodeFormData());
        page.cardCVVFieldSubMessage(subMessageWrongFormat);
    }

    @Test
    @DisplayName("42. Купить в кредит. Оплата не происходит. В поле CVV две цифры")
    void shouldNotPayProcessWithTwoNumberCardCode() {
        page.inputData(DataGenerator.twoNumbersCardCodeFormData());
        page.cardCVVFieldSubMessage(subMessageWrongFormat);
    }

    @Test
    @DisplayName("43. Купить в кредит. Оплата не происходит. Пустое поле Номер карты")
    void shouldNotPayProcessWithEmptyCardNumber() {
        page.inputData(DataGenerator.emptyCardNumberFormData());
        page.cardNumberFieldSubMessage(subMessageEmptyData);
    }

    @Test
    @DisplayName("44. Купить в кредит. Оплата не происходит. Пустое поле Месяц")
    void shouldNotPayProcessWithEmptyMonth() {
        page.inputData(DataGenerator.emptyCardMonthFormData());
        page.cardMonthFieldSubMessage(subMessageEmptyData);
    }

    @Test
    @DisplayName("45. Купить в кредит. Оплата не происходит. Пустое поле Год")
    void shouldNotPayProcessWithEmptyYear() {
        page.inputData(DataGenerator.emptyCardYearFormData());
        page.cardYearFieldSubMessage(subMessageEmptyData);
    }

    @Test
    @DisplayName("46. Купить в кредит. Оплата не происходит. Пустое поле Владелец")
    void shouldNotPayProcessWithEmptyCardHolder() {
        page.inputData(DataGenerator.emptyCardHolderFormData());
        page.cardHolderFieldSubMessage(subMessageEmptyData);
    }

    @Test
    @DisplayName("47. Купить в кредит. Оплата не происходит. Пустое поле CVC/CVV")
    void shouldNotPayProcessWithEmptyCardCode() {
        page.inputData(DataGenerator.emptyCardCodeFormData());
        page.cardCVVFieldSubMessage(subMessageEmptyData);
    }

    @Test
    @DisplayName("48. Купить в кредит. Оплата не происходит. Пустые поля")
    void shouldNotPayProcessWithEmptyFields() {
        page.inputData(DataGenerator.emptyFieldsFormData());
        page.cardNumberFieldSubMessage(subMessageEmptyData);
        page.cardMonthFieldSubMessage(subMessageEmptyData);
        page.cardYearFieldSubMessage(subMessageEmptyData);
        page.cardHolderFieldSubMessage(subMessageEmptyData);
        page.cardCVVFieldSubMessage(subMessageEmptyData);
    }
}
