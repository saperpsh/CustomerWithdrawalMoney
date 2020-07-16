package com.example.qinshao_final;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Custemer implements Serializable,Comparable<Custemer> {

    private Date openDate;
    private double balance;
    private String familyName;
    private String firstName;
    private String phoneNumber;




    private String accountNum;
    private String sinNumber;

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public Custemer(String accountNum, Date openDate, double balance, String firstName, String familyName, String phoneNumber, String sinNumber) {

        this.openDate = openDate;
        this.balance = balance;
        this.familyName = familyName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.accountNum = accountNum;
        this.sinNumber = sinNumber;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getSinNumber() {
        return sinNumber;
    }

    public void setSinNumber(String sinNumber) {
        this.sinNumber = sinNumber;
    }



    @Override
    public String toString() {
        return  firstName + ' ' + familyName ;
    }

    @Override
    public int compareTo(Custemer o) {
        return familyName.compareTo(o.familyName);
    }
}
