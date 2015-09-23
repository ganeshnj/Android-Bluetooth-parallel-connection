package com.ensisa.ganesh.abluetoothdriver.fora.balance;

/**
 * Created by Ganesh on 6/2/2015.
 */
public class BalanceUserParameter {
    int userId;
    int gender; //0 Male 1 Female
    int Age;
    int height;

    public BalanceUserParameter(int age, int gender, int height, int userId) {
        Age = age;
        this.gender = gender;
        this.height = height;
        this.userId = userId;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
