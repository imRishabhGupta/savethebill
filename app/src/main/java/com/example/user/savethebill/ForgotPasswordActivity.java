package com.example.user.savethebill;

/**
 * Created by rohanagarwal94 on 31/1/17.
 */
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.email)
    TextInputLayout inputEmail;
    @BindView(R.id.btn_reset_password)
    Button btnReset;
    @BindView(R.id.btn_back)
    Button btnBack;


    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();

        if (savedInstanceState != null) {
            inputEmail.getEditText().setText(savedInstanceState.getCharSequenceArray("savedStates")[0].toString());
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @OnClick(R.id.btn_back)
    public void back(){
        finish();
    }

    @OnClick(R.id.btn_reset_password)
    public void reset() {
        if (CredentialHelper.checkIfEmpty(inputEmail, this)) {
            return;
        }
        if (!CredentialHelper.isEmailValid(inputEmail.getEditText().getText().toString())) {
            inputEmail.setError(getString(R.string.email_invalid_title));
            return;
        }
        inputEmail.setError(null);
        btnReset.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        auth.sendPasswordResetEmail(inputEmail.getEditText().getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if (task.isSuccessful()) {
                                                   Toast.makeText(getApplicationContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                               } else {
                                                   AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                                                   builder.setTitle(R.string.email_invalid_title);
                                                   builder.setMessage(R.string.email_invalid)
                                                           .setCancelable(false)
                                                           .setPositiveButton("Retry", null);
                                                   AlertDialog alert = builder.create();
                                                   alert.show();
                                                   Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                                                   pbutton.setTextColor(Color.RED);
                                               }

                                           }
                                       });

                btnReset.setEnabled(true);
                progressDialog.dismiss();

            }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        CharSequence values[] = {inputEmail.getEditText().getText().toString()};
        outState.putCharSequenceArray("savedStates", values);
    }

}