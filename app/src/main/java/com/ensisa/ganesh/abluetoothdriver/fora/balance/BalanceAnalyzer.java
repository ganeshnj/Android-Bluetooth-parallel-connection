package com.ensisa.ganesh.abluetoothdriver.fora.balance;

/**
 * Created by Ganesh on 6/2/2015.
 */
public class BalanceAnalyzer {

    public static String  analyzeUser(byte b){
        return "User" + (int)b;
    }

    public static String analyzeGender(byte b){
        if(b == 0x00){
            return "Female";
        }else
            return "Male";
    }

    public static int analyzeAge(byte b){
        return (int)b;
    }

    public static int analyzeHeight(byte b) {
        return b ^ 0xffffff00;
    }

    public static double analyzeWeight(byte b, byte c){
        String s0 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        String s1 = String.format("%8s", Integer.toBinaryString(c & 0xFF)).replace(' ', '0');
        String w =  s0 + s1;
        int x = Integer.parseInt(w, 2);
        return (double)x/10;
    }
    public static double analyzeBodyFatRatio(byte b, byte c){
        String s0 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        String s1 = String.format("%8s", Integer.toBinaryString(c & 0xFF)).replace(' ', '0');
        String w =  s0 + s1;
        int x = Integer.parseInt(w, 2);
        return (double)x/10;
    }

    public static double analyzeBasalMetabolism(byte b, byte c){
        String s0 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        String s1 = String.format("%8s", Integer.toBinaryString(c & 0xFF)).replace(' ', '0');
        String w =  s0 + s1;
        int x = Integer.parseInt(w, 2);
        return (double)x;
    }

    public static double analyzeMoistureContent(byte b, byte c){
        String s0 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        String s1 = String.format("%8s", Integer.toBinaryString(c & 0xFF)).replace(' ', '0');
        String w =  s0 + s1;
        int x = Integer.parseInt(w, 2);
        return (double)x/10;
    }
    public static double analyzeMuscleRatio(byte b, byte c){
        String s0 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        String s1 = String.format("%8s", Integer.toBinaryString(c & 0xFF)).replace(' ', '0');
        String w =  s0 + s1;
        int x = Integer.parseInt(w, 2);
        return (double)x/10;
    }
    public static double analyzeSkletonWeight(byte b){
        String s0 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        String w =  s0;
        int x = Integer.parseInt(w, 2);
        return (double)x/10;
    }
}
