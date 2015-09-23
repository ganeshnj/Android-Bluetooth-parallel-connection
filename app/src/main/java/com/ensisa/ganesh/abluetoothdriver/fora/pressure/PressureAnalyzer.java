package com.ensisa.ganesh.abluetoothdriver.fora.pressure;

import android.os.Bundle;

/**
 * Created by Ganesh on 6/1/2015.
 */
public class PressureAnalyzer {
    private static PressureClock analyseClock (byte [] reply) {

        String cData0 = String.format("%8s", Integer.toBinaryString(reply[2] & 0xFF)).replace(' ', '0');
        String cData1 = String.format("%8s", Integer.toBinaryString(reply[3] & 0xFF)).replace(' ', '0');

        String cbDay = cData0.substring(3, 8);
        String cbMonth = cData1.charAt(7) + cData0.substring(0, 3);
        String cbYear = cData1.substring(0, 7);

        int cDay = Integer.parseInt(cbDay, 2);
        int cMonth = Integer.parseInt(cbMonth, 2);
        int cYear = Integer.parseInt(cbYear, 2);

        String cData2 = String.format("%8s", Integer.toBinaryString(reply[4] & 0xFF)).replace(' ', '0');
        int bt = Integer.parseInt(cData2.substring(0, 1), 2);
        int ht = Integer.parseInt(cData2.substring(1, 2), 2);
        cData2 = String.format("%8s", Integer.toBinaryString(reply[4] & 0xFF)).replace(' ', '0').substring(2);

        String cData3 = String.format("%8s", Integer.toBinaryString(reply[5] & 0xFF)).replace(' ', '0').substring(3);

        int cM = Integer.parseInt(cData2, 2);
        int cH = Integer.parseInt(cData3, 2);

        Bundle bundle = new Bundle();
        bundle.putInt(PressureConstant.KEY_DEVICE_YEAR, cYear);
        bundle.putInt(PressureConstant.KEY_DEVICE_MONTH, cMonth);
        bundle.putInt(PressureConstant.KEY_DEVICE_DAY, cDay);
        bundle.putInt(PressureConstant.KEY_DEVICE_MINUTE, cM);
        bundle.putInt(PressureConstant.KEY_DEVICE_HOUR, cH);

        PressureClock pressureClock = new PressureClock(cYear, cMonth, cDay, cH, cM);

        if(bt == 0)
            pressureClock.setBloodType(PressureClock.BloodMeasureType.BLOODGLUCOSE);
        else
            pressureClock.setBloodType(PressureClock.BloodMeasureType.BLOODPRESSURE);

        if(ht == 0)
            pressureClock.setHeartBeatType(PressureClock.HeartBeatType.NORMALHEARTBEATS);
        else
            pressureClock.setHeartBeatType(PressureClock.HeartBeatType.ARRHYTHMIA);

        return pressureClock;
    }

    public static PressureClock analyseReadClock (byte [] reply) {
        return analyseClock(reply);
    }

    public static String analyzeUser(byte [] reply)
    {
        String s0 = String.format("%8s", Integer.toBinaryString(reply[5] & 0xFF)).replace(' ', '0');
        int x = Integer.parseInt(s0, 2);
        return "User" + x;
    }

    private static Pressure analysePressure(byte[] reply) {
        Pressure pressure = new Pressure();

        //glucose Value
        String s0 = String.format("%8s", Integer.toBinaryString(reply[2] & 0xFF)).replace(' ', '0');
        String s1 = String.format("%8s", Integer.toBinaryString(reply[3] & 0xFF)).replace(' ', '0');
        String g = s1 + s0;
        double glucoseMeasure = (double)Integer.parseInt(g, 2);

        // Measure of Ambient Temperature
        String s2 = String.format("%8s", Integer.toBinaryString(reply[4] & 0xFF)).replace(' ', '0');
        double ambientTemperature = (double)Integer.parseInt(s2, 2);

        //Glucose Type
        String s3 = String.format("%8s", Integer.toBinaryString(reply[5] & 0xFF)).replace(' ', '0');
        int t = Integer.parseInt(s3.substring(0, 2), 2);

        Pressure.GlucoseType glucoseType;
        if(t == 0)
            glucoseType = Pressure.GlucoseType.GENERAL;
        else if(t == 1)
            glucoseType =Pressure.GlucoseType.BEFOREMEAL;
        else if(t == 2)
            glucoseType =Pressure.GlucoseType.AFTERMEAL;
        else
        glucoseType =Pressure.GlucoseType.QUALITYCONTROL;

        //Code
        s3 = String.format("%8s", Integer.toBinaryString(reply[5] & 0xFF)).replace(' ', '0').substring(2);
        double code = (double)Integer.parseInt(s3, 2);

        //Systolic Measure
        double systolicMeasure = (double)Integer.parseInt(s0, 2);

        //Mean Pressure
        double meanPressure = (double)Integer.parseInt(s1, 2);

        //Diastolic Measure
        double diastolicMeasure = (double)Integer.parseInt(s2, 2);

        //Pulse
        s3 = String.format("%8s", Integer.toBinaryString(reply[5] & 0xFF)).replace(' ', '0');
        double pulse = (double)Integer.parseInt(s3, 2);

        pressure = new Pressure(ambientTemperature, code, diastolicMeasure, glucoseMeasure, glucoseType, meanPressure, pulse, systolicMeasure );
        return pressure;
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

    public static PressureClock analyzeReadData1(byte[] reply){
        return analyseClock(reply);
    }

    public static Pressure analyseReadData2 (byte [] reply) {
        return analysePressure(reply);
    }

    public static int analyseReadCount (byte [] reply) {
        int count = reply[3] << 8 | reply[2];
        return count;
    }

    public static PressureClock analyseWriteClock (byte [] reply) {
        return analyseClock(reply);
    }

    public static int analyseTurnOff (byte [] reply) {
        return 0;
    }

    public static int analyseClear (byte [] reply) {
        return 0;
    }
}
