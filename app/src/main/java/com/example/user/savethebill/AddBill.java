package com.example.user.savethebill;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class AddBill extends AppCompatActivity {

    Firebase firebase,ref;
    EditText a,b;
    DatePicker c,d,e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        firebase = new Firebase("https://savethebill.firebaseio.com");
        ref=new Firebase("https://savethebill.firebaseio.com/"+firebase.getAuth().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                System.out.println("The read success: " + count);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query queryRef = ref.orderByChild("warranty");
                queryRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        Bill bill = snapshot.getValue(Bill.class);
                        System.out.println(snapshot.getKey() + " was " + bill.getWarranty() + " meters tall");
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
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
        ref.child("Bill").setValue(bill);


    }
}
