package com.example.user.savethebill;

import java.util.Date;

/**
 * Created by user on 29-03-2016.
 */
public class Bill {
    private String bill_type;
    private String owner_name;
    private Date last_date;
    private Date guarantee;
    private Date warranty;
    public Bill(){

    }
    public String getBill_Type(){
        return bill_type;
    }
    public void setBill_Type(String bill_type){
        this.bill_type=bill_type;
    }
    public String getOwner_name(){
        return owner_name;
    }
    public void setOwner_name(String owner_name){
        this.owner_name=owner_name;
    }
    public Date getLast_date(){
        return last_date;
    }
    public void setLast_date(Date last_date){
        this.last_date=last_date;
    }
    public Date getGuarantee(){
        return guarantee;
    }
    public void setGuarantee(Date guarantee){
        this.guarantee=guarantee;
    }
    public Date getWarranty(){
        return warranty;
    }
    public void setWarranty(Date warranty){
        this.warranty=warranty;
    }

}
