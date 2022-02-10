package com.example.binancedemotradingapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.binancedemotradingapp.Models.User;
import com.example.binancedemotradingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        Button btnSignin = findViewById(R.id.btnSignin);
        btnSignin.setOnClickListener(view -> {
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
        });
    }
    private void loginToAccount(User oUser) {
        mAuth.signInWithEmailAndPassword(oUser.getEmail(), oUser.getPassword())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        String ex = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();
                        Toast.makeText(LoginActivity.this, getErrorMessage(ex),
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }
    private String getErrorMessage(String errorCode){
        switch (errorCode) {
            case "ERROR_CERTIFICATE_FETCH_FAILED":
                return getString(R.string.CERTIFICATE_FETCH_FAILED);
            case "ERROR_EMAIL_ALREADY_EXISTS":
                return getString(R.string.EMAIL_ALREADY_EXISTS);
            case "ERROR_EXPIRED_ID_TOKEN":
                return getString(R.string.EXPIRED_ID_TOKEN);
            case "ERROR_EXPIRED_SESSION_COOKIE":
                return getString(R.string.EXPIRED_SESSION_COOKIE);
            case "ERROR_INVALID_DYNAMIC_LINK_DOMAIN":
                return getString(R.string.INVALID_DYNAMIC_LINK_DOMAIN);
            case "ERROR_INVALID_ID_TOKEN":
                return getString(R.string.INVALID_ID_TOKEN);
            case "ERROR_INVALID_SESSION_COOKIE":
                return getString(R.string.INVALID_SESSION_COOKIE);
            case "ERROR_PHONE_NUMBER_ALREADY_EXISTS":
                return getString(R.string.PHONE_NUMBER_ALREADY_EXISTS);
            case "ERROR_REVOKED_ID_TOKEN":
                return getString(R.string.REVOKED_ID_TOKEN);
            case "ERROR_REVOKED_SESSION_COOKIE":
                return getString(R.string.REVOKED_SESSION_COOKIE);
            case "ERROR_UNAUTHORIZED_CONTINUE_URL":
                return getString(R.string.UNAUTHORIZED_CONTINUE_URL);
            case "ERROR_USER_NOT_FOUND":
                return getString(R.string.USER_NOT_FOUND);
            case "ERROR_EMAIL_ALREADY_IN_USE":
                return getString(R.string.EMAIL_ALREADY_IN_USE);
            case "ERROR_INVALID_EMAIL":
                return getString(R.string.INVALID_EMAIL);
            case "ERROR_WRONG_PASSWORD":
                return getString(R.string.WRONG_PASSWORD);
        }
        return "Nepoznata pogreška, kod greške: " + errorCode;
    }
    private void updateUI(FirebaseUser user) {
        if (user!= null){
            Intent intentMenuAct = new Intent(LoginActivity.this, MenuActivity.class);
            intentMenuAct.putExtra("login_success", getString(R.string.loginSuccess));
            intentMenuAct.putExtra("user_id", user.getUid());
            intentMenuAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentMenuAct);
            finishAffinity();
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