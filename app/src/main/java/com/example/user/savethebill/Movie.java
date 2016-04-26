package com.example.user.savethebill;

/**
 * Created by rohanpc on 4/24/2016.
 */


public class Movie {
    private String bill_name, type , imagestring , nameofowner , lastdate, guarantee ;


    public Movie() {
    }

    public Movie(String bill_name, String type, String imagestring, String nameofowner, String lastdate, String gaurantee) {
        this.bill_name = bill_name;
        this.imagestring = imagestring;
        this.type = type;
        this.nameofowner = nameofowner;
        this.lastdate = lastdate;
        this.guarantee = guarantee;
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

    public String getLastdate() {
        return lastdate;
    }

    public void setLastdate(String lastdate) {
        this.lastdate =lastdate;
    }

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }




}