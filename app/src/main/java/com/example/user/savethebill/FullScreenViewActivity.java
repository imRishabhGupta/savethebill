package com.example.user.savethebill;

/**
 * Created by rohanagarwal94 on 17/2/17.
 */
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class FullScreenViewActivity extends Activity{

    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        viewPager = (ViewPager) findViewById(R.id.pager);


        Intent i = getIntent();
        Uri fileUri = Uri.parse(i.getStringExtra("fileuri"));
        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
                fileUri);

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(0);
    }
}