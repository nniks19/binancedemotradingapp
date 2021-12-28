package com.example.binancedemotradingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnSignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        btnSignin = findViewById(R.id.btnSignin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User oUser = new User();
                EditText txtEmail = (EditText) findViewById(R.id.editTextLoginEmail);
                EditText txtPassword = (EditText) findViewById(R.id.editTextLoginPassword);
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                oUser.setEmail(email);
                oUser.setPassword(password);
                // Provjera: System.out.println(oUser.getEmail() + " " + oUser.getPassword());
                if(oUser.loginValidation()){
                    loginToAccount(oUser);
                } else{
                    Toast.makeText(LoginActivity.this, getFieldValidationMessage(oUser), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loginToAccount(User oUser) {
        mAuth.signInWithEmailAndPassword(oUser.getEmail(), oUser.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            String ex = ((FirebaseAuthException)task.getException()).getErrorCode();
                            Toast.makeText(LoginActivity.this, getErrorMessage(ex),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private String getErrorMessage(String errorCode){
        if (errorCode.equals("ERROR_CERTIFICATE_FETCH_FAILED")){
            return getString(R.string.CERTIFICATE_FETCH_FAILED);
        }else if (errorCode.equals("ERROR_EMAIL_ALREADY_EXISTS")) {
            return getString(R.string.EMAIL_ALREADY_EXISTS);
        }else if (errorCode.equals("ERROR_EXPIRED_ID_TOKEN")) {
            return getString(R.string.EXPIRED_ID_TOKEN);
        }else if (errorCode.equals("ERROR_EXPIRED_SESSION_COOKIE")) {
            return getString(R.string.EXPIRED_SESSION_COOKIE);
        }else if (errorCode.equals("ERROR_INVALID_DYNAMIC_LINK_DOMAIN")) {
            return getString(R.string.INVALID_DYNAMIC_LINK_DOMAIN);
        }else if (errorCode.equals("ERROR_INVALID_ID_TOKEN")) {
            return getString(R.string.INVALID_ID_TOKEN);
        }else if (errorCode.equals("ERROR_INVALID_SESSION_COOKIE")) {
            return getString(R.string.INVALID_SESSION_COOKIE);
        }else if (errorCode.equals("ERROR_PHONE_NUMBER_ALREADY_EXISTS")) {
            return getString(R.string.PHONE_NUMBER_ALREADY_EXISTS);
        }else if (errorCode.equals("ERROR_REVOKED_ID_TOKEN")) {
            return getString(R.string.REVOKED_ID_TOKEN);
        }else if (errorCode.equals("ERROR_REVOKED_SESSION_COOKIE")) {
            return getString(R.string.REVOKED_SESSION_COOKIE);
        }else if (errorCode.equals("ERROR_UNAUTHORIZED_CONTINUE_URL")) {
            return getString(R.string.UNAUTHORIZED_CONTINUE_URL);
        }else if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
            return getString(R.string.USER_NOT_FOUND);
        }else if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")){
            return getString(R.string.EMAIL_ALREADY_IN_USE);
        }else if (errorCode.equals("ERROR_INVALID_EMAIL")) {
            return getString(R.string.INVALID_EMAIL);
        }else if (errorCode.equals("ERROR_WRONG_PASSWORD")){
            return getString(R.string.WRONG_PASSWORD);
        }
        return "Nepoznata pogreška, kod greške: " + errorCode;
    }
    private void updateUI(FirebaseUser user) {
        if (user!= null){
            Intent intentMenuAct = new Intent(LoginActivity.this, MenuActivity.class);
            intentMenuAct.putExtra("login_success", getString(R.string.loginSuccess));
            intentMenuAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentMenuAct);
            finish();
        }
    }
    private String getFieldValidationMessage(User oUser){
        if (!oUser.checkifEnteredEmail()){
            return getString(R.string.empty_email);
        }
        if (!oUser.checkifEnteredPassword()){
            return getString(R.string.empty_password);
        }
        return "Nepoznata pogreška!";
    }
}