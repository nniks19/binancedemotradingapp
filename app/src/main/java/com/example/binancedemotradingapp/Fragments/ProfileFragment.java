package com.example.binancedemotradingapp.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.binancedemotradingapp.Activities.MainActivity;
import com.example.binancedemotradingapp.Activities.MenuActivity;
import com.example.binancedemotradingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;

    private Button btnLogout;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String userId = ((MenuActivity) requireActivity()).user_id;
        View returnView = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        btnLogout = returnView.findViewById(R.id.btnLogout);
        btnLogoutOnClickListener();
        EditText txtName = returnView.findViewById(R.id.editTextTextPersonName);
        EditText txtSurname = returnView.findViewById(R.id.editTextTextPersonSurname);
        EditText txtEmail = returnView.findViewById(R.id.editTextTextEmailAddress);
        EditText txtPhoneNumber = returnView.findViewById(R.id.editTextPhone);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txtName.setText(Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString());
                txtSurname.setText(Objects.requireNonNull(dataSnapshot.child("surname").getValue()).toString());
                txtEmail.setText(Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString());
                txtPhoneNumber.setText(Objects.requireNonNull(dataSnapshot.child("phoneNumber").getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return returnView;
    }
    private void btnLogoutOnClickListener(){

        btnLogout.setOnClickListener(view -> {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getString(R.string.are_you_sure_logout))
                    .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                        // Pritisak na tipku da
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        mAuth.signOut();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        requireActivity().finishAffinity();
                        Toast.makeText(getContext(), getString(R.string.logout_success),
                                Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(getString(R.string.no), (dialog, id) -> {
                        dialog.cancel();
                        Toast.makeText(getContext(), getString(R.string.cancel_logout),
                                Toast.LENGTH_SHORT).show();
                    });
            AlertDialog alert = builder.create();
            alert.show();

        });
    }
}

