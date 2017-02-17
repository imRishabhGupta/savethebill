package com.example.user.savethebill;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayBill extends AppCompatActivity {

    private static final String TAG = DisplayBill.class.getSimpleName();
    private String billname, type, imagestring, endDate1, endDate2, nameofowner, id1, id2,id;
    private ImageView thumbnail;
    private TextView a,b,c,d,e;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private String[] data=new String[9];
    private Button cancelAlarm1,cancelAlarm2;

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
        //Firebase firebase = new Firebase("https://savethebill.firebaseio.com");
        //ref=new Firebase("https://savethebill.firebaseio.com/"+ firebase.getAuth().getUid()+"/Bill"+position);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        String uid=mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference(uid+"/Bill"+position);

        Log.d(TAG,"bill"+position);
        progressDialog = new ProgressDialog(DisplayBill.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Retrieving bill...");
        progressDialog.show();
        refresh(mDatabase);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ProgressDialog progressDialog1 = new ProgressDialog(DisplayBill.this, ProgressDialog.THEME_HOLO_LIGHT);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog1.setMessage("Deleting bill...");
            progressDialog1.show();
            mDatabase.removeValue();
            SharedPreferences preferences=getSharedPreferences("bills", Context.MODE_PRIVATE);
            String cancel1=preferences.getString(id+id1,null);
            String cancel2=preferences.getString(id+id2,null);
            SharedPreferences.Editor editor=getSharedPreferences("bills",MODE_PRIVATE).edit();
            if(cancel1.equals("no"))
                editor.putString(id+id1, "yes");
            if(cancel2.equals("no"))
                editor.putString(id+id2, "yes");
            editor.commit();
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
                finish();
            }
        });

        cancelAlarm1=(Button)findViewById(R.id.alarm1);
        cancelAlarm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=getSharedPreferences("bills",MODE_PRIVATE).edit();
                editor.putString(id+id1, "yes");
                editor.commit();
                Toast.makeText(DisplayBill.this, "Alarm 1 cancelled successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        cancelAlarm2=(Button)findViewById(R.id.alarm2);
        cancelAlarm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=getSharedPreferences("bills",MODE_PRIVATE).edit();
                editor.putString(id+id2, "yes");
                editor.commit();
                Toast.makeText(DisplayBill.this, "Alarm 2 cancelled successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void refresh(DatabaseReference ref){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int i=0;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String str =  postSnapshot.getValue(String.class);
                    data[i]=str;
                    Log.d(TAG,data[i]);
                    i++;
                }
                billname=data[0];
                endDate1 =data[1];
                endDate2 =data[2];
                id=data[3];
                id1=data[4];
                id2=data[5];
                imagestring =data[6];
                nameofowner =data[7];
                type=data[8];
                SharedPreferences preferences=getSharedPreferences("bills", Context.MODE_PRIVATE);
                String cancel1=preferences.getString(id+id1,null);
                String cancel2=preferences.getString(id+id2,null);

                if(cancel1!=null&&cancel1.equals("no")){
                    cancelAlarm1.setVisibility(View.VISIBLE);
                }
                if(cancel2!=null&&cancel2.equals("no")){
                    cancelAlarm2.setVisibility(View.VISIBLE);
                }

                a.setText(billname);
                b.setText(nameofowner);
                c.setText(type);
                d.setText(endDate1);
                e.setText(endDate2);
                if(imagestring !=null&&!imagestring.equals("")){
                    byte[] imageAsBytes = Base64.decode(imagestring.getBytes(), Base64.DEFAULT);
                    thumbnail.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                progressDialog.dismiss();
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}


