package com.example.user.savethebill;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditBill extends AppCompatActivity {

    private static final String TAG = EditBill.class.getSimpleName();
    EditText a,c,d,e;
    AutoCompleteTextView b;
    private Bitmap bitmap;
    String base64Image;
    private String billname, type, imagestring, endDate1, endDate2, nameofowner, id1, id2;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    String[] data=new String[6];
    private SimpleDateFormat dateFormatter;
    private Uri fileUri;
    Calendar cal1,cal2;
    String[] arr={"electricity bill","water bill","consumer bill","telephone bill","other bill"};
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    long count=7;
    String position;
    private ImageView imgPreview;
    private Button btnCapturePicture;
    private static final String IMAGE_DIRECTORY_NAME = "Saved Bills";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

        mDatabase.addValueEventListener(new ValueEventListener() {
            long cd;

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int i=0;
                long count = snapshot.getChildrenCount();
                System.out.println("testing....."+count);
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String str =  postSnapshot.getValue(String.class);
                    data[i]=str;
                    Log.d(TAG,data[i]);

                    i++;
                }
                billname=data[0];
                endDate1 =data[1];
                endDate2 =data[2];
                id1=data[3];
                id2=data[4];
                imagestring =data[5];
                nameofowner =data[6];
                type=data[7];

                a.setText(billname);
                c.setText(nameofowner);
                b.setText(type);
                d.setText(endDate1);
                e.setText(endDate2);
                if(imagestring !=null&&!imagestring.equals("")){
                    byte[] imageAsBytes = Base64.decode(imagestring.getBytes(), Base64.DEFAULT);
                    bitmap=BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                    imgPreview.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(EditBill.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(EditBill.this,
                            new String[]{Manifest.permission.CAMERA},1);

                }
                else if(ContextCompat.checkSelfPermission(EditBill.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(EditBill.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},3);
                }
                else
                    captureImage();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews(){

        final Intent intent = getIntent();
        position=intent.getStringExtra("position");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        String uid=mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference(uid+"/Bill"+position);

        a=(EditText)findViewById(R.id.bill_name);
        b=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        c=(EditText)findViewById(R.id.owner);
        d=(EditText)findViewById(R.id.enddate1);
        e=(EditText)findViewById(R.id.enddate2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, arr);

        b.setThreshold(1);
        b.setAdapter(adapter);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        imgPreview = (ImageView) findViewById(R.id.imageView);
        btnCapturePicture = (Button) findViewById(R.id.bu);

        d.setInputType(InputType.TYPE_NULL);

        e.setInputType(InputType.TYPE_NULL);
        setDateTimeField();
    }

    private void setDateTimeField() {
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fromDatePickerDialog.show();
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();


            }
        });

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cal1 = Calendar.getInstance();
                cal1.set(year, monthOfYear, dayOfMonth);
                d.setText(dateFormatter.format(cal1.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cal2 = Calendar.getInstance();
                cal2.set(year, monthOfYear, dayOfMonth);
                e.setText(dateFormatter.format(cal2.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
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
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void storeImageToFirebase() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
        System.out.println(base64Image);

    }

    private void previewCapturedImage() {
        try {


            imgPreview.setVisibility(View.VISIBLE);

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 4;

            bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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
        ProgressDialog progressDialog = new ProgressDialog(EditBill.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Editing bill...");
        progressDialog.show();

        SharedPreferences preferences=getSharedPreferences("bills", Context.MODE_PRIVATE);
        String cancel1=preferences.getString(billname+id1,null);
        String cancel2=preferences.getString(billname+id2,null);
        SharedPreferences.Editor editor=getSharedPreferences("bills",MODE_PRIVATE).edit();
        if(cancel1!=null)
            editor.putString(billname+id2, "yes");
        if(cancel2!=null)
            editor.putString(billname+id1, "yes");
        editor.commit();

        final Bill bill=new Bill();
        bill.setBillName(a.getText().toString());
        bill.setType(b.getText().toString());
        bill.setNameofowner(c.getText().toString());
        bill.setEndDate2(d.getText().toString());
        bill.setEndDate1(e.getText().toString());
        if(bitmap!=null)
        {
            storeImageToFirebase();
            bill.setImagestring(base64Image);
        }
        else
            bill.setImagestring("");

        setDateTimeField();

        Intent notificationIntent = new Intent(this,AlarmReceiver.class);
        notificationIntent.setAction("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.putExtra("name",a.getText().toString());
        notificationIntent.putExtra("type",b.getText().toString());
        if(cal1!=null&&cal1.getTimeInMillis()<=System.currentTimeMillis()){
            Toast.makeText(EditBill.this, "Date should be greater than current time.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(cal1!=null&&d.getText().toString().length()>0) {
            setRepeatingAlarm(notificationIntent,cal1, bill, 1);
        }
        if(cal2!=null&&cal2.getTimeInMillis()<=System.currentTimeMillis()){
            Toast.makeText(EditBill.this, "Date should be greater than current time.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(cal2!=null&&e.getText().toString().length()>0) {
            setRepeatingAlarm(notificationIntent,cal2,bill,2);
        }
        //System.out.println("got"+count);

        mDatabase.removeValue();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        String uid=mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference(uid+"/Bill"+position);
        mDatabase.child("Bill"+position).setValue(bill);
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(),"Bill edited successfully.",Toast.LENGTH_SHORT).show();

        Intent i=new Intent(getApplicationContext(),DisplayBill.class);
        i.putExtra("position",position);
        startActivity(i);
    }

    public void setRepeatingAlarm(Intent notificationIntent, Calendar cal, Bill bill,int number) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        notificationIntent.putExtra("daysleft",cal.getTime().getTime());

        int id = (int) System.currentTimeMillis();
        notificationIntent.putExtra("id",id);
        if(number==1)
            bill.setId1(id+"");
        else if(number==2)
            bill.setId2(id+"");

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
