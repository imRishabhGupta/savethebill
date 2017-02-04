package com.example.user.savethebill;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
        System.out.println("testing.....1..2...3..");

        Firebase firebase = new Firebase("https://savethebill.firebaseio.com");
        ref=new Firebase("https://savethebill.firebaseio.com/"+firebase.getAuth().getUid());
        ref.keepSynced(true);

        System.out.println("testing.....1..2...3..");

        bills=new ArrayList<>();
        System.out.println("testing.....1..2...3..");
        progressDialog = new ProgressDialog(AllBills.this,
                ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setCanceledOnTouchOutside(false);
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
                Bill x=bills.get(position);

//                intent1.putExtra("billname",x.getBillName());
//                intent1.putExtra("type",x.getType());
//                intent1.putExtra("owner",x.getNameofowner());
//                intent1.putExtra("lastdate",x.getLastdate());
//                intent1.putExtra("guarantee",x.getGuarantee());
//                intent1.putExtra("image",x.getImagestring());
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

            System.out.println("testing.....1..2...3..");
            long count = snapshot.getChildrenCount();
            for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                Bill bill =  postSnapshot.getValue(Bill.class);
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
            progressDialog.dismiss();
        }
    });
}

//    @Override
  //  public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
    //    Toast.makeText(getApplicationContext(),"Bill deleted successfully",Toast.LENGTH_LONG).show();
      //  progressDialog.show();
        //refresh();
    //}

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.action_share:
                try {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,
                            String.format(getString(R.string.promo_msg_template),
                                    String.format(getString(R.string.app_share_url),getPackageName())));
                    startActivity(shareIntent);
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),getString(R.string.error_msg_retry),Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_logout:
                AlertDialog.Builder d = new AlertDialog.Builder(this);
                d.setMessage("Are you sure ?").
                        setCancelable(false).
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(AllBills.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }).
                        setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = d.create();
                alert.setTitle("Logout");
                alert.show();
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTextColor(Color.BLACK);
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.BLACK);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
