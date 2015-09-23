package com.ensisa.ganesh.abluetoothdriver.fora.temperature;

import android.os.Bundle;

/**
 * Created by Ganesh on 5/29/2015.
 */
public class TemperatureAnalyzer {

    public static TemperatureClock analyseClock (byte [] reply) {

        String cData0 = String.format("%8s", Integer.toBinaryString(reply[2] & 0xFF)).replace(' ', '0');
        String cData1 = String.format("%8s", Integer.toBinaryString(reply[3] & 0xFF)).replace(' ', '0');

        String cbDay = cData0.substring(3, 8);
        String cbMonth = cData1.charAt(7) + cData0.substring(0, 3);
        String cbYear = cData1.substring(0, 7);

        int cDay = Integer.parseInt(cbDay, 2);
        int cMonth = Integer.parseInt(cbMonth, 2);
        int cYear = Integer.parseInt(cbYear, 2);


        String cData2 = String.format("%8s", Integer.toBinaryString(reply[4] & 0xFF)).replace(' ', '0');
        int t = Integer.parseInt(cData2.substring(0, 2), 2);
        cData2 = (String.format("%8s", Integer.toBinaryString(reply[4] & 0xFF)).replace(' ', '0')).substring(2);

        String cData3 = String.format("%8s", Integer.toBinaryString(reply[5] & 0xFF)).replace(' ', '0');

        int cM = Integer.parseInt(cData2, 2);
        int cH = Integer.parseInt(cData3, 2);

        TemperatureClock temperatureClock = new TemperatureClock(cYear, cMonth, cDay, cH, cM);


        if(t == 0)
            temperatureClock.setType(TemperatureClock.TemperatureType.EAR);
        else
            temperatureClock.setType(TemperatureClock.TemperatureType.FOREHEAD);

        return temperatureClock;
    }

    public static TemperatureClock analyseReadClock (byte [] reply) {
        return analyseClock(reply);
    }

    public static Temperature analyseTemperature(byte[] reply) {
        String s0 = String.format("%8s", Integer.toBinaryString(reply[2] & 0xFF)).replace(' ', '0');
        String s1 = String.format("%8s", Integer.toBinaryString(reply[3] & 0xFF)).replace(' ', '0');
        String o = s1 + s0;
        double object = (double)Integer.parseInt(o, 2)/10;

        String s2 = String.format("%8s", Integer.toBinaryString(reply[4] & 0xFF)).replace(' ', '0');
        String s3 = String.format("%8s", Integer.toBinaryString(reply[5] & 0xFF)).replace(' ', '0');
        String a = s3 + s2;
        double anmbient = (double)Integer.parseInt(a, 2)/10;

        return new Temperature(anmbient, object);
    }

    public static int analyseReadModel (byte [] reply) {
        int model = reply[3] << 8 | reply[2];
        return model;
    }

    public static long analyseReadSerial1 (byte [] reply) {
        long serial = 0;
        for (int i=3; i>=0;--i) {
            serial <<= 8;
            serial |= reply[2+i];
        }
        return serial;
    }

    public static long analyseReadSerial2 (byte [] reply) {
        long serial = 0;
        for (int i=3; i>=0;--i) {
            serial <<= 8;
            serial |= reply[2+i];
        }
        return serial;
    }

    public static TemperatureClock analyzeReadData1(byte[] reply){
        return analyseClock(reply);
    }

    public static Temperature analyseReadData2 (byte [] reply) {
        return analyseTemperature(reply);
    }

    public static int analyseReadCount (byte [] reply) {
        int count = reply[3] << 8 | reply[2];
        return count;
    }

    public static TemperatureClock analyseWriteClock (byte [] reply) {
           return analyseClock(reply);
    }

    public static Temperature analyseReadTemperature(byte[] reply){
        return analyseTemperature(reply);
    }

    public static Temperature analyseStartTemperature (byte [] reply) {
        return analyseTemperature(reply);
    }

    public static int analyseTurnOff (byte [] reply) {
        return 0;
    }

    public static int analyseClear (byte [] reply) {
        return 0;
    }
}
