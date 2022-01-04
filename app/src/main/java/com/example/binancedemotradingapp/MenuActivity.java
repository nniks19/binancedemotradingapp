package com.example.binancedemotradingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {
    public String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String login_message = extras.getString("login_success");
                user_id = extras.getString("user_id");
                Toast.makeText(MenuActivity.this, login_message, Toast.LENGTH_SHORT).show();
                BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
                if (navHostFragment != null) {

                    NavController navController = navHostFragment.getNavController();

                    NavigationUI.setupWithNavController(bottomNavigationView,navController);

                }
            }
        }

    }
}