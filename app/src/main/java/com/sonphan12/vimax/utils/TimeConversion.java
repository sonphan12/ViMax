package com.sonphan12.vimax.utils;

public class TimeConversion {
    public static String milisToFullTime(String milis) {
        if (milis == null) {
            return "88:88:88";
        }
        long mMilis = Long.parseLong(milis);
        long hours = (mMilis / 1000) / 3600;
        long minutes = ((mMilis / 1000) - hours * 3600) / 60;
        long seconds = (mMilis / 1000) - hours * 3600 - minutes * 60;

        String retHours = (hours < 10) ? ("0" + String.valueOf(hours)) : String.valueOf(hours);
        String retMinutes = (minutes < 10) ? ("0" + String.valueOf(minutes)) : String.valueOf(minutes);
        String retSeconds = (seconds < 10) ? ("0" + String.valueOf(seconds)) : String.valueOf(seconds);

        return retHours + ":" + retMinutes + ":" + retSeconds;

    }
}
