package com.example.binancedemotradingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.binancedemotradingapp.Models.Symbol;
import com.example.binancedemotradingapp.Models.SymbolResponse;
import com.example.binancedemotradingapp.R;
import com.example.binancedemotradingapp.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button btnRegister;
    private Button btnLogin;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                String register_message = extras.getString("register_success");
                Toast.makeText(MainActivity.this, register_message, Toast.LENGTH_SHORT).show();
            }

        }
        mAuth = FirebaseAuth.getInstance();
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Provjera je li korisnik prijavljen (non-null) i promjena activity-a ako je .
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        }
    }
}