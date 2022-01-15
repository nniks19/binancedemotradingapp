package com.example.binancedemotradingapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.binancedemotradingapp.R;
import com.example.binancedemotradingapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnRegister;
    private EditText txtName;
    private EditText txtSurname;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtPin;
    private EditText txtPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegister = findViewById(R.id.btnRegisterConfirm);
        mAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User oUser = new User();
                txtName = (EditText) findViewById(R.id.editTextRegName);
                txtSurname = (EditText) findViewById(R.id.editTextRegSurname);
                txtEmail = (EditText) findViewById(R.id.editTextRegEmail);
                txtPassword = (EditText) findViewById(R.id.editTextRegPassword);
                txtPin = (EditText) findViewById(R.id.editTextRegPin);
                txtPhoneNumber = (EditText) findViewById(R.id.editTextRegPhone);
                String name = txtName.getText().toString();
                String surname = txtSurname.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String pin = txtPin.getText().toString();
                String phonenum = txtPhoneNumber.getText().toString();
                oUser.setName(name);
                oUser.setSurname(surname);
                oUser.setEmail(email);
                oUser.setPassword(password);
                oUser.setPin(pin);
                oUser.setPhoneNumber(phonenum);
                // Provjera: System.out.println(oUser.getNameSurname() + " " + oUser.getEmail() + " " + oUser.getPassword() + " " + oUser.getPIN() + " " + oUser.getPhoneNumber());
                if(oUser.registerValidation()){
                    createAccount(oUser);
                } else{
                    Toast.makeText(RegisterActivity.this, getFieldValidationMessage(oUser), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void createAccount(User oUser){
        mAuth.createUserWithEmailAndPassword(oUser.getEmail(), oUser.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registracija uspjela
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Registracija je uspjela!", Toast.LENGTH_SHORT).show();
                            oUser.setId(user.getUid());
                            oUser.writeNewUser(oUser);
                            updateUI(user);
                        } else {
                            // Registracija nije uspjela
                            String ex = ((FirebaseAuthException)task.getException()).getErrorCode();
                            Toast.makeText(RegisterActivity.this, getErrorMessage(ex), Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });
    }
    // Ispis errora na lokaliziranom jeziku iz strings.xml
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
        }
       return "Nepoznata pogreška, kod greške: " + errorCode;
    }
    private void updateUI(FirebaseUser user) {
        if (user!= null){
            Intent intentMainAct = new Intent(RegisterActivity.this, MainActivity.class);
            intentMainAct.putExtra("register_success", getString(R.string.registrationSuccess));
            intentMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentMainAct);
            finish();
        }
    }
    private String getFieldValidationMessage(User oUser){
        if (!oUser.checkifEnteredName()){
            return getString(R.string.empty_name);
        }
        if (!oUser.checkifEnteredSurname()){
            return getString(R.string.empty_surname);
        }
        if (!oUser.checkifEnteredEmail()){
            return getString(R.string.empty_email);
        }
        if (!oUser.checkifEnteredPassword()){
            return getString(R.string.empty_password);
        }
        if (!oUser.checkifEnteredPin()){
            return getString(R.string.empty_pin);
        }
        if (!oUser.checkIfPinHasFourDigits()){
            return getString(R.string.pin_digits_match);
        }
        if (!oUser.checkifEnteredPhoneNum()){
            return getString(R.string.empty_phone_num);
        }

        return "Nepoznata pogreška!";
    }
}