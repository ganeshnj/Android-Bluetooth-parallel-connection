package com.ensisa.ganesh.abluetoothdriver.fora.balance;

/**
 * Created by Ganesh on 6/2/2015.
 */
public class BalanceMeasurement {
    double weight;
    double bodyFatRatio;
    double baselMetabolism;
    double MoistureContent;
    double muscleRatio;
    double skeletonWeight;


    public double getBaselMetabolism() {
        return baselMetabolism;
    }

    public void setBaselMetabolism(double baselMetabolism) {
        this.baselMetabolism = baselMetabolism;
    }

    public double getBodyFatRatio() {
        return bodyFatRatio;
    }

    public void setBodyFatRatio(double bodyFatRatio) {
        this.bodyFatRatio = bodyFatRatio;
    }

    public double getMoistureContent() {
        return MoistureContent;
    }

    public void setMoistureContent(double moistureContent) {
        MoistureContent = moistureContent;
    }

    public double getMuscleRatio() {
        return muscleRatio;
    }

    public void setMuscleRatio(double muscleRatio) {
        this.muscleRatio = muscleRatio;
    }

    public double getSkeletonWeight() {
        return skeletonWeight;
    }

    public void setSkeletonWeight(double skeletonWeight) {
        this.skeletonWeight = skeletonWeight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
