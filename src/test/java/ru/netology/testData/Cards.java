package ru.netology.testData;

import lombok.Data;

@Data
public class Cards {
    private String number;
    private String month;
    private String year;
    private String cardholder;
    private String cvv;
}
