package com.pizzashop.pizzashopexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pizzashop.pizzashopexample.Model.Users;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputNumber,Inputpassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;

    private String parentDbName="Admins";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton=(Button)findViewById(R.id.login_btn);
        Inputpassword=(EditText) findViewById(R.id.login_password_input);
        InputNumber=(EditText) findViewById(R.id.login_phone_number_input);
        loadingBar=new ProgressDialog(this);
        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginuser();
            }
        });
    }

    private void Loginuser() {
        String phone=InputNumber.getText().toString();
        String password=Inputpassword.getText().toString();

        if(TextUtils.isEmpty(phone)){
            InputNumber.setError("Enter Phone Number");
            Toast.makeText(this,"Please Write Your Ph.no... ",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)) {
            Inputpassword.setError("Enter Password");
            Toast.makeText(this, "Please Write Your Password... ", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
//            AllowAccessToAccount(phone, password);
            AllowLogin(password, phone);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password) {


        final DatabaseReference databaseReference;
        databaseReference= FirebaseDatabase.getInstance().getReference().child(parentDbName);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Admins").child(parentDbName).child("phone").exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child("phone").getValue(Users.class);
                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {
                            if (parentDbName.equals("Admins")) {
                                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                               // loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        } else {
                           // loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else
                    {
                    Toast.makeText(LoginActivity.this, "Account With this " + phone + " number do not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    //Toast.makeText(LoginActivity.this, "You need to create a new account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error Message", databaseError.getMessage().toString());
            }
        });
    }

    void AllowLogin(String Password, String phoneNo)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admins");
        reference.child("Admins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String DBPhone = snapshot.child("Admins").child("phone").getValue().toString();
                String DBPassword = snapshot.child("Admins").child("Password").getValue().toString();
                Log.d("Msg", "Password " + DBPassword + " Phone " + DBPhone);
                if (DBPhone.equals(phoneNo) && DBPassword.equals(Password))
                {
                    Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    //loadingBar.dismiss();
                    Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
                    //loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}