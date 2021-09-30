package com.pizzashop.pizzashopexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView addPizza,addDrinks,addSnacks;
    private Button maintainProducts,checkOrders,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        addPizza=findViewById(R.id.pizza_add_btn);
        addDrinks=findViewById(R.id.drinks_add_btn);
        addSnacks=findViewById(R.id.snacks_add_btn);

        maintainProducts=findViewById(R.id.admin_maintain_product_btn);
        checkOrders=findViewById(R.id.admin_check_orders_btn);
        logout=findViewById(R.id.logout_btn);

        addPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","pizza");
                startActivity(intent);
            }
        });

        addDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","drinks");
                startActivity(intent);
            }
        });

        addSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","snacks");
                startActivity(intent);
            }
        });

        maintainProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        checkOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        CharSequence options[]= new CharSequence[]{
                "Logout",
                "Cancel"
        };

        AlertDialog.Builder builder= new AlertDialog.Builder(AdminCategoryActivity.this);
        builder.setTitle("Sure want to Logout ?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    Intent intent = new Intent(AdminCategoryActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    finish();
                }
            }
        });
        builder.show();
    }
}
