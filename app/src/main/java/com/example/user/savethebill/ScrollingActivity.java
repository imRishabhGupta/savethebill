package com.example.user.savethebill;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://savethebill.firebaseio.com");
        firebase.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    Toast.makeText(getApplicationContext(),"You  are logged in",Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getApplicationContext(),"Sorry!!",Toast.LENGTH_SHORT);

                    }
            }
        });

    }

    public void submit(View view){
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        String emailText=email.getText().toString();
        String passwordText=password.getText().toString();
        firebase.authWithPassword(emailText, passwordText,
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        // Authentication just completed successfully :)
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("provider", authData.getProvider());
                        if (authData.getProviderData().containsKey("displayName")) {
                            map.put("displayName", authData.getProviderData().get("displayName").toString());
                        }
                        firebase.child("users").child(authData.getUid()).setValue(map);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError error) {
                        switch (error.getCode()) {
                            case FirebaseError.USER_DOES_NOT_EXIST:
                                // handle a non existing user
                                break;
                            case FirebaseError.INVALID_PASSWORD:
                                // handle an invalid password
                                break;
                            default:
                                // handle other errors
                                break;
                        }
                        // Something went wrong :(
                    }
                });
    }
    public  void logIn(View view){
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        String emailText=email.getText().toString();
        String passwordText=password.getText().toString();

        Firebase ref = new Firebase("https://savethebill.firebaseio.com");
// Create a handler to handle the result of the authentication
        Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                // Authenticated successfully with payload authData
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // Authenticated failed with error firebaseError
            }
        };
// Authenticate users with a custom Firebase token
        ref.authWithCustomToken("<token>", authResultHandler);
// Alternatively, authenticate users anonymously
        ref.authAnonymously(authResultHandler);
// Or with an email/password combination
        ref.authWithPassword(emailText,passwordText, authResultHandler);
// Or via popular OAuth providers ("facebook", "github", "google", or "twitter")
        ref.authWithOAuthToken("<provider>", "<oauth-token>", authResultHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
