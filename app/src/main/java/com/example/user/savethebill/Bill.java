package com.example.user.savethebill;

/**
 * Created by rohanpc on 4/24/2016.
 */


public class Bill {
    private String bill_name, endDate1, endDate2, id1, id2, imagestring , nameofowner, type;


    public Bill() {
    }

    public Bill(String bill_name, String type, String imagestring, String nameofowner, String endDate2, String endDate1, String id1, String id2) {
        this.bill_name = bill_name;
        this.imagestring = imagestring;
        this.type = type;
        this.nameofowner = nameofowner;
        this.endDate2 = endDate2;
        this.id1 = id1;
        this.id2=id2;
        this.endDate1 = endDate1;
    }

    public String getBillName() {return bill_name;}

    public void setBillName(String bill_name) {
        this.bill_name = bill_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImagestring() {
        return imagestring;
    }

    public void setImagestring(String imagestring) {
        this.imagestring = imagestring;
    }

    public String getNameofowner() {
        return nameofowner;
    }

    public void setNameofowner(String nameofowner) {
        this.nameofowner = nameofowner;
    }

    public String getEndDate2() {
        return endDate2;
    }

    public void setEndDate2(String endDate2) {
        this.endDate2 = endDate2;
    }

    public String getEndDate1() {
        return endDate1;
    }

    public void setEndDate1(String endDate1) {
        this.endDate1 = endDate1;
    }


    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }
}