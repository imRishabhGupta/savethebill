package com.example.user.savethebill;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScrollingActivity extends AppCompatActivity {

    @BindView(R.id.email)
    TextInputLayout email;
    @BindView(R.id.password)
    TextInputLayout password;
    @BindView(R.id.log_in)
    Button logIn;


    Firebase firebase;
    ProgressDialog progressDialog;
    private String emailText;
    private String passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ButterKnife.bind(this);
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
            progressDialog.dismiss();
            Intent intent = new Intent(getApplicationContext(), AllBills.class);
            startActivity(intent);
            // Authenticated successfully with payload authData
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            // Authenticated failed with error firebaseError
            progressDialog.dismiss();
            Toast.makeText(ScrollingActivity.this, "error in logging in", Toast.LENGTH_SHORT).show();
        }
    };

    public void storing() {
        AuthData authData = firebase.getAuth();
        firebase.child(authData.getUid());
    }

    public void submit(View view) {
        progressDialog = new ProgressDialog(ScrollingActivity.this,
                ProgressDialog.THEME_HOLO_LIGHT);

        progressDialog.setMessage("Creating new account...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        emailText = email.getEditText().getText().toString();
        passwordText = password.getEditText().getText().toString();

        if (emailText.equals("") || passwordText.equals("")) {
            progressDialog.dismiss();
            Toast.makeText(ScrollingActivity.this, "Both fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }
        firebase.createUser(emailText, passwordText, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                firebase.authWithPassword(emailText, passwordText, authResultHandler);
                storing();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                progressDialog.dismiss();
                Toast.makeText(ScrollingActivity.this, "error in creating new account", Toast.LENGTH_SHORT).show();
                // there was an error
            }
        });
    }

    public void logIn(View view) {

        progressDialog = new ProgressDialog(ScrollingActivity.this,
                ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        emailText = email.getEditText().getText().toString();
        passwordText = password.getEditText().getText().toString();

        if (emailText.equals("") || passwordText.equals("")) {
            progressDialog.dismiss();
            Toast.makeText(ScrollingActivity.this, "Both fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }
        firebase.authWithPassword(emailText, passwordText, authResultHandler);
    }

}
