package ru.netology.testdata;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));

    public static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getUnknownCardNumber() {
        return faker.number().digits(16);
    }

    public static String getWrongCardNumber() {
        return faker.number().digits(15);
    }

    public static String getCardMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getCardYear() {
        return LocalDate.now().plusYears(2).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getCardHolder() {
        return faker.name().fullName().toUpperCase();
    }

    public static String getCardCode() {
        return faker.number().digits(3);
    }

    public static String getDoubleZeroData() {
        return "00";
    }

    public static String getWrongCardMonthTwoNumbers() {
        return String.valueOf(faker.number().numberBetween(13, 99));
    }

    public static String getWrongCardMonthOneNumber() {
        return String.valueOf(faker.number().numberBetween(0, 9));
    }

    public static String getWrongCardYear() {
        return LocalDate.now().plusYears(6).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getWrongCardYearOneNumber() {
        return String.valueOf(faker.number().numberBetween(0, 9));
    }

    public static String getLocalYear() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getCardMonthExpired() {
        return LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getCardYearExpired() {
        return LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getWrongCardHolder() {
        return faker.name().firstName().toUpperCase();
    }

    public static String getCyrillicCardHolder() {
        final Faker faker = new Faker(new Locale("ru"));
        return faker.name().fullName();
    }

    public static String getNumbersCardHolder() {
        return String.valueOf(faker.number().numberBetween(0, 99999));
    }

    public static String getOneNumberCvc() {
        return String.valueOf(faker.number().numberBetween(0, 9));
    }

    public static String getTwoNumbersCvc() {
        return String.valueOf(faker.number().numberBetween(10, 99));
    }

    public static Cards approvedCardFormData() {
        return new Cards(getApprovedCardNumber(), getCardMonth(), getCardYear(), getCardHolder(), getCardCode());
    }

    public static Cards declinedCardFormData() {
        return new Cards(getDeclinedCardNumber(), getCardMonth(), getCardYear(), getCardHolder(), getCardCode());
    }

    public static Cards unknownCardFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonth(), getCardYear(), getCardHolder(), getCardCode());
    }

    public static Cards wrongCardNumberFormData() {
        return new Cards(getWrongCardNumber(), getCardMonth(), getCardYear(), getCardHolder(), getCardCode());
    }

    public static Cards wrongCardMonthFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonthExpired(), getLocalYear(), getCardHolder(), getCardCode());
    }

    public static Cards wrongCardMonthOneNumberFormData() {
        return new Cards(getUnknownCardNumber(), getWrongCardMonthOneNumber(), getCardYear(), getCardHolder(), getCardCode());
    }

    public static Cards wrongCardMonthTwoNumbersFormData() {
        return new Cards(getUnknownCardNumber(), getWrongCardMonthTwoNumbers(), getCardYear(), getCardHolder(), getCardCode());
    }

    public static Cards wrongCardYearFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonth(), getWrongCardYear(), getCardHolder(), getCardCode());
    }

    public static Cards wrongCardYearFormatFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonth(), getWrongCardYearOneNumber(), getCardHolder(), getCardCode());
    }

    public static Cards expiredCardYearFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonth(), getCardYearExpired(), getCardHolder(), getCardCode());
    }

    public static Cards wrongCardHolderFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonth(), getCardYear(), getWrongCardHolder(), getCardCode());
    }

    public static Cards cyrillicCardHolderFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonth(), getCardYear(), getCyrillicCardHolder(), getCardCode());
    }

    public static Cards numbersCardHolderFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonth(), getCardYear(), getNumbersCardHolder(), getCardCode());
    }

    public static Cards specSymbolsCardHolderFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonth(), getCardYear(), "!@#$%^&%:?", getCardCode());
    }

    public static Cards oneNumberCardCodeFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonth(), getCardYear(), getCardHolder(), getOneNumberCvc());
    }

    public static Cards twoNumbersCardCodeFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonth(), getCardYear(), getCardHolder(), getTwoNumbersCvc());
    }

    public static Cards emptyCardNumberFormData() {
        return new Cards("", getCardMonth(), getCardYear(), getCardHolder(), getCardCode());
    }

    public static Cards emptyCardMonthFormData() {
        return new Cards(getUnknownCardNumber(), "", getCardYear(), getCardHolder(), getCardCode());
    }

    public static Cards emptyCardYearFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonth(), "", getCardHolder(), getCardCode());
    }

    public static Cards emptyCardHolderFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonth(), getCardYear(), "", getCardCode());
    }

    public static Cards emptyCardCodeFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonth(), getCardYear(), getCardHolder(), "");
    }

    public static Cards emptyFieldsFormData() {
        return new Cards("", "", "", "", "");
    }

    public static Cards doubleZeroMonthFormData() {
        return new Cards(getUnknownCardNumber(), getDoubleZeroData(), getCardYear(), getCardHolder(), getCardCode());
    }

    public static Cards doubleZeroYearFormData() {
        return new Cards(getUnknownCardNumber(), getCardMonth(), getDoubleZeroData(), getCardHolder(), getCardCode());
    }
}
