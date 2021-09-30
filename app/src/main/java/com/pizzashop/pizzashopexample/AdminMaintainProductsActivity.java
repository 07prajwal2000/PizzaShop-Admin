package com.pizzashop.pizzashopexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applyChangesBtn,deleteBtn;
    private EditText name,price,description,discount;
    private ImageView imageView;
    private  String productID="";
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productID=getIntent().getStringExtra("pid");

        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);



        applyChangesBtn=findViewById(R.id.apply_changes_Btn);
        name=findViewById(R.id.product_name_maintain);
        price=findViewById(R.id.product_price_maintain);
        description=findViewById(R.id.product_description_maintain);
        discount=findViewById(R.id.product_discount_maintain);
        imageView=findViewById(R.id.product_image_maintain);
        deleteBtn=findViewById(R.id.delete_product_Btn);

        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteThisProduct();
            }
        });
    }

    private void DeleteThisProduct() {
        CharSequence options[]= new CharSequence[]
                {
                        "Remove",
                        "Cancel"
                };
        final AlertDialog.Builder builder=new AlertDialog.Builder(AdminMaintainProductsActivity.this);
        builder.setTitle("Are you sure want to remove this product ?");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(AdminMaintainProductsActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                            Toast.makeText(AdminMaintainProductsActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
        builder.show();


    }

    private void applyChanges() {
        String pName=name.getText().toString();
        String pPrice=price.getText().toString();
        String pDescription=description.getText().toString();
        String pDiscount=discount.getText().toString();

        if(pName.equals("")){
            Toast.makeText(this, "Enter Product Name", Toast.LENGTH_SHORT).show();
        }else if(pPrice.equals("")) {
            Toast.makeText(this, "Enter Product Price", Toast.LENGTH_SHORT).show();
        }else if(pDescription.equals("")) {
            Toast.makeText(this, "Enter Product Description", Toast.LENGTH_SHORT).show();
        }else {
            HashMap<String,Object> productMap= new HashMap<>();
            productMap.put("pid",productID);
            productMap.put("description",pDescription);
            productMap.put("price",pPrice);
            productMap.put("pname",pName);
            productMap.put("discount",pDiscount);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes applied Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainProductsActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void displaySpecificProductInfo() {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String pName=dataSnapshot.child("pname").getValue().toString();
                    String pPrice=dataSnapshot.child("price").getValue().toString();
                    String pDescription =dataSnapshot.child("description").getValue().toString();
                    String pImage=dataSnapshot.child("image").getValue().toString();
                    String pDiscount=dataSnapshot.child("discount").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    discount.setText(pDiscount);
                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {


        Intent intent =new Intent(AdminMaintainProductsActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}