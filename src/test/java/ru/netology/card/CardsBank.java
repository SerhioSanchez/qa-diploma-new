package ru.netology.card;

public class CardsBank {
    private String number;
    private String month;
    private String year;
    private String holder;
    private String cvc;

    public CardsBank(String number, String month, String year, String holder, String cvc){
        this.number =number;
        this.month = month;
        this.year = year;
        this.holder = holder;
        this.cvc = cvc;
    }

    public String getNumber(){
        return number;
    }
    public String getMonth(){
        return month;
    }
    public String getYear(){
        return year;
    }
    public String getHolder(){
        return holder;
    }
    public String getCvc(){
        return cvc;
    }

}