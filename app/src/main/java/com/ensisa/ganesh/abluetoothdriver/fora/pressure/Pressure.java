package com.ensisa.ganesh.abluetoothdriver.fora.pressure;

/**
 * Created by Ganesh on 6/2/2015.
 */
public class Pressure {
    private int user;
    private PressureClock clock;
    private double glucoseMeasure;
    private double ambientTemperature;
    private double code;
    private double systolicMeasure;
    private double meanPressure;
    private double diastolicMeasure;
    private double pulse;

    private GlucoseType glucoseType;

    public Pressure(double ambientTemperature, PressureClock clock, double code, double diastolicMeasure, double glucoseMeasure, GlucoseType glucoseType, double meanPressure, double pulse, double systolicMeasure) {
        this.ambientTemperature = ambientTemperature;
        this.clock = clock;
        this.code = code;
        this.diastolicMeasure = diastolicMeasure;
        this.glucoseMeasure = glucoseMeasure;
        this.glucoseType = glucoseType;
        this.meanPressure = meanPressure;
        this.pulse = pulse;
        this.systolicMeasure = systolicMeasure;
    }

    public Pressure(PressureClock clock) {
        this.clock = clock;
    }

    public Pressure(double ambientTemperature, double code, double diastolicMeasure, double glucoseMeasure, GlucoseType glucoseType, double meanPressure, double pulse, double systolicMeasure) {
        this.ambientTemperature = ambientTemperature;
        this.code = code;
        this.diastolicMeasure = diastolicMeasure;
        this.glucoseMeasure = glucoseMeasure;
        this.glucoseType = glucoseType;
        this.meanPressure = meanPressure;
        this.pulse = pulse;
        this.systolicMeasure = systolicMeasure;
    }

    public Pressure() {
    }

    public double getSystolicMeasure() {
        return systolicMeasure;
    }

    public void setSystolicMeasure(double systolicMeasure) {
        this.systolicMeasure = systolicMeasure;
    }

    public double getAmbientTemperature() {
        return ambientTemperature;
    }

    public void setAmbientTemperature(double ambientTemperature) {
        this.ambientTemperature = ambientTemperature;
    }

    public PressureClock getClock() {
        return clock;
    }

    public void setClock(PressureClock clock) {
        this.clock = clock;
    }

    public double getCode() {
        return code;
    }

    public void setCode(double code) {
        this.code = code;
    }

    public double getDiastolicMeasure() {
        return diastolicMeasure;
    }

    public void setDiastolicMeasure(double diastolicMeasure) {
        this.diastolicMeasure = diastolicMeasure;
    }

    public double getGlucoseMeasure() {
        return glucoseMeasure;
    }

    public void setGlucoseMeasure(double glucoseMeasure) {
        this.glucoseMeasure = glucoseMeasure;
    }

    public String getGlucoseType() {
        if(glucoseType == GlucoseType.GENERAL)
            return "GEN";
        else if(glucoseType == GlucoseType.BEFOREMEAL)
            return "AC";
        else if(glucoseType == GlucoseType.AFTERMEAL)
            return "PC";
        else
            return "QC";
    }

    public void setGlucoseType(GlucoseType glucoseType) {
        this.glucoseType = glucoseType;
    }

    public double getMeanPressure() {
        return meanPressure;
    }

    public void setMeanPressure(double meanPressure) {
        this.meanPressure = meanPressure;
    }

    public double getPulse() {
        return pulse;
    }

    public void setPulse(double pulse) {
        this.pulse = pulse;
    }

    public enum GlucoseType {
        GENERAL, BEFOREMEAL, AFTERMEAL, QUALITYCONTROL;
    }

    public String getTime() {
        return clock.getTime();
    }

    public String getDate() {
        return clock.getDate();
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

}
