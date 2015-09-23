package com.ensisa.ganesh.abluetoothdriver.fora.temperature;

/**
 * Created by Ganesh on 6/2/2015.
 */
public class Temperature {
    private TemperatureClock clock;
    private double objectTemp;
    private double ambientTemp;

    public Temperature(double ambientTemp, double objectTemp, TemperatureClock time) {
        this.ambientTemp = ambientTemp;
        this.objectTemp = objectTemp;
        this.clock = time;
    }

    public Temperature(double ambientTemp, double objectTemp) {
        this.ambientTemp = ambientTemp;
        this.objectTemp = objectTemp;
    }

    public Temperature(TemperatureClock time) {
        this.clock = time;
    }


    public double getAmbientTemp() {
        return ambientTemp;
    }

    public void setAmbientTemp(double ambientTemp) {
        this.ambientTemp = ambientTemp;
    }

    public double getObjectTemp() {
        return objectTemp;
    }

    public void setObjectTemp(double objectTemp) {
        this.objectTemp = objectTemp;
    }

    public TemperatureClock getClock() {
        return clock;
    }

    public void setClock(TemperatureClock clock) {
        this.clock = clock;
    }

    public String getTime() {
        return clock.getTime();
    }

    public String getDate() {
        return clock.getDate();
    }
}
