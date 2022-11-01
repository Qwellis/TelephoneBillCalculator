package com.phonecompany.billing;

public class ApplicationMain {
    public static void main(String[] args) {

        TelephoneBillCalculatorImpl calculator = new TelephoneBillCalculatorImpl();
        System.out.println(calculator.calculate("420774577453,13-01-2020 07:50:00,13-01-2020 07:59:00"));



    }
}
