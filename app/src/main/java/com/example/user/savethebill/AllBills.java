package com.example.user.savethebill;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

public class AllBills extends AppCompatActivity {
    ArrayList<Bill> bills;
    Firebase ref;
    CustomListAdapter billAdapter;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bills);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),AddBill.class);
                startActivity(i);
            }
        });
        Firebase firebase = new Firebase("https://savethebill.firebaseio.com");
        ref=new Firebase("https://savethebill.firebaseio.com/"+firebase.getAuth().getUid());
        ref.keepSynced(true);

        bills=new ArrayList<>();
        progressDialog = new ProgressDialog(AllBills.this,
                ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Retrieving all your bills...");
        progressDialog.show();
        refresh();

        billAdapter=new CustomListAdapter(this,bills);
        final ListView listView=(ListView)findViewById(R.id.list);
        listView.setAdapter(billAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent1 =new Intent(getApplicationContext(),DisplayBill.class);
                Bill x=bills.get(position);

                intent1.putExtra("position",Integer.toString(position));

                startActivity(intent1);

            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
void refresh(){
    bills=new ArrayList<>();
    bills.clear();

    ref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {

            long count = snapshot.getChildrenCount();
            for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                Bill bill =  postSnapshot.getValue(Bill.class);
                bills.add(bill);
            }
            billAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            System.out.println("The read failed: " + firebaseError.getMessage());
            progressDialog.dismiss();
        }
    });
}

}
