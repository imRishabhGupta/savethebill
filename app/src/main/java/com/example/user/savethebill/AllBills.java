package com.example.user.savethebill;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllBills extends AppCompatActivity {
    ArrayList<Movie> bills;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bills);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),AddBill.class);
                startActivity(i);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Firebase firebase = new Firebase("https://savethebill.firebaseio.com");
        Firebase ref=new Firebase("https://savethebill.firebaseio.com/"+firebase.getAuth().getUid());
//        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        ref.keepSynced(true);



        bills=new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        Movie bill = postSnapshot.getValue(Movie.class);
                        bills.add(bill);
                        System.out.println(bill.getType() + "  " + bills.get(0).getNameofowner());
                    }
                    System.out.println("The read success: " + count);


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        //System.out.println("got  "+bills.get(0).getOwner_name());
        final CustomListAdapter billAdapter=new CustomListAdapter(this,bills);
        final ListView listView=(ListView)findViewById(R.id.list);
        listView.setAdapter(billAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent1 =new Intent(getApplicationContext(),DisplayBill.class);
                Movie x=(Movie)billAdapter.getItem(position);

                intent1.putExtra("billname",x.getBillName());
                intent1.putExtra("type",x.getType());
                intent1.putExtra("owner",x.getNameofowner());
                intent1.putExtra("lastdate",x.getLastdate());
                intent1.putExtra("guarantee",x.getGuarantee());

                startActivity(intent1);

            }
        });



    }

}
