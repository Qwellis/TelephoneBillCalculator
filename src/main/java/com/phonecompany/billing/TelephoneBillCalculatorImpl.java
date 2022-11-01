package com.phonecompany.billing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {

    public String phoneLog = "420774577453,13-01-2020 07:50:00,13-01-2020 07:59:00";
    private final LocalTime startInterval = LocalTime.of(8, 0, 0);
    private final LocalTime endInterval = LocalTime.of(16, 0, 0);
    private double priceForCall = 0.0;

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

    public boolean isLongerCall() {
        Duration callTime = Duration.between(getStartedCall(), getEndedCall());
        long callTimeInSeconds = callTime.toSeconds();
        return callTimeInSeconds > 300;
    }

    public double countLongerCall() {
        if (isLongerCall()) {
            Duration callTime = Duration.between(getStartedCall(), getEndedCall());
            long callTimeInSeconds = callTime.toSeconds();
            callTimeInSeconds -= 300;
            callTimeInSeconds = callTimeInSeconds / 60;
            double callTimeInMinutes = Math.ceil(callTimeInSeconds);
            callTimeInMinutes *= 0.2;
            priceForCall += callTimeInMinutes;
        }
        return priceForCall;
    }

    public Double countCall() {
        if (isLongerCall()) {
            countLongerCall();
        }

        //8:00 - 16:00
        if (getStartedCall().isAfter(startInterval) && (getEndedCall().isBefore(endInterval))) {
            Duration callTime = Duration.between(getStartedCall(), getEndedCall());
            double callInSeconds = callTime.toSeconds();
            if (callInSeconds < 300) {
                callInSeconds = callInSeconds / 60;
                double callInMinutes = Math.ceil(callInSeconds);
                priceForCall += callInMinutes; // ------------ 1 minuta hovoru = 1 Kč
            } else
                priceForCall += 5.0; // ------------------- K ceně hovoru nad rámec 5. minut se připočte 5 Kč za sazbu hovoru v intervalu 8-16 v prvních pěti minutách hovoru

            // začatek hovoru před 8:00 && konec hovoru po 8:00 || začatek hovoru po 8:00 && začátek hovoru je před 16:00 && konec hovoru je po 16:00
        } else if ((getStartedCall().isBefore(startInterval) && getEndedCall().isAfter(startInterval)) ||
                ((getStartedCall().isAfter(startInterval)) && (getStartedCall().isBefore(endInterval)) && (getEndedCall().isAfter(endInterval)))) {

            //začatek hovoru před 8:00
            if (getStartedCall().isBefore(startInterval)) {

                //interval od začatku hovoru do 8:00 hod
                Duration callTime = Duration.between(getStartedCall(), startInterval);
                double callInSeconds = callTime.toSeconds();
                double callInMinutes = callInSeconds / 60;
                callInMinutes = Math.ceil(callInMinutes);
                priceForCall = priceForCall + (callInMinutes * 0.5);

                //interval od 8:00 do konce hovoru
                Duration callTime8_16 = Duration.between(startInterval, getEndedCall());
                double callInSeconds8_16 = callTime8_16.toSeconds();
                double callInMinutes8_16 = callInSeconds8_16 / 60;
                callInMinutes8_16 = Math.ceil(callInMinutes8_16);
                priceForCall += callInMinutes8_16;
            }


            //začátek hovoru v intervalu 8:00 - 16:00 && konec hovoru po 16:00
            else if (getStartedCall().isAfter(startInterval) && getStartedCall().isBefore(endInterval) && getEndedCall().isAfter(endInterval)) {

                //cena hovoru v intervalu 8-16
                Duration callTime8_16 = Duration.between(getStartedCall(), endInterval);
                double callInMinutes8_16 = callTime8_16.toSeconds();
                callInMinutes8_16 = callInMinutes8_16 / 60;
                callInMinutes8_16 = Math.ceil(callInMinutes8_16);
                priceForCall += +callInMinutes8_16;

                //cena hovoru v intervalu po 16:00
                Duration callTime = Duration.between(endInterval, getEndedCall());
                double callInMinutes = callTime.toSeconds();
                callInMinutes = callInMinutes / 60;
                callInMinutes = Math.ceil(callInMinutes);
                priceForCall = priceForCall + (callInMinutes * 0.5);
            }
        }
        //začátek a konec hovoru mimo interval 8-16
        else if ((getStartedCall().isBefore(startInterval) && getEndedCall().isBefore(startInterval)) ||
                (getStartedCall().isAfter(endInterval) && getEndedCall().isAfter(endInterval))) {
            Duration callTime = Duration.between(getStartedCall(), getEndedCall());
            double callInSeconds = callTime.toSeconds();
            if (callInSeconds < 300) {
                double callInMinutes = callInSeconds / 60;
                callInMinutes = Math.ceil(callInMinutes);
                priceForCall = priceForCall + (callInMinutes * 0.5);
            } else priceForCall += 2.5;
        }
        return priceForCall;
    }

    @Override
    public Double calculate(String phoneLog) {
        return countCall();
    }
}











