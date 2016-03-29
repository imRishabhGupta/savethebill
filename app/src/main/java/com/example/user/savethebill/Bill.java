package com.example.user.savethebill;

/**
 * Created by user on 29-03-2016.
 */
public class Bill {
    private String bill_type;
    private String owner_name;
    private Date1 last_date;
    private Date1 guarantee;
    private Date1 warranty;
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
    public Date1 getLast_date(){
        return last_date;
    }
    public void setLast_date(Date1 last_date){
        this.last_date=last_date;
    }
    public Date1 getGuarantee(){
        return guarantee;
    }
    public void setGuarantee(Date1 guarantee){
        this.guarantee=guarantee;
    }
    public Date1 getWarranty(){
        return warranty;
    }
    public void setWarranty(Date1 warranty){
        this.warranty=warranty;
    }

}
