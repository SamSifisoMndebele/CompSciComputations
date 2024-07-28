package com.compscicomputations.karnaugh_maps.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class BinaryBits {
    public boolean[] bits;
    private int intValue;
    private final int numberOfBits;

    public BinaryBits(int i) {
        this.numberOfBits = i;
        this.bits = new boolean[i];
        int i2 = 0;
        while (true) {
            boolean[] zArr = this.bits;
            if (i2 < zArr.length) {
                zArr[i2] = false;
                i2++;
            } else {
                this.intValue = 0;
                return;
            }
        }
    }

    public int numberOfPermutations() {
        return (int) Math.pow(2.0d, this.numberOfBits);
    }

    @NonNull
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < this.numberOfBits; i++) {
            if (this.bits[i]) {
                str.append("1");
            } else {
                str.append("0");
            }
        }
        return str.toString();
    }

    public void setBits(boolean[] zArr) {
        this.bits = zArr;
        this.intValue = Integer.parseInt(toString(), 2);
    }

    public ArrayList<Integer> arrayIndexOfSetBits() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < this.numberOfBits; i++) {
            if (this.bits[i]) {
                arrayList.add(i);
            }
        }
        return arrayList;
    }

    private void setBits(int i) {
        StringBuilder binaryString = new StringBuilder(Integer.toBinaryString(i));
        while (binaryString.length() < this.numberOfBits) {
            binaryString.insert(0, "0");
        }
        char[] charArray = binaryString.toString().toCharArray();
        for (int i2 = 0; i2 < this.numberOfBits; i2++) {
            this.bits[i2] = charArray[i2] != '0';
        }
    }

    public void inc() {
        int i = this.intValue + 1;
        this.intValue = i;
        setBits(i);
    }

    public void dec() {
        int i = this.intValue - 1;
        this.intValue = i;
        setBits(i);
    }
}
