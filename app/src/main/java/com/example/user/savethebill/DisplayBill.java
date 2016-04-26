package com.example.user.savethebill;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayBill extends AppCompatActivity {

    String billname;
    String type;
    String image;
    String guarantee;
    String lastdate;
    String owner;
    ImageView thumbnail;
    TextView a,b,c,d,e;
    Button delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bill);
        a=(TextView)findViewById(R.id.textView);
        b=(TextView)findViewById(R.id.textView2);
        c=(TextView)findViewById(R.id.textView3);
        d=(TextView)findViewById(R.id.textView4);
        e=(TextView)findViewById(R.id.textView5);
        thumbnail=(ImageView)findViewById(R.id.imageView2);
        delete=(Button)findViewById(R.id.button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Intent intent = getIntent();
        if (null != intent) {
            billname = intent.getStringExtra("billname");
            type = intent.getStringExtra("type");
            image = intent.getStringExtra("image");
            guarantee = intent.getStringExtra("guarantee");
            lastdate = intent.getStringExtra("lastdate");
            owner = intent.getStringExtra("owner");
        }

        if(image!=null){
        byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
        thumbnail.setImageBitmap(
                BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
        );}
        a.setText(billname);
        b.setText(owner);
        c.setText(type);
        d.setText(guarantee);
        e.setText(lastdate);




    }
}
