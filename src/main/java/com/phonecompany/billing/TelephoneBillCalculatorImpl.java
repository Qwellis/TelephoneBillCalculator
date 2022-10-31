package com.phonecompany.billing;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {

    public String phoneLog = "420774577453,13-01-2020 07:54:00,13-01-2020 08:03:00";
    private final LocalTime startInterval = LocalTime.of(8, 0, 0);
    private final LocalTime endInterval = LocalTime.of(16, 0, 0);
    private double priceForCall;

    public double getPriceForCall() {
        return priceForCall;
    }

    public LocalTime getStartedCall() {
        String[] phoneLogArr = phoneLog.split(",");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime startedCall = LocalDateTime.parse(phoneLogArr[1], formatter);
        return startedCall.toLocalTime();
    }

    public LocalTime getEndedCall() {
        String[] phoneLogArr = phoneLog.split(",");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime endedCall = LocalDateTime.parse(phoneLogArr[2], formatter);
        return endedCall.toLocalTime();
    }

    public Boolean isLongerPeriod() {
        Duration callTime = Duration.between(getStartedCall(), getEndedCall());
        long callTimeInSeconds = callTime.toSeconds();
        return callTimeInSeconds > 300;

    }

    public void intervalTest() {
        if (isLongerPeriod()) {

        }

        //8:00 - 16:00
        if (getStartedCall().isAfter(startInterval) && (getEndedCall().isBefore(endInterval))) {
            Duration callTime = Duration.between(getStartedCall(), getEndedCall());
            double callInMinutes = callTime.toSeconds();
            callInMinutes = callInMinutes / 60;
            callInMinutes = Math.ceil(callInMinutes);
            priceForCall += callInMinutes;
            System.out.println("The price for this call is: " + priceForCall + " Kč");

            // začatek hovoru před 8:00 && konec hovoru po 8:00
        } else if ((getStartedCall().isBefore(startInterval) && getEndedCall().isAfter(startInterval)) ||
                // začatek hovoru po 8:00 && začátek hovoru je před 16:00 && konec hovoru je po 8:00
                ((getStartedCall().isAfter(startInterval)) && (getStartedCall().isBefore(endInterval)) && (getEndedCall().isAfter(startInterval)))) {

            //začatek hovoru před 8:00
            if (getStartedCall().isBefore(startInterval)) {
                //interval od začatku hovoru do 8:00 hod
                Duration callTime = Duration.between(getStartedCall(), startInterval);
                double callInMinutes = callTime.toSeconds();
                callInMinutes = callInMinutes / 60;
                callInMinutes = Math.ceil(callInMinutes);
                priceForCall = priceForCall + (callInMinutes * 0.5);

                //interval od 8:00 do konce hovoru
                Duration callTime8_16 = Duration.between(startInterval, getEndedCall());
                double callInMinutes8_16 = callTime8_16.toSeconds();
                callInMinutes8_16 = callInMinutes8_16 / 60;
                callInMinutes8_16 = Math.ceil(callInMinutes8_16);
                priceForCall += + callInMinutes8_16;
                System.out.println("The price for this call is: " + priceForCall + " Kč");

            }
        }
    }

    @Override
    public BigDecimal calculate(String phoneLog) {
        return null;
    }
}











