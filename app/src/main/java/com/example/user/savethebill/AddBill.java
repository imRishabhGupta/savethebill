package com.example.user.savethebill;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBill extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private  EditText a,c;
    private Bitmap bitmap;
    String base64Image;
    private   AutoCompleteTextView b;
    long count=7;
    String[] arr={"electricity bill","water bill","consumer bill","telephone bill","other bill"};
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Saved Bills";
    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    Calendar cal1,cal2;

    private ImageView imgPreview;
    private static final int CAMERA_CAPTURE_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 1234;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private Uri fileUri;
    private ProgressDialog progress;
    Bitmap image = null;

    private static String[] APP_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @OnClick(R.id.gotoGallery)
    void openGalleryImages() {
        GetImagesGallery();
    }

    @OnClick(R.id.imageView)
    void zoomImages() {
        if(fileUri!=null) {
            Intent i = new Intent(AddBill.this, FullScreenViewActivity.class);
            i.putExtra("fileuri", fileUri.toString());
            startActivity(i);
        }
    }

    @OnClick(R.id.bu)
    void openCameraImages() {
        CaptureImageCamera();
    }

    private void CaptureImageCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_CAPTURE_REQUEST_CODE);
    }

    private void GetImagesGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST_CODE);
    }

    private void getCameraImageUri() {
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = new File(Environment.getExternalStorageDirectory(), "bill.jpg");
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    image.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();
                    fileUri = Uri.fromFile(file);
                } catch (Exception e) {
                    Log.i("File Error", e.toString());
                }
            }
        }.start();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                image = (Bitmap) data.getExtras().get("data");
                imgPreview.setImageBitmap(image);
                getCameraImageUri();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                fileUri = data.getData();
                try {
                    image = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                    image = Bitmap.createScaledBitmap(image, 500, 500, false);
                    imgPreview.setImageBitmap(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image selection", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to open Gallery", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    APP_PERMISSIONS,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        ButterKnife.bind(this);
        verifyStoragePermissions(this);
        verifyCameraPermission();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        b = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, arr);

        b.setThreshold(1);
        b.setAdapter(adapter);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);

        toDateEtxt = (EditText) findViewById(R.id.etxt_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);

        setDateTimeField();

        //firebase = new Firebase("https://savethebill.firebaseio.com");
        //ref = new Firebase("https://savethebill.firebaseio.com/" + firebase.getAuth().getUid());
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        String uid=mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            long cd;

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                cd = snapshot.getChildrenCount();
                System.out.println("The read success: " + cd);
                getData(cd);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        imgPreview = (ImageView) findViewById(R.id.imageView);

        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {fromDatePickerDialog.show();
            }
        });

        toDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cal1 = Calendar.getInstance();

                cal1.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(cal1.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cal2 = Calendar.getInstance();
                cal2.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(cal2.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public void storeImageToFirebase() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public void add(View view){
        a=(EditText)findViewById(R.id.bill_name);
        b=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        c=(EditText)findViewById(R.id.owner);

         final Bill bill=new Bill();
        bill.setBillName(a.getText().toString());
        bill.setType(b.getText().toString());
        bill.setNameofowner(c.getText().toString());
        bill.setEndDate1(fromDateEtxt.getText().toString());
        bill.setEndDate2(toDateEtxt.getText().toString());
        if(bitmap!=null)
        {
        storeImageToFirebase();
        bill.setImagestring(base64Image);
        }
        else
            bill.setImagestring("");

        Intent notificationIntent = new Intent(this,AlarmReceiver.class);
        notificationIntent.setAction("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.putExtra("name",a.getText().toString());
        notificationIntent.putExtra("type",b.getText().toString());

        if(cal1!=null&&cal1.getTimeInMillis()<=System.currentTimeMillis()){
            Toast.makeText(AddBill.this, "Date should be greater than current time.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(cal1!=null&&fromDateEtxt.getText().toString().length()>0) {
          setRepeatingAlarm(notificationIntent,cal1);
        }
        if(cal2!=null&&cal2.getTimeInMillis()<=System.currentTimeMillis()){
            Toast.makeText(AddBill.this, "Date should be greater than current time.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(cal2!=null&&toDateEtxt.getText().toString().length()>0) {
            setRepeatingAlarm(notificationIntent,cal2);
        }

        mDatabase.child("Bill"+count).setValue(bill);
        Toast.makeText(getApplicationContext(),"Bill added successfully.",Toast.LENGTH_SHORT).show();

        Intent i=new Intent(getApplicationContext(),AllBills.class);
        startActivity(i);
    }

    public void setRepeatingAlarm(Intent notificationIntent, Calendar cal) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        notificationIntent.putExtra("daysleft",cal.getTime().getTime());

        int id = (int) System.currentTimeMillis();
        notificationIntent.putExtra("id",id);

        PendingIntent broadcast2 = PendingIntent.getBroadcast(getApplicationContext(), id, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences.Editor editor=getSharedPreferences("bills",MODE_PRIVATE).edit();
        editor.putString(a.getText().toString()+id, "no");
        editor.commit();

        Calendar calx = Calendar.getInstance();
        calx.set(Calendar.MINUTE, 15);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calx.getTimeInMillis(), 60 * 1000, broadcast2);

        Intent cancellationIntent = new Intent(this, CancelAlarmBroadcastReceiver.class);
        //cancellationIntent.setAction("android.media.action.DISPLAY_NOTIFICATION");
        cancellationIntent.putExtra("name",notificationIntent.getStringExtra("name"));

        Log.d("AddBill","ABOUT TO REACH");
        cancellationIntent.putExtra("key",broadcast2);
        cal.set(Calendar.HOUR_OF_DAY,10);

        PendingIntent cancellationPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, cancellationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(),cancellationPendingIntent);
    }

    private void verifyCameraPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }

    public void getData(long c){
        count=c;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }
        else {
            return null;
        }

        return mediaFile;
    }
}
