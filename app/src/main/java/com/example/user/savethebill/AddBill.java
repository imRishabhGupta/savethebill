package com.example.user.savethebill;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class AddBill extends AppCompatActivity {

    Firebase firebase,ref;
    EditText a,b;
    DatePicker c,d,e;
    long count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebase = new Firebase("https://savethebill.firebaseio.com");
        ref=new Firebase("https://savethebill.firebaseio.com/"+firebase.getAuth().getUid());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
Button button=(Button)findViewById(R.id.bu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent =new Intent(getApplicationContext(),ImageActivity.class);
                //startActivity(intent);
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void add(View view){
        a=(EditText)findViewById(R.id.bill_name);
        b=(EditText)findViewById(R.id.owner);
        c=(DatePicker)findViewById(R.id.last_date);
        d=(DatePicker)findViewById(R.id.guarantee);
        e=(DatePicker)findViewById(R.id.warranty);

        final Bill bill=new Bill();
        bill.setBill_Type(a.getText().toString());
        bill.setOwner_name(b.getText().toString());
        Date1 date1=new Date1();
        date1.setDay(c.getDayOfMonth());
        date1.setMonth(c.getMonth());
        date1.setYear(c.getYear());
        bill.setLast_date(date1);

        date1.setDay(d.getDayOfMonth());
        date1.setMonth(d.getMonth());
        date1.setYear(d.getYear());
        bill.setGuarantee(date1);

        date1.setDay(e.getDayOfMonth());
        date1.setMonth(e.getMonth());
        date1.setYear(e.getYear());
        bill.setWarranty(date1);
        ref.addValueEventListener(new ValueEventListener() {
            long c;

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                c = snapshot.getChildrenCount();
                System.out.println("The read success: " + c);
                getData(c);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        System.out.println("got"+count);
        ref.child("Bill"+count).setValue(bill);


    }
    public void getData(long c){
        count=c;
    }
}
