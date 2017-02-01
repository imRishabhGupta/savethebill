package com.example.user.savethebill;

/**
 * Created by rohanagarwal94 on 31/1/17.
 */
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.email)
    TextInputLayout email;
    @BindView(R.id.password)
    TextInputLayout password;
    @BindView(R.id.confirm_password)
    TextInputLayout confirmPassword;
    @BindView(R.id.sign_up)
    Button signUp;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        if(savedInstanceState!=null){
            email.getEditText().setText(savedInstanceState.getCharSequenceArray("savedStates")[0].toString());
            password.getEditText().setText(savedInstanceState.getCharSequenceArray("savedStates")[1].toString());
            confirmPassword.getEditText().setText(savedInstanceState.getCharSequenceArray("savedStates")[2].toString());
        }
        setupPasswordWatcher();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupPasswordWatcher() {
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    CredentialHelper.checkPasswordValid(password, SignUpActivity.this);
                }
            }
        });
    }

    @OnClick(R.id.sign_up)
    public void signUp() {
        if (CredentialHelper.checkIfEmpty(email, this) |
                CredentialHelper.checkIfEmpty(password, this) |
                CredentialHelper.checkIfEmpty(confirmPassword, this)) {
            return;
        }
        if (!CredentialHelper.isEmailValid(email.getEditText().getText().toString())) {
            email.setError("Invalid email");
            return;
        }
        email.setError(null);
        if (!CredentialHelper.checkPasswordValid(password, this)) {
            return;
        }
        if (!password.getEditText().getText().toString()
                .equals(confirmPassword.getEditText().getText().toString())) {
            confirmPassword.setError("Passwords do not match");
            return;
        }
        confirmPassword.setError(null);
        signUp.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing up...");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email.getEditText().getText().toString(), password.getEditText().getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(), "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });

                signUp.setEnabled(true);
                progressDialog.dismiss();
                CredentialHelper.clearFields(email, password, confirmPassword);

            }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        CharSequence values[] = {email.getEditText().getText().toString(), password.getEditText().getText().toString(), confirmPassword.getEditText().getText().toString() };
        outState.putCharSequenceArray("savedStates", values);
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUpActivity.this);

        alertDialog.setCancelable(false);
        alertDialog.setMessage(R.string.error_cancelling_signUp_process_text);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SignUpActivity.super.onBackPressed();
            }
        });
        alertDialog.setNegativeButton("No",null);

        AlertDialog alert = alertDialog.create();
        alert.show();


        Button yes = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button no = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        yes.setTextColor(getResources().getColor(R.color.md_blue_500));
        no.setTextColor(getResources().getColor(R.color.md_red_500));
    }
}
