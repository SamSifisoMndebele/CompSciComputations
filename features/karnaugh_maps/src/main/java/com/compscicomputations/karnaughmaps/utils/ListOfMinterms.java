package com.compscicomputations.karnaughmaps.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ListOfMinterms {
    private ArrayList<MinTerm> mintermsList = new ArrayList<>();
    private final int numberOfVariables;

    public ListOfMinterms(int[] iArr, int[] iArr2, int varNo) {
        this.numberOfVariables = varNo;
        String[] strArr = {"00000", "00001", "00010", "00011", "00100", "00101", "00110", "00111", "01000", "01001", "01010", "01011", "01100", "01101", "01110", "01111", "10000", "10001", "10010", "10011", "10100", "10101", "10110", "10111", "11000", "11001", "11010", "11011", "11100", "11101", "11110", "11111"};
        for (int i2 = 0; i2 < iArr.length; i2++) {
            MinTerm minterm = new MinTerm();
            minterm.minTermString = strArr[iArr[i2]];
            minterm.minTermString = minterm.minTermString.substring(5 - varNo, 5);
            minterm.minTermIntegers.add(iArr[i2]);
            this.mintermsList.add(i2, minterm);
        }
        for (int i3 = 0; i3 < iArr2.length; i3++) {
            MinTerm minTerm2 = new MinTerm();
            minTerm2.minTermString = strArr[iArr2[i3]];
            minTerm2.minTermString = minTerm2.minTermString.substring(5 - varNo, 5);
            minTerm2.minTermIntegers.add(iArr2[i3]);
            this.mintermsList.add(i3, minTerm2);
        }
    }

    public ListOfMinterms(int i) {
        this.numberOfVariables = i;
    }

    @NonNull
    public String toString() {
        if (this.mintermsList.size() == 1) {
            if (this.mintermsList.get(0).minTermIntegers.size() == 0) {
                return "S = 0";
            }
            if (((double) this.mintermsList.get(0).minTermIntegers.size()) == Math.pow(2.0d, this.numberOfVariables)) {
                return "S = 1";
            }
        }
        if (this.mintermsList.size() == 0) {
            return "S = 0";
        }
        StringBuilder str = new StringBuilder("S = " + this.mintermsList.get(0).toString());
        for (int i = 1; i < this.mintermsList.size(); i++) {
            str.append(" + ").append(this.mintermsList.get(i).toString());
        }
        return str.toString();
    }

    public String toStringPos() {
        if (this.mintermsList.size() == 1) {
            if (this.mintermsList.get(0).minTermIntegers.size() == 0) {
                return "S = 1";
            }
            if (((double) this.mintermsList.get(0).minTermIntegers.size()) == Math.pow(2.0d, this.numberOfVariables)) {
                return "S = 0";
            }
        }
        if (this.mintermsList.size() == 0) {
            return "S = 1";
        }
        StringBuilder str = new StringBuilder("S = (" + this.mintermsList.get(0).toStringPos() + ")");
        for (int i = 1; i < this.mintermsList.size(); i++) {
            str.append(" · (").append(this.mintermsList.get(i).toStringPos()).append(")");
        }
        return str.toString();
    }

    public int simplicity() {
        int i = 0;
        for (int i2 = 0; i2 < this.mintermsList.size(); i2++) {
            i += this.mintermsList.get(i2).minTermIntegers.size();
        }
        return i;
    }

    public int size() {
        return this.mintermsList.size();
    }

    public MinTerm getMinterm(int i) {
        return this.mintermsList.get(i);
    }

    public String getString(int i) {
        return this.mintermsList.get(i).minTermString;
    }

    public ArrayList<Integer> getIntegers(int i) {
        return this.mintermsList.get(i).minTermIntegers;
    }

    public void setString(int i, String str) {
        this.mintermsList.get(i).minTermString = str;
    }

    public void setInteger(int i, ArrayList<Integer> arrayList) {
        this.mintermsList.get(i).minTermIntegers = arrayList;
    }

    public ArrayList<Integer> getMintermsInteger() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < this.mintermsList.size(); i++) {
            arrayList.addAll(this.mintermsList.get(i).minTermIntegers);
        }
        return arrayList;
    }

    public void add(String str, ArrayList<Integer> arrayList) {
        MinTerm minterm = new MinTerm();
        minterm.minTermString = str;
        minterm.minTermIntegers = arrayList;
        this.mintermsList.add(minterm);
    }

    private boolean findString(ArrayList<MinTerm> arrayList, String str) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).minTermString.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public void removeDuplicates() {
        ArrayList<MinTerm> arrayList = new ArrayList<>();
        for (int i = 0; i < this.mintermsList.size(); i++) {
            MinTerm minterm = new MinTerm();
            String str = this.mintermsList.get(i).minTermString;
            ArrayList<Integer> arrayList2 = this.mintermsList.get(i).minTermIntegers;
            minterm.minTermString = str;
            minterm.minTermIntegers = arrayList2;
            if (!findString(arrayList, str)) {
                arrayList.add(minterm);
            }
        }
        this.mintermsList = arrayList;
    }

    public void removeString(String str) {
        for (int i = 0; i < this.mintermsList.size(); i++) {
            if (this.mintermsList.get(i).minTermString.equals(str)) {
                this.mintermsList.remove(i);
                return;
            }
        }
    }
}
