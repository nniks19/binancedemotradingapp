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

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
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
        Button btnRegister = findViewById(R.id.btnRegisterConfirm);
        mAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(view -> {
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
        });
    }
    private void createAccount(User oUser){
        mAuth.createUserWithEmailAndPassword(oUser.getEmail(), oUser.getPassword())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registracija uspjela
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(RegisterActivity.this, "Registracija je uspjela!", Toast.LENGTH_SHORT).show();
                        assert user != null;
                        oUser.setId(user.getUid());
                        oUser.writeNewUser(oUser);
                        updateUI(user);
                    } else {
                        // Registracija nije uspjela
                        String ex = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();
                        Toast.makeText(RegisterActivity.this, getErrorMessage(ex), Toast.LENGTH_LONG).show();
                        updateUI(null);
                    }
                });
    }
    // Ispis errora na lokaliziranom jeziku iz strings.xml
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