package com.pizzashop.pizzashopexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button getDataBtn = findViewById(R.id.getData);
        getDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDataFromFirebase();
            }
        });
    }

    void GetDataFromFirebase()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Admins");
        databaseReference.child("Admins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String phone = snapshot.child("phone").getValue(String.class);
                    String password = snapshot.child("password").getValue(String.class);
                    Toast.makeText(getApplicationContext(),"Phone " + phone + "\nPassword " + password , Toast.LENGTH_LONG).show();
                    Log.d("Message", "\"Phone " + phone + "\nPassword " + password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}