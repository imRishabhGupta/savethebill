package com.example.user.savethebill;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class DisplayBill extends AppCompatActivity {

    private String billname, type, image, endDate1, endDate2, owner;
    private ImageView thumbnail;
    private TextView a,b,c,d,e;
    private Firebase ref;
    private ProgressDialog progressDialog;
    private String[] data=new String[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        a=(TextView)findViewById(R.id.textView);
        b=(TextView)findViewById(R.id.textView2);
        c=(TextView)findViewById(R.id.textView3);
        d=(TextView)findViewById(R.id.textView4);
        e=(TextView)findViewById(R.id.textView5);
        thumbnail=(ImageView)findViewById(R.id.imageView2);
        Button delete = (Button) findViewById(R.id.button);
        final Intent intent = getIntent();
        final String position=intent.getStringExtra("position");
        System.out.println(position);
        Firebase firebase = new Firebase("https://savethebill.firebaseio.com");
        ref=new Firebase("https://savethebill.firebaseio.com/"+ firebase.getAuth().getUid()+"/Bill"+position);

        progressDialog = new ProgressDialog(DisplayBill.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Retrieving bill...");
        progressDialog.show();
        refresh(ref);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ProgressDialog progressDialog1 = new ProgressDialog(DisplayBill.this, ProgressDialog.THEME_HOLO_LIGHT);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog1.setMessage("Deleting bill...");
            progressDialog1.show();
            ref.removeValue();
            progressDialog1.dismiss();
            finish();
            }
        });

        Button edit=(Button)findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent1=new Intent(getApplicationContext(),EditBill.class);
            intent1.putExtra("position",position);
            startActivity(intent1);
            }
        });

        Button cancelAlarm=(Button)findViewById(R.id.alarm);
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=getSharedPreferences("bills",MODE_PRIVATE).edit();
                editor.putString(billname, "yes");
                editor.commit();
                Toast.makeText(DisplayBill.this, "Alarm cancelled successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void refresh(Firebase ref){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int i=0;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String str =  postSnapshot.getValue(String.class);
                    data[i]=str;
                    i++;
                }
                billname=data[0];
                endDate1 =data[1];
                endDate2 =data[2];
                image=data[3];
                owner=data[4];
                type=data[5];
                a.setText(billname);
                b.setText(owner);
                c.setText(type);
                d.setText(endDate1);
                e.setText(endDate2);
                if(image!=null&&!image.equals("")){
                    byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
                    thumbnail.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                progressDialog.dismiss();
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
}


