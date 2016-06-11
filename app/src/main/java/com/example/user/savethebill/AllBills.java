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
    ArrayList<Movie> bills;
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
        System.out.println("testing.....1..2...3..");

        Firebase firebase = new Firebase("https://savethebill.firebaseio.com");
        ref=new Firebase("https://savethebill.firebaseio.com/"+firebase.getAuth().getUid());
//        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        ref.keepSynced(true);

        System.out.println("testing.....1..2...3..");

        bills=new ArrayList<>();
        System.out.println("testing.....1..2...3..");
        progressDialog = new ProgressDialog(AllBills.this,
                ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Retrieving all your bills...");
        progressDialog.show();
        refresh();

        //System.out.println("got  "+bills.get(0).getOwner_name());
        billAdapter=new CustomListAdapter(this,bills);
        final ListView listView=(ListView)findViewById(R.id.list);
        listView.setAdapter(billAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent1 =new Intent(getApplicationContext(),DisplayBill.class);
                Movie x=bills.get(position);

                intent1.putExtra("billname",x.getBillName());
                intent1.putExtra("type",x.getType());
                intent1.putExtra("owner",x.getNameofowner());
                intent1.putExtra("lastdate",x.getLastdate());
                intent1.putExtra("guarantee",x.getGuarantee());
                intent1.putExtra("image",x.getImagestring());

                startActivity(intent1);

            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
void refresh(){
    bills=new ArrayList<>();

    ref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {

            System.out.println("testing.....1..2...3..");
            long count = snapshot.getChildrenCount();
            for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                Movie bill =  postSnapshot.getValue(Movie.class);
                bills.add(bill);
                System.out.println(bill.getType() + "  " + bill.getNameofowner());
            }
            System.out.println("The read success: " + count);
            billAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            System.out.println("The read failed: " + firebaseError.getMessage());
        }
    });


}

}
