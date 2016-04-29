package com.example.user.savethebill;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class ScrollingActivity extends AppCompatActivity  {
Firebase firebase;
    EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://savethebill.firebaseio.com");
        firebase.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    Toast.makeText(getApplicationContext(), "You  are logged in", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry!!", Toast.LENGTH_SHORT);

                }
            }
        });

    }
    Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
        @Override
        public void onAuthenticated(AuthData authData) {
            Intent intent=new Intent(getApplicationContext(),AllBills.class);
            startActivity(intent);
            // Authenticated successfully with payload authData
        }
        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            // Authenticated failed with error firebaseError
            Toast.makeText(ScrollingActivity.this, "error in logging in", Toast.LENGTH_SHORT).show();
        }
    };
    public void storing(){
        AuthData authData=firebase.getAuth();
        firebase.child(authData.getUid());
    }
    public void submit(View view){
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        final String emailText=email.getText().toString();
        final String passwordText=password.getText().toString();
        firebase.createUser(emailText, passwordText, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                firebase.authWithPassword(emailText, passwordText, authResultHandler);
                storing();
            }
            @Override
            public void onError(FirebaseError firebaseError) {

                // there was an error
            }
        });
    }

    public  void logIn(View view){
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        String emailText=email.getText().toString();
        String passwordText=password.getText().toString();

        firebase.authWithPassword(emailText, passwordText, authResultHandler);

    }


}
