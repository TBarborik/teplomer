/**
 * Třída představující jeden záznam teploty.
 *
 * Poslední modifikace 1.1. 2018
 * @Author Tom Barbořík (xbarbo06)
 */

package fit.imp.xbarbo06.thermometer;

import android.text.format.DateFormat;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;

public class Temperature {
    private float temp;
    private long timestamp;

    Temperature(float temp, long timestamp) {
        this.temp = temp;
        this.timestamp = timestamp;
    }

    public float getTemperature() {
        return this.temp;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || (!(obj instanceof Temperature))) return false;

        Temperature objT = (Temperature) obj;
        return this.timestamp == objT.getTimestamp(); // je-li změřeno ve stejný okamžik, pak stejná teplota.
    }

    @Override
    public String toString() {
        String toReturn = "";
        if (this.timestamp > 0) {
            long timestamp = this.timestamp / 1000; // milisekundy
            toReturn += Long.toString(timestamp % 60);  // vteřiny
            if ((timestamp % 60) < 10) toReturn = "0" + toReturn;

            timestamp = timestamp / 60; toReturn = Long.toString(timestamp % 60) + ":" + toReturn; // minuty
            if ((timestamp % 60) < 10) toReturn = "0" + toReturn;

            timestamp = timestamp / 60; toReturn = Long.toString(timestamp % 24) + ":" + toReturn; // hodiny
            if ((timestamp % 24) < 10) toReturn = "0" + toReturn;

            timestamp = timestamp / 24; // dny
            int days = (int) timestamp;

            if (days > 0) {
                switch (days) {
                    case 1: toReturn = Integer.toString(days) + " den " + toReturn; break;
                    case 2:case 3:case 4: toReturn = Integer.toString(days) + " dny " + toReturn; break;
                    default: toReturn = Integer.toString(days) + " dní " + toReturn; break;
                }
            }

            toReturn += " - ";

        }

        toReturn += (Float.valueOf(this.temp)).toString().concat(" °C");

        return toReturn;
    }
}
