package ru.netology.data;

import com.github.javafaker.Faker;
import ru.netology.card.CardsBank;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class BankCardDataHelper {

    private static Faker faker = new Faker(new Locale("en"));

    private static String validCardNumber = "4444 4444 4444 4441";
    private static String invalidCardNumber = "4444 4444 4444 4442";

    private static String getValidHolder() {
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    private static String getValidCVC() {
        return faker.numerify("###");
    }

    public static String GetTodayDatePart(String pattern) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    // Карта со статусом APPROVED, Все поля заполнены валидными данными
    public static CardsBank getCardValidValuesCurrentMonthYear() {
        return new CardsBank(validCardNumber,
                GetTodayDatePart("MM"),
                GetTodayDatePart("yy"),
                getValidHolder(),
                getValidCVC());
    }

    // Карта со статусом DECLINED, остальные поля заполнены валидными данными
    public static CardsBank getCardInValidValuesNextMonthYear() {
        return new CardsBank(invalidCardNumber,
                GetTodayDatePart("MM"),
                GetTodayDatePart("yy"),
                getValidHolder(),
                getValidCVC());
    }

    // Поле номер карты заполнена не полностью, остальные поля заполнены валидными данными
    public static CardsBank getIncompleteNumberCardValuesMonthYear() {
        return new CardsBank("4444 4444 4444 444",
                GetTodayDatePart("MM"),
                LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy")),
                getValidHolder(),
                getValidCVC());
    }

    // Поле номер карты не заполнено, остальные поля заполнены валидными данными
    public static CardsBank getEmptyNumberCardValuesMonthYear() {
        return new CardsBank(" ",
                GetTodayDatePart("MM"),
                GetTodayDatePart("yy"),
                getValidHolder(),
                getValidCVC());
    }

    // Поле номер месяца заполнено одной цифрой, остальные поля заполнены валидными данными
    public static CardsBank getValidCardValuesInvalidMonthYear() {
        return new CardsBank(validCardNumber,
                "1",
                GetTodayDatePart("yy"),
                getValidHolder(),
                getValidCVC());
    }

    // Поле номер месяца заполнено двумя нулями, остальные поля заполнены валидными данными
    public static CardsBank getValidCardValuesTwoZeroMonthYear() {
        return new CardsBank(validCardNumber,
                "00",
                GetTodayDatePart("yy"),
                getValidHolder(),
                getValidCVC());
    }

    // Поле номер месяца заполнено невалидным числом, остальные поля заполнены валидными данными
    public static CardsBank getValidCardValuesUnrealMonthYear() {
        return new CardsBank(validCardNumber,
                "13",
                GetTodayDatePart("yy"),
                getValidHolder(),
                getValidCVC());
    }

    // Поле месяц заполнено месяцем меньше текущего, будущего года, остальные данные валидные
    public static CardsBank getCardValidValuesMinusMonthNextYear() {
        return new CardsBank(validCardNumber,
                LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("MM")),
                LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy")),
                getValidHolder(),
                getValidCVC());
    }

    // Поле номер месяца не заполнено Все поля заполнены валидными данными, будущие месяц , год
    public static CardsBank getCardValidValuesEmptyMonthNextYear() {
        return new CardsBank(validCardNumber,
                "",
                LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy")),
                getValidHolder(),
                getValidCVC());
    }

    // Поле год не заполнено, все поля заполнены валидными данными
    public static CardsBank getCardValidValuesMonthEmptyYear() {
        return new CardsBank(validCardNumber,
                GetTodayDatePart("MM"),
                "",
                getValidHolder(),
                getValidCVC());
    }

    // Поле год заполнено двумя нулями, все поля заполнены валидными данными
    public static CardsBank getCardValidValuesMonthTwoZeroYear() {
        return new CardsBank(validCardNumber,
                GetTodayDatePart("MM"),
                "00",
                getValidHolder(),
                getValidCVC());
    }

    // Поле год заполнено прошедшим годом, все поля заполнены валидными данными
    public static CardsBank getCardValidValuesMonthLastYear() {
        return new CardsBank(validCardNumber,
                GetTodayDatePart("MM"),
                LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy")),
                getValidHolder(),
                getValidCVC());
    }

    // Поле год заполнено +6 лет, все поля заполнены валидными данными
    public static CardsBank getCardValidValuesMonthNextSixYear() {
        return new CardsBank(validCardNumber,
                GetTodayDatePart("MM"),
                LocalDate.now().plusYears(6).format(DateTimeFormatter.ofPattern("yy")),
                getValidHolder(),
                getValidCVC());
    }

    // Поле владелец заполнено Киррилицей, все поля заполнены валидными данными
    public static CardsBank getKirrilitsaHolderCardValidValuesMonthYear() {
        return new CardsBank(validCardNumber,
                GetTodayDatePart("MM"),
                LocalDate.now().plusYears(2).format(DateTimeFormatter.ofPattern("yy")),
                "Иван Иванов",
                getValidCVC());
    }

    // Поле владелец не заполнено , все поля заполнены валидными данными
    public static CardsBank getEmptyHolderCardValidValuesMonthYear() {
        return new CardsBank(validCardNumber,
                GetTodayDatePart("MM"),
                LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy")),
                "",
                getValidCVC());
    }

    // Поле владелец заполнено не полностью , все поля заполнены валидными данными
    public static CardsBank getOneWordHolderCardValidValuesMonthYear() {
        return new CardsBank(validCardNumber,
                GetTodayDatePart("MM"),
                LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy")),
                "Ivanov",
                getValidCVC());
    }

    // Поле владелец заполнено не полностью , все поля заполнены валидными данными
    public static CardsBank getOneCharacterHolderCardValidValuesMonthYear() {
        return new CardsBank(validCardNumber,
                GetTodayDatePart("MM"),
                GetTodayDatePart("yy"),
                "I",
                getValidCVC());
    }

    // Поле владелец заполнено цифрами , все поля заполнены валидными данными
    public static CardsBank getNumbersHolderCardValidValuesMonthYear() {
        return new CardsBank(validCardNumber,
                GetTodayDatePart("MM"),
                GetTodayDatePart("yy"),
                "123645",
                getValidCVC());
    }

    // Поле владелец заполнено спец символами , все поля заполнены валидными данными
    public static CardsBank getCharacterHolderCardValidValuesMonthYear() {
        return new CardsBank(validCardNumber,
                GetTodayDatePart("MM"),
                GetTodayDatePart("yy"),
                "*-06; ",
                getValidCVC());
    }

    // Поле cvc/cw заполнено нулями , все поля заполнены валидными данными
    public static CardsBank getZeroCvcCardValidValuesMonthYear() {
        return new CardsBank(validCardNumber,
                GetTodayDatePart("MM"),
                GetTodayDatePart("yy"),
                getValidHolder(),
                "000");
    }

    // Поле cvc/cw заполнено нулями , все поля заполнены валидными данными
    public static CardsBank getTwoNumberCvcCardValidValuesMonthYear() {
        return new CardsBank(validCardNumber,
                GetTodayDatePart("MM"),
                GetTodayDatePart("yy"),
                getValidHolder(),
                "02");
    }

    // Поле cvc/cw заполнено нулями , все поля заполнены валидными данными
    public static CardsBank getEmptyCvcCardValidValuesMonthYear() {
        return new CardsBank(validCardNumber,
                GetTodayDatePart("MM"),
                GetTodayDatePart("yy"),
                getValidHolder(),
                "");
    }
}