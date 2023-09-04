package ru.netology.testData;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Cards {
    private String number;
    private String month;
    private String year;
    private String cardholder;
    private String cvc;
}
