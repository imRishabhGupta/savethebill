package com.example.user.savethebill;

/**
 * Created by rohanagarwal94 on 31/1/17.
 */
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email)
    TextInputLayout email;
    @BindView(R.id.password)
    TextInputLayout password;
    @BindView(R.id.email_input)
    AutoCompleteTextView autocompleteEmail;
    @BindView(R.id.log_in)
    Button logIn;
    private FirebaseAuth auth;
    private Set<String> savedEmails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();

        savedEmails = new HashSet<>();
        if(PrefManager.getStringSet("saved_emails")!=null) {
            savedEmails.addAll(PrefManager.getStringSet("saved_emails"));
            List<String> listOfEmails = new ArrayList<>(savedEmails);
            autocompleteEmail.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, listOfEmails));
        }

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, AllBills.class));
            finish();
        }

        if (savedInstanceState != null) {
            email.getEditText().setText(savedInstanceState.getCharSequenceArray("savedStates")[0].toString());
            password.getEditText().setText(savedInstanceState.getCharSequenceArray("savedStates")[1].toString());

        }
    }

    @OnClick(R.id.sign_up)
    public void signUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.forgot_password)
    public void forgotPassword() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.log_in)
    public void logIn() {
        if (CredentialHelper.checkIfEmpty(email, this) | CredentialHelper.checkIfEmpty(password, this)) {
            return;
        }
        if (!CredentialHelper.isEmailValid(email.getEditText().getText().toString())) {
            InvalidAcess();
            return;
        }
        email.setError(null);

        //TODO: USE AUTOCOMPLETEVIEW IN EMAIL TEXT INPUT

        logIn.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        auth.signInWithEmailAndPassword(email.getEditText().getText().toString(), password.getEditText().getText().toString())
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.getEditText().getText().toString().length() < 6) {
                                password.setError(getString(R.string.minimum_password));
                            } else {

                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(getApplicationContext(), "User with this email already exist.", Toast.LENGTH_SHORT).show();
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle(R.string.password_invalid_title);
                                builder.setMessage(R.string.password_invalid)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                return;
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                                pbutton.setTextColor(Color.BLUE);                            }
                        } else {

                            //Save email for easy login.
                            savedEmails.add(email.getEditText().getText().toString());
                            PrefManager.putStringSet("saved_emails", savedEmails);

                            Intent intent = new Intent(LoginActivity.this, AllBills.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    logIn.setEnabled(true);
    progressDialog.dismiss();

}

    public void InvalidAcess(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(R.string.email_invalid_title);
        builder.setMessage(R.string.email_invalid)
                .setCancelable(false)
                .setPositiveButton("RETRY", null);
        AlertDialog alert = builder.create();
        alert.show();
        Button ok = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setTextColor(Color.RED);
    }

    @OnEditorAction(R.id.password_input)
    public boolean onEditorAction(int actionId) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_GO) {
            logIn();
            handled = true;
        }
        return handled;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        CharSequence values[] = {email.getEditText().getText().toString(), password.getEditText().getText().toString() };
        outState.putCharSequenceArray("savedStates", values);

    }
}